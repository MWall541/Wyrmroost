package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity>
{
    private final String loc = DEF_LOC + "silverglider/";
    private final ResourceLocation FEMALE = ModUtils.location(loc + "female.png");
    // Male Variants: "male_{the variant int}.png"

    public SilverGliderRenderer(EntityRendererManager manager) {
        super(manager, new SilverGliderModel(), 1f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SilverGliderEntity entity) {
        if (!entity.getGender()) return FEMALE;
        return ModUtils.location(loc + "male_" + entity.getVariant() + ".png");
    }
}
