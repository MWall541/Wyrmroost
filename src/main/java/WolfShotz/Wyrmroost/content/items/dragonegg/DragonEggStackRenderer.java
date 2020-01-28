package WolfShotz.Wyrmroost.content.items.dragonegg;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggModel;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class DragonEggStackRenderer extends ItemStackTileEntityRenderer
{
    private static final DragonEggModel EGG_MODEL = new DragonEggModel();
    private ResourceLocation texture;
    
    @Override
    public void renderByItem(ItemStack itemStackIn)
    {
        if (texture == null) texture = getEggTexture(itemStackIn); // get texture lazily
        
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1.5f, 1.5f, 1.5f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        EGG_MODEL.base.render(0.0625f);
        GlStateManager.popMatrix();
    }
    
    private ResourceLocation getEggTexture(ItemStack stack)
    {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("dragonType"))
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) ModUtils.getTypeByString(tag.getString("dragonType")).create(ModUtils.getClientWorld());
            if (dragon != null) return dragon.getEggProperties().getEggTexture();
        }
        
        return ModUtils.resource("textures/entity/dragonegg/default.png");
    }
}
