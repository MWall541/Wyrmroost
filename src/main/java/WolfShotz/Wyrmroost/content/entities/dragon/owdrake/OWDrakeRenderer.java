package WolfShotz.Wyrmroost.content.entities.dragon.owdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity>
{
    private final String loc = DEF_LOC + "owdrake/";
    private final ResourceLocation MALE_COM = ModUtils.location(loc + "male_com.png");
    private final ResourceLocation FEMALE_COM = ModUtils.location(loc + "female_com.png");
    private final ResourceLocation MALE_SAV = ModUtils.location(loc + "male_sav.png");
    private final ResourceLocation FEMALE_SAV = ModUtils.location(loc + "female_sav.png");
    private final ResourceLocation MALE_SPE = ModUtils.location(loc + "male_spe.png");
    private final ResourceLocation FEMALE_SPE = ModUtils.location(loc + "female_spe.png");
    private final ResourceLocation CHILD_COM = ModUtils.location(loc + "child_com.png");
    private final ResourceLocation CHILD_SAV = ModUtils.location(loc + "child_sav.png");
    private final ResourceLocation CHILD_SPE = ModUtils.location(loc + "child_spe.png");
    // Easter Egg
    private final ResourceLocation DAISY = ModUtils.location(loc + "dasy.png");
    // Saddle
    private final ResourceLocation SADDLE_LAYER = ModUtils.location(loc + "saddle.png");

    public OWDrakeRenderer(EntityRendererManager manager) {
        super(manager, new OWDrakeModel(), 1.6f);
        addLayer(new ConditionalLayer(this, SADDLE_LAYER, OWDrakeEntity::isSaddled));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OWDrakeEntity drake) {
        if (drake.hasCustomName() && drake.getCustomName().getUnformattedComponentText().equals("Daisy")) return DAISY;
        return getDrakeTexture(drake.getGender(), drake.getVariant(), drake.isSpecial(), drake.isChild());
    }

    private ResourceLocation getDrakeTexture(boolean gender, boolean isSavannah, boolean isSpecial, boolean isChild) {
        if (isChild) {
            if (isSavannah) return CHILD_SAV;
            if (isSpecial) return CHILD_SPE;
            return CHILD_COM;
        }
        if (isSavannah) return gender? MALE_SAV : FEMALE_SAV;
        if (isSpecial) return gender? MALE_SPE : FEMALE_SPE;
        return gender? MALE_COM : FEMALE_COM;
    }
}
