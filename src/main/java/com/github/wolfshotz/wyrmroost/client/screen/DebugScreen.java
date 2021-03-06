package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class DebugScreen extends Screen
{
    public final AbstractDragonEntity dragon;
    
    public DebugScreen(AbstractDragonEntity dragon)
    {
        super(new StringTextComponent("debug_screen"));
        
        this.dragon = dragon;
    }
    
    @Override
    protected void init()
    {
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

        drawCenteredString(ms, textRenderer, dragon.getDisplayName().asString(), (width / 2), 15, 0xffffff);
        drawCenteredString(ms, textRenderer, "isSleeping: " + dragon.isSleeping(), (width / 2) + 50, 50, 0xffffff);
        drawCenteredString(ms, textRenderer, "isTamed: " + dragon.isTamed(), (width / 2) - 50, 50, 0xffffff);
        drawCenteredString(ms, textRenderer, "isSitting: " + dragon.isInSittingPose(), (width / 2) - 50, 75, 0xffffff);
        drawCenteredString(ms, textRenderer, "isFlying: " + dragon.isFlying(), (width / 2) + 50, 75, 0xffffff);
        drawCenteredString(ms, textRenderer, "variant: " + dragon.getVariant(), (width / 2) - 50, 100, 0xffffff);
        drawCenteredString(ms, textRenderer, "gender: " + gender, (width / 2) + 50, 100, 0xffffff);
        drawCenteredString(ms, textRenderer, "health: " + dragon.getHealth() + " / " + dragon.getMaxHealth(), (width / 2) - 50, 125, 0xffffff);
        drawCenteredString(ms, textRenderer, "noAI: " + dragon.isAiDisabled(), (width / 2) + 50, 125, 0xffffff);
        drawCenteredString(ms, textRenderer, "position: " + dragon.getPos(), (width / 2), 150, 0xffffff);
        drawCenteredString(ms, textRenderer, "motion: " + dragon.getVelocity(), (width / 2), 175, 0xffffff);
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }

    public static void open(AbstractDragonEntity dragon)
    {
        Minecraft.getInstance().openScreen(new DebugScreen(dragon));
    }
}
