package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class DebugScreen extends Screen
{
    public final TameableDragonEntity dragon;
    private boolean paused = true;

    public DebugScreen(TameableDragonEntity dragon)
    {
        super(new StringTextComponent("debug_screen"));

        this.dragon = dragon;
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

    public static void open(TameableDragonEntity dragon)
    {
        Minecraft.getInstance().setScreen(new DebugScreen(dragon));
    }
}
