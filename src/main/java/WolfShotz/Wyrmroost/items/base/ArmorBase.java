package WolfShotz.Wyrmroost.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Helper class used to help register playerArmor items
 */
public class ArmorBase extends ArmorItem
{
    public ArmorBase(ArmorMaterials material, EquipmentSlotType equipType)
    {
        super(material, equipType, WRItems.builder().rarity(material.getRarity()));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        int layer = slot == EquipmentSlotType.LEGS? 2 : 1;
        return Wyrmroost.MOD_ID + ":textures/models/armor/" + material.getName() + "_layer_" + layer + ".png";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lines, ITooltipFlag flags)
    {
        super.addInformation(stack, world, lines, flags);
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.set", new TranslationTextComponent("item.wyrmroost.armors." + material.getName()).applyTextStyle(((ArmorMaterials) material).getRarity().color)));
        lines.add(new StringTextComponent(""));
        lines.add(new TranslationTextComponent(String.format("item.wyrmroost.armors.%s.desc", material.getName().toLowerCase())));
    }

    public void applyFullSetBonus(LivingEntity entity, boolean hasFullSet) {}

    public static boolean hasFullSet(LivingEntity entity)
    {
        Item prev = null;
        for (ItemStack itemStack : entity.getArmorInventoryList())
        {
            Item item = itemStack.getItem();
            if (prev == null || item.getClass() == prev.getClass()) prev = item;
            else return false;
        }
        return true;
    }
}
