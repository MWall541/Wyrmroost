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
    private final ResourceLocation femaleCom = ModUtils.location(loc + "female_com.png");
    private final ResourceLocation maleSav = ModUtils.location(loc + "male_sav.png");
    private final ResourceLocation femaleSav = ModUtils.location(loc + "female_sav.png");
    private final ResourceLocation maleAlb = ModUtils.location(loc + "male_alb.png");
    private final ResourceLocation femaleAlb = ModUtils.location(loc + "female_alb.png");
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
        return getDrakeTexture(drake.getGender(), drake.getVariant(), drake.isAlbino());
    }

    private ResourceLocation getDrakeTexture(boolean gender, boolean isSavannah, boolean isAlbino) {
        if (isAlbino) return gender ? maleAlb : femaleAlb;
        if (isSavannah) return gender ? maleSav : femaleSav;
        return gender ? maleCom : femaleCom;
    }

}
