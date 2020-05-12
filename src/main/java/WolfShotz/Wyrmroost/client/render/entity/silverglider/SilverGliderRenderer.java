package WolfShotz.Wyrmroost.client.render.entity.silverglider;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.dragon.SilverGliderEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity, SilverGliderModel>
{
    // Constant textures
    public static final ResourceLocation FEMALE = resource("female.png");
    public static final ResourceLocation FEMALE_GLOW = resource("female_glow.png");
    public static final ResourceLocation BODY_SPE = resource("spe.png");
    public static final ResourceLocation BODY_SPE_GLOW = resource("spe_glow.png");
    public static final ResourceLocation XMAS_LAYER = resource("body_christmas.png");
    public static final ResourceLocation XMAS_GLOW = resource("body_christmas_glow.png");
    public static final ResourceLocation SLEEP = resource("sleep.png");

    public SilverGliderRenderer(EntityRendererManager manager)
    {
        super(manager, new SilverGliderModel(), 1f);

        addLayer(new GlowLayer(this::getGlowTexture));
        if (isChristmas)
        {
            addLayer(new ConditionalLayer(c -> true, d -> RenderType.getEntityCutoutNoCull(XMAS_LAYER)));
            addLayer(new GlowLayer(sg -> XMAS_GLOW));
        }
        addLayer(new ConditionalLayer(AbstractDragonEntity::isSleeping, d -> RenderType.getEntityCutoutNoCull(SLEEP)));
    }

    public static ResourceLocation resource(String png) { return Wyrmroost.rl(BASE_PATH + "silverglider/" + png); }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(SilverGliderEntity sg)
    {
        if (sg.isSpecial()) return BODY_SPE;
        if (!sg.isMale()) return FEMALE;
        return resource("male_" + sg.getVariant() + ".png");
    }

    private ResourceLocation getGlowTexture(SilverGliderEntity sg)
    {
        if (sg.isSpecial()) return BODY_SPE_GLOW;
        if (!sg.isMale()) return FEMALE_GLOW;
        return resource("male_" + sg.getVariant() + "_glow.png");
    }
}
