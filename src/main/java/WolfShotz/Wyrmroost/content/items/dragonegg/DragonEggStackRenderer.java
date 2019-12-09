package WolfShotz.Wyrmroost.content.items.dragonegg;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggModel;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggProperties;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class DragonEggStackRenderer extends ItemStackTileEntityRenderer
{
    private static final DragonEggModel EGG_MODEL = new DragonEggModel();
    private static DragonEggProperties PROPERTIES;
    
    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if (PROPERTIES == null) PROPERTIES = getEggProperties(itemStackIn);
        
        GlStateManager.pushMatrix();
        GlStateManager.scalef(-1f, 1f, 1f);
        GlStateManager.scalef(PROPERTIES.getSize().width * 2.95f, -(PROPERTIES.getSize().height * 2), -(PROPERTIES.getSize().width * 2.95f));
        GlStateManager.color4f(1.5f, 1.5f, 1.5f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(PROPERTIES.getEggTexture());
        EGG_MODEL.base.render(0.0625f);
        GlStateManager.popMatrix();
    }
    
    private DragonEggProperties getEggProperties(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("dragonType")) {
            AbstractDragonEntity dragon = (AbstractDragonEntity) ModUtils.getTypeByString(tag.getString("dragonType")).create(ModUtils.getClientWorld());
            if (dragon != null) return dragon.getEggProperties();
        }
        
        return new DragonEggProperties(2f, 2f, 12000);
    }
}
