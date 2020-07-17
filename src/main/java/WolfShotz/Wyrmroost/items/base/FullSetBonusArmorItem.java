package WolfShotz.Wyrmroost.items.base;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class FullSetBonusArmorItem extends ItemArmorBase
{
    public FullSetBonusArmorItem(ArmorMaterialList material, EquipmentSlotType equipType)
    {
        super(material, equipType);
    }

    public abstract void applyFullSetBonus(LivingEntity entity, boolean hasFullSet);

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lines, ITooltipFlag flags)
    {
        super.addInformation(stack, world, lines, flags);
        lines.add(new StringTextComponent(""));
        lines.add(new TranslationTextComponent(String.format("item.wyrmroost.armors.%s.desc", material.getName().toLowerCase())));
    }

    public static boolean hasFullSet(LivingEntity entity)
    {
        Item prev = null;
        for (ItemStack itemStack : entity.getArmorInventoryList())
        {
            Item item = itemStack.getItem();
            if (item instanceof FullSetBonusArmorItem && (prev == null || item.getClass() == prev.getClass()))
                prev = item;
            else return false;
        }
        return true;
    }
}
