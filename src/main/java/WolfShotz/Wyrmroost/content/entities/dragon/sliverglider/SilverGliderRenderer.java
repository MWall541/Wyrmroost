package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity>
{
    private final ResourceLocation FEMALE = location("female.png");
    private final ResourceLocation FEMALE_GLOW = location("female_glow.png");
    private final ResourceLocation BODY_SPE = location("spe.png");
    private final ResourceLocation BODY_SPE_GLOW = location("spe_glow.png");
    private final ResourceLocation XMAS_LAYER = location("body_christmas.png");
    private final ResourceLocation XMAS_GLOW = location("body_christmas_glow.png");
    private final ResourceLocation SLEEP = location("sleep.png");
    // Male Variants:
    // Body: "male_{the variant int}.png"
    // Glow: "male_{the variant int}_glow.png"
    
    private Function<SilverGliderEntity, ResourceLocation> condition = sg -> {
        if (sg.isSpecial()) return BODY_SPE_GLOW;
        if (!sg.getGender()) return FEMALE_GLOW;
        return location("male_" + sg.getVariant() + "_glow.png");
    };
    
    public SilverGliderRenderer(EntityRendererManager manager) {
        super(manager, new SilverGliderModel(), 1f);
        
        addLayer(new GlowLayer(condition));
        if (isChristmas) addLayer(new ConditionalLayer(XMAS_LAYER, c -> true));
        if (isChristmas) addLayer(new GlowLayer(sg -> XMAS_GLOW));
        addLayer(new SleepLayer(SLEEP));
    }
    
//        if (glider.isSpecial()) return BODY_SPE;
//        if (!glider.getGender()) return FEMALE;
//        return ModUtils.location(LOC + "male_" + glider.getVariant() + ".png");
    
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SilverGliderEntity glider) {
        if (glider.isSpecial()) return BODY_SPE;
        if (!glider.getGender()) return FEMALE;
        return location("male_" + glider.getVariant() + ".png");
    }
    
    @Override
    public String getResourceDirectory() { return DEF_LOC + "silverglider/"; }
}
