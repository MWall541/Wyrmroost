package WolfShotz.Wyrmroost.client.render.entity.orbwyrm;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.OrbwyrmEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class OrbwyrmRenderer extends AbstractDragonRenderer<OrbwyrmEntity, OrbwyrmModel>
{
    public static final ResourceLocation TEXTURE = Wyrmroost.rl(BASE_PATH + "orbwyrm/body.png");

    public OrbwyrmRenderer(EntityRendererManager manager)
    {
        super(manager, new OrbwyrmModel(), 2);
    }

    @Override
    protected void preRenderCallback(OrbwyrmEntity entity, MatrixStack ms, float partialTicks)
    {
        super.preRenderCallback(entity, ms, partialTicks);
        ms.scale(2.5f, 2.5f, 2.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(OrbwyrmEntity entity)
    {
        return TEXTURE;
    }
}
