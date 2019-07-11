package WolfShotz.Wyrmroost.content.entities.owdrake;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class OWDrakeRenderer extends MobRenderer<OWDrakeEntity, OWDrakeModel<OWDrakeEntity>>
{
    public OWDrakeRenderer(EntityRendererManager manager) {
        super(manager, new OWDrakeModel<>(), 1.6f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(OWDrakeEntity entity) {
        return null;
    }
}
