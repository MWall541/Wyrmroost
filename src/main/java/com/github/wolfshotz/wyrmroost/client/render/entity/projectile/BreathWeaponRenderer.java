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
    public static final ResourceLocation BLUE_FIRE = Wyrmroost.id("entity/projectiles/rr_breath/blue_fire");

    public BreathWeaponRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void render(DragonProjectileEntity entity, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer typeBuffer, int packedLine)
    {
        if (entity.isOnFire())
        {
            renderFire(ms, typeBuffer, entity);
        }
    }

    @Override
    public ResourceLocation getTexture(DragonProjectileEntity entity)
    {
        return null;
    }

    private void renderFire(MatrixStack ms, IRenderTypeBuffer typeBuffer, Entity entity)
    {
        Function<ResourceLocation, TextureAtlasSprite> func = Minecraft.getInstance().getSpriteAtlas(AtlasTexture.BLOCK_ATLAS_TEXTURE);
        TextureAtlasSprite fireSprite1 = func.apply(BLUE_FIRE);
        ms.push();
        float width = entity.getWidth() * 1.4F;
        ms.scale(width, width, width);
        float x = 0.5F;
        float height = entity.getHeight() / width;
        float y = 0.0F;
        ms.multiply(getRenderManager().getRotation());
        ms.translate(0, 0, (-0.3f + (float) ((int) height) * 0.02f));
        float z = 0;
        IVertexBuilder vertex = typeBuffer.getBuffer(Atlases.getEntityCutout());
        MatrixStack.Entry msEntry = ms.peek();

        for (int i = 0; height > 0; i++)
        {
            float minU = fireSprite1.getMinU();
            float minV = fireSprite1.getMinV();
            float maxU = fireSprite1.getMaxU();
            float maxV = fireSprite1.getMaxV();
            if (i / 2 % 2 == 0)
            {
                float prevMaxU = maxU;
                maxU = minU;
                minU = prevMaxU;
            }

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
        bufferIn.vertex(msEntry.getModel(), x, y, z).color(255, 255, 255, 255).texture(texU, texV).overlay(0, 10).light(240).normal(msEntry.getNormal(), 0.0F, 1.0F, 0.0F).next();
    }
}
