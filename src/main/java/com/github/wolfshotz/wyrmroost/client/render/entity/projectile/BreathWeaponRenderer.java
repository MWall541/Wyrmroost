package com.github.wolfshotz.wyrmroost.client.render.entity.projectile;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.projectile.DragonProjectileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class BreathWeaponRenderer extends EntityRenderer<DragonProjectileEntity>
{
    public static final ResourceLocation RR_BREATH_0 = Wyrmroost.rl("entity/projectiles/rr_breath/blue_fire_0");
    public static final ResourceLocation RR_BREATH_1 = Wyrmroost.rl("entity/projectiles/rr_breath/blue_fire_1");

    public BreathWeaponRenderer(EntityRendererManager renderManager) { super(renderManager); }

    @Override
    public void render(DragonProjectileEntity entity, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer typeBuffer, int packedLine)
    {
        if (entity.isBurning())
        {
            renderFire(ms, typeBuffer, entity);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(DragonProjectileEntity entity) { return null; }

    private void renderFire(MatrixStack ms, IRenderTypeBuffer typeBuffer, Entity entity)
    {
        Function<ResourceLocation, TextureAtlasSprite> func = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite fireSprite1 = func.apply(RR_BREATH_0);
        TextureAtlasSprite fireSprite2 = func.apply(RR_BREATH_1);
        ms.push();
        float width = entity.getWidth() * 1.4F;
        ms.scale(width, width, width);
        float x = 0.5F;
        float height = entity.getHeight() / width;
        float y = 0.0F;
        ms.rotate(renderManager.getCameraOrientation());
        ms.translate(0, 0, (-0.3f + (float) ((int) height) * 0.02f));
        float z = 0;
        IVertexBuilder vertex = typeBuffer.getBuffer(Atlases.getCutoutBlockType());

        for (MatrixStack.Entry msEntry = ms.getLast(); height > 0f;)
        {
            float minU = fireSprite1.getMinU();
            float minV = fireSprite1.getMinV();
            float maxU = fireSprite1.getMaxU();
            float maxV = fireSprite1.getMaxV();

            vertex(msEntry, vertex, x, -y, z, maxU, maxV);
            vertex(msEntry, vertex, -x, -y, z, minU, maxV);
            vertex(msEntry, vertex, -x, 1.4f - y, z, minU, minV);
            vertex(msEntry, vertex, x, 1.4f - y, z, maxU, minV);
            height -= 0.45f;
            y -= 0.45f;
            x *= 0.9f;
            z += 0.03f;
        }

        ms.pop();
    }

    private static void vertex(MatrixStack.Entry msEntry, IVertexBuilder bufferIn, float x, float y, float z, float texU, float texV)
    {
        bufferIn.pos(msEntry.getMatrix(), x, y, z).color(255, 255, 255, 255).tex(texU, texV).overlay(0, 10).lightmap(240).normal(msEntry.getNormal(), 0.0F, 1.0F, 0.0F).endVertex();
    }
}
