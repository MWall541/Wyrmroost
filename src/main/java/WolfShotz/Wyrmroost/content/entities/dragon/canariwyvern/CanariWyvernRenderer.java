package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class CanariWyvernRenderer extends AbstractDragonRenderer<CanariWyvernEntity>
{
    private static final ResourceLocation FEMALE_BLUE   = resource("body_fb.png");
    private static final ResourceLocation FEMALE_GREEN  = resource("body_fg.png");
    private static final ResourceLocation FEMALE_PURPLE = resource( "body_fp.png");
    private static final ResourceLocation FEMALE_RED    = resource("body_fr.png");
    private static final ResourceLocation FEMALE_YELLOW = resource("body_fy.png");
    private static final ResourceLocation MALE_BLUE     = resource("body_mb.png");
    private static final ResourceLocation MALE_GREEN    = resource("body_mg.png");
    private static final ResourceLocation MALE_PURPLE   = resource("body_mp.png");
    private static final ResourceLocation MALE_RED      = resource("body_mr.png");
    private static final ResourceLocation MALE_YELLOW   = resource("body_my.png");
    // Easter egg
    private static final ResourceLocation FEMALE_LADY   = resource("lady.png");
    private static final ResourceLocation RUDY          = resource("rudy.png");
    
    public CanariWyvernRenderer(EntityRendererManager manager) {
        super(manager, new CanariWyvernModel(), 1);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(CanariWyvernEntity canari) {
        if (canari.hasCustomName()) {
            String name = canari.getCustomName().getUnformattedComponentText();
            if (name.equalsIgnoreCase("rudy")) return RUDY;
            if (name.equalsIgnoreCase("Lady Everlyn Winklestein") && !canari.getGender()) return FEMALE_LADY;
        }
        
        boolean male = canari.getGender();
        switch (canari.getVariant()) {
            case 0: return male? MALE_BLUE   : FEMALE_BLUE;
            case 1: return male? MALE_GREEN  : FEMALE_GREEN;
            case 2: return male? MALE_PURPLE : FEMALE_PURPLE;
            case 3: return male? MALE_RED    : FEMALE_RED;
            case 4: return male? MALE_YELLOW : FEMALE_YELLOW;
        }
        return MALE_BLUE; // Fallback but HOW THE FUCK?!
    }
    
    private static ResourceLocation resource(String png) {
        return ModUtils.resource(DEF_LOC + "canari/" + png);
    }
}
