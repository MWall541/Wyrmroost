package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.TranslationUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DebugScreen extends Screen
{
    public final AbstractDragonEntity dragon;
    
    public DebugScreen(AbstractDragonEntity dragon) {
        super(TranslationUtils.stringTranslation("debug_screen"));
        
        this.dragon = dragon;
    }
    
    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        renderBackground();
        String gender = dragon.getGender()? "male" : "female";
        
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
    public boolean isPauseScreen() { return true; }
}
