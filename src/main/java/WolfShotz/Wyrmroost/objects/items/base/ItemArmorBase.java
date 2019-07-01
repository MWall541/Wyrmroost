package WolfShotz.Wyrmroost.objects.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.objects.items.ItemList;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import javax.annotation.Nullable;

/**
 * Helper class used to help register playerArmor items
 */
public class ItemArmorBase extends ArmorItem
{
    public ItemArmorBase(String name, IArmorMaterial material, EquipmentSlotType equipType) {
        super(material, equipType, new Item.Properties().group(ItemGroup.COMBAT));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int layer = slot == EquipmentSlotType.LEGS ? 2 : 1;
        return Wyrmroost.modID + ":textures/models/armor/" + material.getName() + "_layer_" + layer + ".png";
    }
}
