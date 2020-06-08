package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggModel;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class DragonEggStackRenderer extends ItemStackTileEntityRenderer
{
    private static final ResourceLocation DEFAULT = Wyrmroost.rl("textures/entity/dragonegg/default.png");
    private static final DragonEggModel EGG_MODEL = new DragonEggModel();

    @Override
    public void render(ItemStack itemStackIn, MatrixStack ms, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        IVertexBuilder builder = ItemRenderer.getBuffer(bufferIn, EGG_MODEL.getRenderType(getEggTexture(itemStackIn)), false, itemStackIn.hasEffect());
        EGG_MODEL.render(ms, builder, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
    }

    private ResourceLocation getEggTexture(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(DragonEggEntity.DATA_DRAGON_TYPE))
        {
            EntityType<?> type = ModUtils.getTypeByString(tag.getString(DragonEggEntity.DATA_DRAGON_TYPE));
            if (type != null)
            {
                try
                {
                    ResourceLocation textureLoc = Wyrmroost.rl(String.format("textures/entity/dragon/%s/egg.png", type.getRegistryName().getPath().replace("wyrmroost:", "")));
                    Minecraft.getInstance().getResourceManager().getResource(textureLoc);
                    return textureLoc;
                }
                catch (IOException ignore) {}
            }
        }

        return DEFAULT;
    }
}
