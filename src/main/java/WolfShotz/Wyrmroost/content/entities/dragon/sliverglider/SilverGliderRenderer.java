package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity>
{
    private static final String LOC = DEF_LOC + "silverglider/";
    private final ResourceLocation FEMALE = ModUtils.location(LOC + "female.png");
    private final ResourceLocation FEMALE_GLOW = ModUtils.location(LOC + "female_glow.png");
    private final ResourceLocation BODY_SPE = ModUtils.location(LOC + "spe.png");
    private final ResourceLocation BODY_SPE_GLOW = ModUtils.location(LOC + "spe_glow.png");
    private final ResourceLocation XMAS_LAYER = ModUtils.location(LOC + "body_christmas.png");
    private final ResourceLocation XMAS_GLOW = ModUtils.location(LOC + "body_christmas_glow.png");
    private final ResourceLocation SLEEP = ModUtils.location(LOC + "sleep.png");
    // Male Variants:
    // Body: "male_{the variant int}.png"
    // Glow: "male_{the variant int}_glow.png"
    
    public SilverGliderRenderer(EntityRendererManager manager) {
        super(manager, new SilverGliderModel(), 1f);
        
        Function<SilverGliderEntity, ResourceLocation> condition = sg -> {
            if (sg.isSpecial()) return BODY_SPE_GLOW;
            if (!sg.getGender()) return FEMALE_GLOW;
            return ModUtils.location(LOC + "male_" + sg.getVariant() + "_glow.png");
        };
        
        addLayer(new GlowLayer(this, condition));
        if (isChristmas) addLayer(new ConditionalLayer(this, XMAS_LAYER, c -> true));
        if (isChristmas) addLayer(new GlowLayer(this, sg -> XMAS_GLOW));
        addLayer(new SleepLayer(this, SLEEP));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SilverGliderEntity glider) {
        if (glider.isSpecial()) return BODY_SPE;
        if (!glider.getGender()) return FEMALE;
        return ModUtils.location(LOC + "male_" + glider.getVariant() + ".png");
    }
}
