package WolfShotz.Wyrmroost.client.screen;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.ai.controller.MovementController;
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
                addButton(new AbstractButton((i * 50) + (width / 2) - (animations.length * 25), 265, 50, 12, "Anim: " + i)
                {
                    @Override
                    public void onPress()
                    {
                        dragon.setAnimation(animation);
                        onClose();
                    }
                });
            }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        String gender = dragon.isMale()? "male" : "female";
        String moveTo = dragon.getNavigator().getPath() == null? "null" : dragon.getNavigator().getPath().getCurrentPos().toString();
        MovementController movement = dragon.getMoveHelper();
        String forceMoving = movement.getX() + " " + movement.getY() + " " + movement.getZ();

        drawCenteredString(font, dragon.getDisplayName().getUnformattedComponentText(), (width / 2), 15, 0xffffff);
        drawCenteredString(font, "isSleeping: " + dragon.isSleeping(), (width / 2) + 50, 50, 0xffffff);
        drawCenteredString(font, "isTamed: " + dragon.isTamed(), (width / 2) - 50, 50, 0xffffff);
        drawCenteredString(font, "isSitting: " + dragon.isSitting(), (width / 2) - 50, 75, 0xffffff);
        drawCenteredString(font, "isFlying: " + dragon.isFlying(), (width / 2) + 50, 75, 0xffffff);
        drawCenteredString(font, "variant: " + dragon.getVariant(), (width / 2) - 50, 100, 0xffffff);
        drawCenteredString(font, "gender: " + gender, (width / 2) + 50, 100, 0xffffff);
        drawCenteredString(font, "health: " + dragon.getHealth() + " / " + dragon.getMaxHealth(), (width / 2) - 50, 125, 0xffffff);
        drawCenteredString(font, "noAI: " + dragon.isAIDisabled(), (width / 2) + 50, 125, 0xffffff);
        drawCenteredString(font, "position: " + dragon.getPositionVec(), (width / 2), 150, 0xffffff);
        drawCenteredString(font, "motion: " + dragon.getMotion(), (width / 2), 175, 0xffffff);
        drawCenteredString(font, "moveTo position: " + moveTo, (width / 2), 200, 0xffffff);
        drawCenteredString(font, "Moving to: " + movement.getClass().getSimpleName() + ": " + forceMoving, (width / 2), 225, 0xffffff);
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
