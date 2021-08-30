package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
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
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimateScreen extends Screen
{
    public static AnimateScreen last;

    public final TameableDragonEntity dragon;
    private final Map<String, ModelRenderer> boxes;
    private final List<TransformationWidget> transformations = new ArrayList<>();
    private ReloadWidget reloader;
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
        transformations.forEach(TransformationWidget::init);
        if (reloader != null) reloader = new ReloadWidget();
        error = null;

        addButton(new Button(0, 0, 33, 20, new StringTextComponent("Clear"), b ->
        {
            onClose();
            last = null;
        }));
        addButton(new Button(34, 0, 20, 20, new StringTextComponent("+"), b -> addTransformer(true), (b, m, x, y) -> renderTooltip(m, new StringTextComponent("Add Rotation Box"), x, y)));
        addButton(new Button(55, 0, 20, 20, new StringTextComponent("<+>"), b -> addTransformer(false), (b, m, x, y) -> renderTooltip(m, new StringTextComponent("Add Position Box"), x, y)));
        addButton(new Button(77, 0, 80, 20, new StringTextComponent("Print Positions"), b -> printPositions()));
        addButton(new Button(160, 0, 90, 20, new StringTextComponent("Reload Positions"), b ->
        {
            if (reloader == null) addWidget(reloader = new ReloadWidget());
        }));

        addWidget(boxAdder = new TextFieldWidget(font, 1, 22, 100, 9, new StringTextComponent("")));
    }

    private void printPositions()
    {
        if (!transformations.isEmpty())
        {
            StringBuilder builder = new StringBuilder("Printing Positions");
            for (TransformationWidget w : transformations)
            {
                if (w.xT == 0 && w.yT == 0 && w.zT == 0) continue;
                builder.append("\n").append(w.getOutput());
            }
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
        if (reloader != null) reloader.render(ms, mouseX, mouseY, partialTicks);
        font.drawShadow(ms, error, 105, 22, 0xFF0000);
    }

    @Override
    public boolean mouseScrolled(double width, double height, double amount)
    {
        for (TransformationWidget w : transformations) w.setWidgetY(w.y + ((int) amount * 3));
        return super.mouseScrolled(width, height, amount);
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

        transformations.add(new TransformationWidget(name, box, rotate));
    }

    public static void open(TameableDragonEntity dragon)
    {
        if (last == null || last.dragon != dragon) last = new AnimateScreen(dragon);
        else last.init();
        Minecraft.getInstance().setScreen(last);
    }

    public class TransformationWidget extends TextFieldWidget
    {
        private final String name;
        private final ModelRenderer box;
        private Button closeButton;
        private final boolean rotate;
        private float xT, yT, zT;

        public TransformationWidget(String name, ModelRenderer box, boolean rotateOrMove)
        {
            super(font, 60, 0, 90, 13, new StringTextComponent(""));
            this.box = box;
            this.name = name;
            this.rotate = rotateOrMove;

            setValue("0, 0, 0");
            setResponder(this::updateCords);
            setTextColor(rotateOrMove? 0xFFFFFF : 0x00FFFF);
            init();
        }

        public TransformationWidget(String name, String input, boolean rotateOrMove)
        {
            this(name, boxes.get(name), rotateOrMove);
            updateCords(input);
            setValue(String.format("%s, %s, %s", xT, yT, zT));
        }

        public void init()
        {
            addButton(closeButton = new Button(x + 92, y, 13, 13, new StringTextComponent("X"), b ->
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
            if (transformations.isEmpty() || i == 0) setWidgetY(35);
            else
            {
                if (i == -1) i = transformations.size();
                setWidgetY(transformations.get(i - 1).y + 14);
            }
        }

        public void close()
        {
            children.remove(this);
            buttons.remove(closeButton);
            children.remove(closeButton);
        }

        @Override
        public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
        {
            closeButton.visible = visible;
            super.render(ms, mouseX, mouseY, partialTicks);
        }

        @Override
        public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
        {
            super.renderButton(ms, mouseX, mouseY, partialTicks);
            font.drawShadow(ms, name, 1, y + 3f, 0xFFFFFF);
        }

        public void setWidgetY(int y)
        {
            this.y = y;
            closeButton.y = y;

            visible = y > 31 || y > AnimateScreen.this.height;
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

    public class ReloadWidget extends TextFieldWidget
    {
        private Button closeButton;
        private Button parseButton;
        private Button pathButton;

        public ReloadWidget()
        {
            super(font, AnimateScreen.this.width / 2 - 100, AnimateScreen.this.height / 2 - 22, 200, 45, new StringTextComponent(""));
            setValue("Insert Path to file...");
            setMaxLength(Integer.MAX_VALUE);
            init();
        }

        public void init()
        {
            addButton(closeButton = new Button(x + width + 5, y + 1, 20, 20, new StringTextComponent("X"), b -> close()));
            addButton(parseButton = new Button(x + width / 2 - 40, y + height + 10, 80, 20, new StringTextComponent("Parse"), b -> parseAndReload()));
            addButton(pathButton = new Button(x + width + 5, y + 23, 60, 20, new StringTextComponent("Get Path"), b -> setValue(System.getProperty("user.home") + "/Desktop/")));
        }

        private void parseAndReload()
        {
            try
            {
                for (String line : Files.readAllLines(Paths.get(getValue())))
                {
                    line = line.trim();
                    try
                    {
                        int para = line.indexOf("(");
                        String rotateOrMove = line.substring(0, para);
                        String substring = line.substring(para + 1, line.indexOf(")") - 1);
                        String[] split = substring.split(",", 2);
                        transformations.add(new TransformationWidget(split[0], split[1], !rotateOrMove.equals("move")));
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        Wyrmroost.LOG.error("Invalid line: " + line);
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        Wyrmroost.LOG.error("Invalid line has too few arguments: " + line);
                    }
                }
                error = null;
                close();
            }
            catch (NoSuchFileException e)
            {
                e.printStackTrace();
                error = "ayo try actually supplying a real file.";
            }
            catch (Exception e)
            {
                e.printStackTrace();
                error = "Something went horrible wrong trying to parse this object; check logs.";
            }
        }

        public void close()
        {
            children.remove(parseButton);
            children.remove(closeButton);
            children.remove(pathButton);
            buttons.remove(parseButton);
            buttons.remove(closeButton);
            buttons.remove(pathButton);
            children.remove(this);
            reloader = null;
        }
    }
}
