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
    private final ResourceLocation childCom = ModUtils.location(loc + "child_com.png");
    private final ResourceLocation childSav = ModUtils.location(loc + "child_sav.png");
    private final ResourceLocation childAlb = ModUtils.location(loc + "child_alb.png");
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
        if (isChild) {
            if (isSavannah) return childSav;
            if (isAlbino) return childAlb;
            return childCom;
        }
        if (isSavannah) return gender? maleSav : femaleSav;
        if (isAlbino) return gender? maleAlb : femaleAlb;
        return gender? maleCom : femaleCom;
    }
}
