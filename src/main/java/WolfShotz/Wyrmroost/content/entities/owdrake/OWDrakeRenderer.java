package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends AbstractDragonRenderer<OWDrakeEntity, OWDrakeModel<OWDrakeEntity>>
{
    private String loc = DEF_LOC + "owdrake/";
    private ResourceLocation maleCom = ModUtils.location(loc + "male_com.png");
    private ResourceLocation femaleCom = ModUtils.location(loc + "female_com.png");
    private ResourceLocation maleSav = ModUtils.location(loc + "male_sav.png");
    private ResourceLocation femaleSav = ModUtils.location(loc + "female_sav.png");
    private ResourceLocation maleAlb = ModUtils.location(loc + "male_alb.png");
    private ResourceLocation femaleAlb = ModUtils.location(loc + "female_alb.png");
    // Easter Egg
    private ResourceLocation daisy = ModUtils.location(loc + "dasy.png");

    public OWDrakeRenderer(EntityRendererManager manager) { super(manager, new OWDrakeModel<>(), 1.6f); }

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
