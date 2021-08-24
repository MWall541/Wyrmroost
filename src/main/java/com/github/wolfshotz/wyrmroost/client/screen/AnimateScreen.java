package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.text.StringTextComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimateScreen extends Screen
{
    public static AnimateScreen last;

    private final Map<String, ModelRenderer> boxes;
    public final TameableDragonEntity dragon;
    public final List<TransformationWidget> transformations = new ArrayList<>();
    private TextFieldWidget boxAdder;
    private String error;

    protected AnimateScreen(TameableDragonEntity dragon)
    {
        super(new StringTextComponent("Debug: Animate Screen"));

        this.dragon = dragon;
        this.boxes = referBoxList();
    }

    @Override
    protected void init()
    {
        super.init();

        addButton(new Button(34, 0, 20, 20, new StringTextComponent("+"), b -> addTransformer(true), (b, m, x, y) -> renderTooltip(m, new StringTextComponent("Add Rotation Box"), x, y)));
        addButton(new Button(55, 0, 20, 20, new StringTextComponent("<+>"), b -> addTransformer(false), (b, m, x, y) -> renderTooltip(m, new StringTextComponent("Add Position Box"), x, y)));
        addButton(new Button(77, 0, 80, 20, new StringTextComponent("Print Positions"), b -> printPositions()));
        addButton(new Button(0, 0, 33, 20, new StringTextComponent("Clear"), b ->
        {
            onClose();
            last = null;
        }));

        transformations.forEach(TransformationWidget::init);
        boxAdder = addWidget(new TextFieldWidget(font, 1, 22, 100, 9, new StringTextComponent("Box Name...")));
        error = null;
    }

    private void printPositions()
    {
        if (!transformations.isEmpty())
        {
            StringBuilder builder = new StringBuilder("Printing Positions");
            for (TransformationWidget w : transformations) builder.append("\n").append(w.getOutput());
            System.out.println(builder);
        }
    }

    private Map<String, ModelRenderer> referBoxList()
    {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(dragon);
        if (renderer instanceof LivingRenderer<?, ?>)
        {
            EntityModel<?> model = ((LivingRenderer<?, ?>) renderer).getModel();
            return Arrays.stream(model.getClass().getFields())
                    .filter(f -> ModelRenderer.class.isAssignableFrom(f.getType()))
                    .collect(Collectors.toMap(Field::getName, f ->
                    {
                        try
                        {
                            return (ModelRenderer) f.get(model);
                        }
                        catch (Throwable e)
                        {
                            throw new RuntimeException(e);
                        }
                    }));
        }
        return null;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        super.render(ms, mouseX, mouseY, partialTicks);
        transformations.forEach(b -> b.render(ms, mouseX, mouseY, partialTicks));
        boxAdder.render(ms, mouseX, mouseY, partialTicks);
        font.drawShadow(ms, error, 160, 6, 0xFF0000);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        printPositions();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    public void positionModel()
    {
        transformations.forEach(TransformationWidget::patchBox);
    }

    public void addTransformer(boolean rotate)
    {
        String name = boxAdder.getValue();
        ModelRenderer box = boxes.get(name);
        if (box == null)
        {
            error = "No box found for the name: \"" + name + "\"";
            return;
        }
        else error = null;

        transformations.add(new TransformationWidget(name, box, 50, rotate));
    }

    public static void open(TameableDragonEntity dragon)
    {
        if (last == null || last.dragon != dragon) last = new AnimateScreen(dragon);
        Minecraft.getInstance().setScreen(last);
    }

    public class TransformationWidget extends TextFieldWidget
    {
        private final String name;
        private final ModelRenderer box;
        private Button closeButton;
        private final boolean rotate;
        float xT, yT, zT;

        public TransformationWidget(String name, ModelRenderer box, int x, boolean rotateOrMove)
        {
            super(font, x, 0, 100, 13, new StringTextComponent("ye"));
            this.box = box;
            this.name = name;
            this.rotate = rotateOrMove;

            setValue("0, 0, 0");
            setResponder(this::updateCords);
            init();
        }

        public void init()
        {
            addButton(closeButton = new Button(x + 103, y, 13, 13, new StringTextComponent("X"), b ->
            {
                close();
                int i = transformations.indexOf(this);
                transformations.remove(this);
                transformations.subList(i, transformations.size()).forEach(TransformationWidget::reposition);
            }));

            reposition();

            addWidget(this);
        }

        public void reposition()
        {
            int i = transformations.indexOf(this);
            if (i == -1) i = transformations.size();
            y = i * 14 + 35;
            closeButton.y = y;
        }

        public void close()
        {
            children.remove(this);
            buttons.remove(closeButton);
            children.remove(closeButton);
        }

        @Override
        public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
        {
            super.renderButton(ms, mouseX, mouseY, partialTicks);
            font.drawShadow(ms, name, x - 45, y + 3f, 0xFFFFFF);
        }

        public void updateCords(String text)
        {
            try
            {
                String[] coords = text.trim().split(",", 3);
                xT = Float.parseFloat(coords[0]);
                yT = Float.parseFloat(coords[1]);
                zT = Float.parseFloat(coords[2]);
                error = null;
            }
            catch (Throwable e)
            {
                error = "Invalid cords received for: \"" + name + "\"";
            }
        }

        public void patchBox()
        {
            if (rotate)
            {
                box.xRot += xT;
                box.yRot += yT;
                box.zRot += zT;
            }
            else
            {
                box.x += xT;
                box.y += yT;
                box.z += zT;
            }
        }

        public String getOutput()
        {
            return String.format("%s(%s, %sf, %sf, %sf);", rotate? "rotate" : "move", name, xT, yT, zT);
        }
    }
}
