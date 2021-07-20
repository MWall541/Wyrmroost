package com.github.wolfshotz.wyrmroost.client.model.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WREntityModel;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public abstract class DragonEntityModel<T extends TameableDragonEntity> extends WREntityModel<T>
{
    public static final String FOLDER = "textures/entity/dragon/";

    public DragonEntityModel()
    {
    }

    public DragonEntityModel(Function<ResourceLocation, RenderType> type)
    {
        super(type);
    }

    @Override
    public void scale(T entity, MatrixStack ms, float partialTicks)
    {
        float scale = entity.getScale();
        ms.scale(scale, scale, scale);
    }

    public void renderEyes(ResourceLocation texture, MatrixStack ms, IRenderTypeBuffer buffer)
    {
        renderToBuffer(ms, buffer.getBuffer(RenderHelper.getAdditiveGlow(texture)), 15728640, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }

    public void renderArmorOverlay(MatrixStack ms, IRenderTypeBuffer buffer, int light)
    {
        if (entity.hasArmor())
            renderTexturedOverlay(getArmorTexture(entity), ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    private static ResourceLocation getArmorTexture(TameableDragonEntity entity)
    {
        String path = entity.getArmorStack().getItem().getRegistryName().getPath().replace("_dragon_armor", "");
        return Wyrmroost.id(String.format("%s%s/accessories/armor_%s.png", FOLDER, entity.getType().getRegistryName().getPath(), path));
    }
}
