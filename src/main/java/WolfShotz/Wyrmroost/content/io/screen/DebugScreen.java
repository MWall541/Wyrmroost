package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
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
        if (dragon.getAnimations() != null && dragon.getAnimations().length > 0)
            for (int i = 0; i < dragon.getAnimations().length; i++)
            {
                Animation anim = dragon.getAnimations()[i];
                addButton(new Button((i * 50) + (width / 2) - (dragon.getAnimations().length * 25), 225, 50, 12, "Anim: " + i, b -> {
                    dragon.setAnimation(anim);
                    onClose();
                }));
            }
    }
    
    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        String gender = dragon.isMale()? "male" : "female";
        
        drawCenteredString(font, dragon.getDisplayName().getUnformattedComponentText(), (width / 2), 15, 0xffffff);
        drawCenteredString(font, "isSleeping: " + dragon.isSleeping(), (width / 2) + 50, 50, 0xffffff);
        drawCenteredString(font, "isTamed: " + dragon.isTamed(), (width / 2) - 50, 50, 0xffffff);
        drawCenteredString(font, "isSitting: " + dragon.isSitting(), (width / 2) - 50, 75, 0xffffff);
        drawCenteredString(font, "isFlying: " + dragon.isFlying(), (width / 2) + 50, 75, 0xffffff);
        drawCenteredString(font, "variant: " + dragon.getVariant(), (width / 2) - 50, 100, 0xffffff);
        drawCenteredString(font, "gender: " + gender, (width / 2) + 50, 100, 0xffffff);
        drawCenteredString(font, "health: " + dragon.getHealth() + " / " + dragon.getMaxHealth(), (width / 2) - 50, 125, 0xffffff);
        drawCenteredString(font, "position: " + dragon.getPositionVec(), (width / 2), 150, 0xffffff);
        drawCenteredString(font, "motion: " + dragon.getMotion(), (width / 2), 175, 0xffffff);
        drawCenteredString(font, "moveTo position: " + dragon.getNavigator().getPath(), (width / 2), 200, 0xffffff);
    }
    
    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}
