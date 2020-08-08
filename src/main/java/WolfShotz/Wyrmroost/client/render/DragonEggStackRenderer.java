package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggRenderer;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class DragonEggStackRenderer extends ItemStackTileEntityRenderer
{
    @Override
    public void render(ItemStack itemStackIn, MatrixStack ms, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        IVertexBuilder builder = ItemRenderer.getBuffer(bufferIn, DragonEggRenderer.MODEL.getRenderType(getEggTexture(itemStackIn)), false, itemStackIn.hasEffect());
        DragonEggRenderer.MODEL.render(ms, builder, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
    }

    private ResourceLocation getEggTexture(ItemStack stack)
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
