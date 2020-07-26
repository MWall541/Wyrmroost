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
    public static final ResourceLocation[] MALE_TEXTURES = new ResourceLocation[6]; // includes glow
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
        if (itsChristmasOMG)
        {
            addLayer(new ConditionalLayer(c -> true, d -> RenderType.getEntityCutoutNoCull(XMAS_LAYER)));
            addLayer(new GlowLayer(sg -> XMAS_GLOW));
        }
        addLayer(new ConditionalLayer(AbstractDragonEntity::isSleeping, d -> RenderType.getEntityCutoutNoCull(SLEEP)));
    }

    public static ResourceLocation resource(String png) { return Wyrmroost.rl(BASE_PATH + "silver_glider/" + png); }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(SilverGliderEntity sg)
    {
        if (sg.isSpecial()) return BODY_SPE;
        if (!sg.isMale()) return FEMALE;
        int index = sg.getVariant();
        if (MALE_TEXTURES[index] == null) return MALE_TEXTURES[index] = resource("male_" + index + ".png");
        return MALE_TEXTURES[index];
    }

    private ResourceLocation getGlowTexture(SilverGliderEntity sg)
    {
        if (sg.isSpecial()) return BODY_SPE_GLOW;
        if (!sg.isMale()) return FEMALE_GLOW;
        int index = sg.getVariant() + 3;
        if (MALE_TEXTURES[index] == null) return MALE_TEXTURES[index] = resource("male_" + index + "_glow.png");
        return MALE_TEXTURES[index];
    }
}
