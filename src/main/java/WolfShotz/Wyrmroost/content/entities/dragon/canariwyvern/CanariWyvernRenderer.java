package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class CanariWyvernRenderer extends AbstractDragonRenderer<CanariWyvernEntity>
{
    public static final ResourceLocation FEMALE_BLUE = resource("body_fb.png");
    public static final ResourceLocation FEMALE_GREEN = resource("body_fg.png");
    public static final ResourceLocation FEMALE_PURPLE = resource("body_fp.png");
    public static final ResourceLocation FEMALE_RED = resource("body_fr.png");
    public static final ResourceLocation FEMALE_YELLOW = resource("body_fy.png");
    public static final ResourceLocation MALE_BLUE = resource("body_mb.png");
    public static final ResourceLocation MALE_GREEN = resource("body_mg.png");
    public static final ResourceLocation MALE_PURPLE = resource("body_mp.png");
    public static final ResourceLocation MALE_RED = resource("body_mr.png");
    public static final ResourceLocation MALE_YELLOW = resource("body_my.png");
    // Easter egg
    private static final ResourceLocation FEMALE_LADY = resource("lady.png");
    private static final ResourceLocation RUDY = resource("rudy.png");
    
    private static final ResourceLocation[] MALE_VARS = new ResourceLocation[]{MALE_BLUE, MALE_GREEN, MALE_YELLOW, MALE_RED, MALE_PURPLE};
    private static final ResourceLocation[] FEMALE_VARS = new ResourceLocation[]{FEMALE_YELLOW, FEMALE_PURPLE, FEMALE_GREEN, FEMALE_BLUE, FEMALE_RED};
    
    public CanariWyvernRenderer(EntityRendererManager manager)
    {
        super(manager, new CanariWyvernModel(), 1);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(CanariWyvernEntity canari)
    {
        if (canari.hasCustomName())
        {
            String name = canari.getCustomName().getUnformattedComponentText();
            if (name.equals("Rudy")) return RUDY;
            if (name.equals("Lady Everlyn Winklestein") && !canari.getGender()) return FEMALE_LADY;
        }
        
        return canari.getGender()? MALE_VARS[canari.getVariant()] : FEMALE_VARS[canari.getVariant()];
    }
    
    private static ResourceLocation resource(String png)
    {
        return ModUtils.resource(DEF_LOC + "canari/" + png);
    }
}
