package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.client.render.entity.DragonEggRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggEntity;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class DragonEggStackRenderer extends ItemStackTileEntityRenderer
{
    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack ms, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        IVertexBuilder builder = ItemRenderer.getFoilBuffer(buffer, DragonEggRenderer.MODEL.renderType(getEggTexture(stack)), false, stack.hasFoil());
        DragonEggRenderer.MODEL.renderToBuffer(ms, builder, combinedLight, combinedOverlay, 1, 1, 1, 1);
    }

    private static ResourceLocation getEggTexture(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(DragonEggEntity.DATA_DRAGON_TYPE))
        {
            EntityType<?> type = ModUtils.getEntityTypeByKey(tag.getString(DragonEggEntity.DATA_DRAGON_TYPE));
            if (type != null) return DragonEggRenderer.getDragonEggTexture(type);
        }

        return DragonEggRenderer.DEFAULT_TEXTURE;
    }
}
