package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.util.animation.Animation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.AbstractButton;
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
                addButton(new AbstractButton((i * 50) + (width / 2) - (animations.length * 25), 200, 50, 12, new StringTextComponent("Anim: " + i))
                {
                    @Override
                    public void onPress()
                    {
                        dragon.setAnimation(animation);
                        closeScreen();
                    }
                });
            }
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        String gender = dragon.isMale()? "male" : "female";

        drawCenteredString(ms, font, dragon.getDisplayName().getUnformattedComponentText(), (width / 2), 15, 0xffffff);
        drawCenteredString(ms, font, "isSleeping: " + dragon.isSleeping(), (width / 2) + 50, 50, 0xffffff);
        drawCenteredString(ms, font, "isTamed: " + dragon.isTamed(), (width / 2) - 50, 50, 0xffffff);
        drawCenteredString(ms, font, "isSitting: " + dragon.func_233684_eK_(), (width / 2) - 50, 75, 0xffffff);
        drawCenteredString(ms, font, "isFlying: " + dragon.isFlying(), (width / 2) + 50, 75, 0xffffff);
        drawCenteredString(ms, font, "variant: " + dragon.getVariant(), (width / 2) - 50, 100, 0xffffff);
        drawCenteredString(ms, font, "gender: " + gender, (width / 2) + 50, 100, 0xffffff);
        drawCenteredString(ms, font, "health: " + dragon.getHealth() + " / " + dragon.getMaxHealth(), (width / 2) - 50, 125, 0xffffff);
        drawCenteredString(ms, font, "noAI: " + dragon.isAIDisabled(), (width / 2) + 50, 125, 0xffffff);
        drawCenteredString(ms, font, "position: " + dragon.getPositionVec(), (width / 2), 150, 0xffffff);
        drawCenteredString(ms, font, "motion: " + dragon.getMotion(), (width / 2), 175, 0xffffff);
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }

    public static void open(AbstractDragonEntity dragon)
    {
        Minecraft.getInstance().displayGuiScreen(new DebugScreen(dragon));
    }
}
