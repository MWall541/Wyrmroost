package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import javax.annotation.Nullable;

/**
 * Helper class used to help register playerArmor items
 */
public class ItemArmorBase extends ArmorItem
{
    public ItemArmorBase(IArmorMaterial material, EquipmentSlotType equipType) { super(material, equipType, ModUtils.itemBuilder()); }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int layer = slot == EquipmentSlotType.LEGS ? 2 : 1;
        return Wyrmroost.MOD_ID + ":textures/models/armor/" + material.getName() + "_layer_" + layer + ".png";
    }
}
