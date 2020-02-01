package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DrakeArmorItem extends ItemArmorBase
{
    public DrakeArmorItem(EquipmentSlotType equipType)
    {
        super(ArmorMaterialList.DRAKE, equipType);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipSlot)
    {
        Multimap<String, AttributeModifier> map = super.getAttributeModifiers(equipSlot);
        return map;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lines, ITooltipFlag p_77624_4_)
    {
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.set", new StringTextComponent("Drake").applyTextStyle(TextFormatting.DARK_GREEN)));
        lines.add(new StringTextComponent(""));
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.drakedesc"));
    }
}
