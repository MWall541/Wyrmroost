package WolfShotz.Wyrmroost.content.entities.sliverglider;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity>
{
    private final String loc = DEF_LOC + "silverglider/";
    private final ResourceLocation TEXTURE = ModUtils.location(loc + "body.png");

    public SilverGliderRenderer(EntityRendererManager manager) {
        super(manager, new SilverGliderModel(), 2f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SilverGliderEntity entity) { return TEXTURE; }
}
