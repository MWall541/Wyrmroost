package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.Resource;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity>
{
    private final String loc = DEF_LOC + "owdrake/";
    private final ResourceLocation maleCom = ModUtils.location(loc + "male_com.png");
    private final ResourceLocation femaleCom = ModUtils.location(loc + "female_com");
    private final ResourceLocation maleComCld = ModUtils.location(loc + "male_com_cld");
    private final ResourceLocation femaleComCld = ModUtils.location(loc + "female_com_cld");
    private final ResourceLocation maleSav = ModUtils.location(loc + "male_sav.png");
    private final ResourceLocation femaleSav = ModUtils.location(loc + "female_sav.png");
    private final ResourceLocation maleSavCld = ModUtils.location(loc + "male_sav_cld");
    private final ResourceLocation femaleSavCld = ModUtils.location(loc + "female_sav_cld");
    private final ResourceLocation maleAlb = ModUtils.location(loc + "male_alb.png");
    private final ResourceLocation femaleAlb = ModUtils.location(loc + "female_alb");
    private final ResourceLocation maleAlbCld = ModUtils.location(loc + "male_alb_cld");
    private final ResourceLocation femaleAlbCld = ModUtils.location(loc + "female_alb_cld");
    // Saddle
    private final ResourceLocation saddle = ModUtils.location(loc + "saddle.png");
    // Easter Egg
    private final ResourceLocation daisy = ModUtils.location(loc + "dasy.png");

    public OWDrakeRenderer(EntityRendererManager manager) {
        super(manager, new OWDrakeModel(), 1.6f);
        addLayer(new SaddleLayer(this, saddle));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OWDrakeEntity drake) {
        if (drake.hasCustomName() && drake.getCustomName().getUnformattedComponentText().equals("Daisy")) return daisy;
        return getDrakeTexture(drake.getGender(), drake.getVariant(), drake.isAlbino(), drake.isChild());
    }

    private ResourceLocation getDrakeTexture(boolean gender, boolean isSavannah, boolean isAlbino, boolean isChild) {
        if (gender) {
            if (isAlbino) return isChild? maleAlbCld : maleAlb;
            if (isSavannah) return isChild? maleSavCld : maleSav;
        } else {
            if (isAlbino) return isChild? femaleAlbCld : femaleAlb;
            if (isSavannah) return isChild? femaleSavCld : femaleSav;
        }
        
        return maleCom; // FALLBACK
    }

}
