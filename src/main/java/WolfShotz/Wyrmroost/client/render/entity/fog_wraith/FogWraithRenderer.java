package WolfShotz.Wyrmroost.client.render.entity.fog_wraith;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.FogWraithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class FogWraithRenderer extends AbstractDragonRenderer<FogWraithEntity, FogWraithModel>
{
    public static final ResourceLocation TEXTURE = Wyrmroost.rl(BASE_PATH + "fog_wraith/body.png");

    public FogWraithRenderer(EntityRendererManager manager)
    {
        super(manager, new FogWraithModel(), 2);
    }

    @Override
    protected void preRenderCallback(FogWraithEntity entity, MatrixStack ms, float partialTicks)
    {
        super.preRenderCallback(entity, ms, partialTicks);
        ms.scale(1.2f, 1.2f, 1.2f);
    }

    @Override
    public ResourceLocation getEntityTexture(FogWraithEntity entity)
    {
        return TEXTURE;
    }
}
