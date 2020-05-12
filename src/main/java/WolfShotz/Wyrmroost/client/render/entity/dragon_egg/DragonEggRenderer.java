package WolfShotz.Wyrmroost.client.render.entity.dragon_egg;

import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntitySize;
import net.minecraft.util.ResourceLocation;

public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    private final DragonEggModel EGG_MODEL = new DragonEggModel();

    public DragonEggRenderer(EntityRendererManager manager) { super(manager); }

    @Override
    public void render(DragonEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn)
    {
        ms.push();
        setScale(entityIn, ms);
        ms.translate(0, -1.5, 0);
        EGG_MODEL.animate(entityIn);
        IVertexBuilder builder = buffer.getBuffer(EGG_MODEL.getRenderType(getEntityTexture(entityIn)));
        EGG_MODEL.render(ms, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        ms.pop();

        super.render(entityIn, entityYaw, partialTicks, ms, buffer, packedLightIn);
    }

    @Override
    protected boolean canRenderName(DragonEggEntity entity) { return false; }

    @Override
    public ResourceLocation getEntityTexture(DragonEggEntity entity)
    {
        return entity.getProperties().getEggTexture();
    }

    /**
     * Render Custom egg sizes / shapes. <P>
     * If none is defined, then calculate the model size according to egg size
     */
    private void setScale(DragonEggEntity entity, MatrixStack ms)
    {
        EntitySize size = entity.getSize(entity.getPose());
        if (size == null) return;
        ms.scale(size.width * 2.95f, -(size.height * 2), -(size.width * 2.95f));
    }
}
