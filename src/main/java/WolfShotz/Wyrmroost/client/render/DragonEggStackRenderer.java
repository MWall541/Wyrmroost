package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggModel;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class DragonEggStackRenderer extends ItemStackTileEntityRenderer
{
    private static final DragonEggModel EGG_MODEL = new DragonEggModel();
    private ResourceLocation texture;

    @Override
    public void render(ItemStack itemStackIn, MatrixStack ms, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        texture = getEggTexture(itemStackIn);
        ms.push();
        IVertexBuilder builder = ItemRenderer.getBuffer(bufferIn, EGG_MODEL.getRenderType(texture), false, itemStackIn.hasEffect());
        EGG_MODEL.render(ms, builder, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        ms.pop();
    }

    private ResourceLocation getEggTexture(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("dragonType"))
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) ModUtils.getTypeByString(tag.getString("dragonType")).create(ModUtils.getClientWorld());
            if (dragon != null) return dragon.getEggProperties().getEggTexture();
        }

        return Wyrmroost.rl("textures/entity/dragonegg/default.png");
    }
}
