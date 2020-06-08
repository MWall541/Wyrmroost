package WolfShotz.Wyrmroost.client.render.entity.dragon_egg;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    public static final ResourceLocation DEFAULT_TEXTURE = Wyrmroost.rl("textures/entity/dragonegg/dragon_egg.png");
    public static final DragonEggModel MODEL = new DragonEggModel();

    public DragonEggRenderer(EntityRendererManager manager) { super(manager); }

    @Override
    public void render(DragonEggEntity entityIn, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn)
    {
        ms.push();
        setScale(entityIn, ms);
        ms.translate(0, -1.5, 0);
        MODEL.animate(entityIn);
        IVertexBuilder builder = buffer.getBuffer(MODEL.getRenderType(getEntityTexture(entityIn)));
        MODEL.render(ms, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        ms.pop();

        super.render(entityIn, entityYaw, partialTicks, ms, buffer, packedLightIn);
    }

    @Override
    protected boolean canRenderName(DragonEggEntity entity) { return false; }

    @Override
    public ResourceLocation getEntityTexture(DragonEggEntity entity) { return getDragonEggTexture(entity.getType()); }

    public static ResourceLocation getDragonEggTexture(EntityType<?> type)
    {
        ResourceLocation textureLoc = Wyrmroost.rl(String.format("textures/entity/dragon/%s/egg.png", type.getRegistryName().getPath().replace("wyrmroost:", "")));
        if (Minecraft.getInstance().getResourceManager().hasResource(textureLoc)) return textureLoc;
        return DEFAULT_TEXTURE;
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
