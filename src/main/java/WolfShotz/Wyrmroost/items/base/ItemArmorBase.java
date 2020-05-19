package WolfShotz.Wyrmroost.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Helper class used to help register playerArmor items
 */
public class ItemArmorBase extends ArmorItem
{
    public ItemArmorBase(ArmorMaterialList material, EquipmentSlotType equipType)
    {
        super(material, equipType, ModUtils.itemBuilder().rarity(material.getRarity()));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        int layer = slot == EquipmentSlotType.LEGS? 2 : 1;
        return Wyrmroost.MOD_ID + ":textures/models/armor/" + material.getName() + "_layer_" + layer + ".png";
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> lines, ITooltipFlag p_77624_4_)
    {
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.set", new TranslationTextComponent("item.wyrmroost.armors." + material.getName()).applyTextStyle(((ArmorMaterialList) material).getRarity().color)));
    }
}
