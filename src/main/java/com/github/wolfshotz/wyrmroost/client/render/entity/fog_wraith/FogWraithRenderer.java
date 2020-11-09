package com.github.wolfshotz.wyrmroost.client.render.entity.fog_wraith;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.entity.AbstractDragonRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.FogWraithEntity;
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
//
//    @Nullable
//    @Override
//    protected RenderType func_230496_a_(FogWraithEntity entity, boolean isInvisible, boolean visibleOnClient, boolean glowing)
//    {
//        if (entity.stealthTimer.get() > 0) return RenderHelper.getEntityTranslucent(TEXTURE);
//
//        return super.func_230496_a_(entity, isInvisible, visibleOnClient, glowing);
//    }

    @Override
    protected void applyRotations(FogWraithEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(FogWraithEntity entity)
    {
        return TEXTURE;
    }
}
