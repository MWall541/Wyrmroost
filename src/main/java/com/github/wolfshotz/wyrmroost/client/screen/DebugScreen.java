package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
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

public class DebugScreen extends Screen
{
    public final TameableDragonEntity dragon;
    private boolean paused = true;
    public final Map<String, ModelRenderer> boxList;
    public final List<TransformationWidget> transformations = new ArrayList<>();

    public DebugScreen(TameableDragonEntity dragon)
    {
        super(new StringTextComponent("debug_screen"));

        this.dragon = dragon;
        this.boxList = referBoxList();
    }

    @Override
    protected void init()
    {
        addButton(new Button(0, 0, 50, 20, new StringTextComponent("Pause Game"), b -> paused = !paused));

        Animation[] animations = dragon.getAnimations();
        if (animations != null && animations.length > 0)
            for (int i = 0; i < animations.length; i++)
            {
                Animation animation = animations[i];
                addButton(new Button((i * 50) + (width / 2) - (animations.length * 25), 200, 50, 12, new StringTextComponent("Anim: " + i), b ->
                {
                    dragon.setAnimation(animation);
                    onClose();
                }));
            }
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        String gender = dragon.isMale()? "male" : "female";

        drawCenteredString(ms, font, dragon.getDisplayName().getString(), (width / 2), 15, 0xffffff);
        drawCenteredString(ms, font, "isSleeping: " + dragon.isSleeping(), (width / 2) + 50, 50, 0xffffff);
        drawCenteredString(ms, font, "isTamed: " + dragon.isTame(), (width / 2) - 50, 50, 0xffffff);
        drawCenteredString(ms, font, "isSitting: " + dragon.isInSittingPose(), (width / 2) - 50, 75, 0xffffff);
        drawCenteredString(ms, font, "isFlying: " + dragon.isFlying(), (width / 2) + 50, 75, 0xffffff);
        drawCenteredString(ms, font, "variant: " + dragon.getVariant(), (width / 2) - 50, 100, 0xffffff);
        drawCenteredString(ms, font, "gender: " + gender, (width / 2) + 50, 100, 0xffffff);
        drawCenteredString(ms, font, "health: " + dragon.getHealth() + " / " + dragon.getMaxHealth(), (width / 2) - 50, 125, 0xffffff);
        drawCenteredString(ms, font, "noAI: " + dragon.isNoAi(), (width / 2) + 50, 125, 0xffffff);
        drawCenteredString(ms, font, "position: " + dragon.position(), (width / 2), 150, 0xffffff);
        drawCenteredString(ms, font, "motion: " + dragon.getDeltaMovement(), (width / 2), 175, 0xffffff);
    }

    @Override
    public boolean isPauseScreen()
    {
        return paused;
    }

    private Map<String, ModelRenderer> referBoxList()
    {
        EntityRenderer<?> renderer = minecraft.getEntityRenderDispatcher().getRenderer(dragon);
        if (renderer instanceof LivingRenderer<?, ?>)
        {
            EntityModel<?> model = ((LivingRenderer<?, ?>) renderer).getModel();
            return Arrays.stream(model.getClass().getFields())
                    .filter(f -> f.getType().isInstance(ModelRenderer.class))
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

    public void positionModel()
    {
        transformations.forEach(TransformationWidget::patchBox);
    }

    public static void open(TameableDragonEntity dragon)
    {
        Minecraft.getInstance().setScreen(new DebugScreen(dragon));
    }

    public class TransformationWidget extends TextFieldWidget
    {
        private final String name;
        private final ModelRenderer box;
        private final boolean rotate;
        float xT, yT, zT;

        public TransformationWidget(String name, ModelRenderer box, int x, int y, boolean rotateOrMove)
        {
            super(font, x, y, 103, 12, new StringTextComponent("ye"));
            this.box = box;
            this.name = name;
            this.rotate = rotateOrMove;

            setResponder(this::updateCords);
        }

        @Override
        public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
        {
            super.renderButton(ms, mouseX, mouseY, partialTicks);
            font.draw(ms, name, x - 7, y, 0xFFFFFF);
        }

        public void updateCords(String text)
        {
            String[] coords = text.trim().split(",", 3);
            xT = Float.parseFloat(coords[0]);
            yT = Float.parseFloat(coords[1]);
            zT = Float.parseFloat(coords[2]);
        }

        public void patchBox()
        {
            if (rotate)
            {
                box.xRot = xT;
                box.yRot = yT;
                box.zRot = zT;
            }
            else
            {
                box.x = xT;
                box.y = yT;
                box.z = zT;
            }
        }

        public String getOutput()
        {
            return String.format("%s(%s, %s, %s, %s);", rotate? "rotate" : "move", name, xT, yT, zT);
        }
    }
}
