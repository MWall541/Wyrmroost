package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity>
{
    public static final ResourceLocation FEMALE         = resource("female.png");
    public static final ResourceLocation FEMALE_GLOW    = resource("female_glow.png");
    public static final ResourceLocation BODY_SPE       = resource("spe.png");
    public static final ResourceLocation BODY_SPE_GLOW  = resource("spe_glow.png");
    public static final ResourceLocation XMAS_LAYER     = resource("body_christmas.png");
    public static final ResourceLocation XMAS_GLOW      = resource("body_christmas_glow.png");
    public static final ResourceLocation SLEEP          = resource("sleep.png");
    // Male
    public static final ResourceLocation MALE_0         = resource("male_0.png");
    public static final ResourceLocation MALE_1         = resource("male_1.png");
    public static final ResourceLocation MALE_2         = resource("male_2.png");
    public static final ResourceLocation MALE_0_GLOW    = resource("male_0_glow.png");
    public static final ResourceLocation MALE_1_GLOW    = resource("male_1_glow.png");
    public static final ResourceLocation MALE_2_GLOW    = resource("male_2_glow.png");
    
    public SilverGliderRenderer(EntityRendererManager manager) {
        super(manager, new SilverGliderModel(), 1f);
        
        addLayer(new GlowLayer(this::getGlowTexture));
        if (isChristmas) addLayer(new ConditionalLayer(XMAS_LAYER, c -> true));
        if (isChristmas) addLayer(new GlowLayer(sg -> XMAS_GLOW));
        addLayer(new SleepLayer(SLEEP));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SilverGliderEntity sg) {
        if (sg.isSpecial()) return BODY_SPE;
        if (!sg.getGender()) return FEMALE;
        switch (sg.getVariant()) {
            case 0:  return MALE_0;
            case 1:  return MALE_1;
            case 2:  return MALE_2;
            default: return FEMALE; // AW GOD WTF IS THIS
        }
    }
    
    private ResourceLocation getGlowTexture(SilverGliderEntity sg) {
        if (sg.isSpecial()) return BODY_SPE_GLOW;
        if (!sg.getGender()) return FEMALE_GLOW;
        
        switch (sg.getVariant()) {
            default:
            case 0: return MALE_0_GLOW;
            case 1: return MALE_1_GLOW;
            case 2: return MALE_2_GLOW;
        }
    }
    
    public static ResourceLocation resource(String png) { return ModUtils.resource(DEF_LOC + "silverglider/" + png); }
}
