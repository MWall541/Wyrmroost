package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CanariArmorItem extends ItemArmorBase
{
    public CanariArmorItem(EquipmentSlotType equipType)
    {
        super(ArmorMaterialList.CANARI, equipType);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> lines, ITooltipFlag p_77624_4_)
    {
        super.addInformation(p_77624_1_, p_77624_2_, lines, p_77624_4_);
        lines.add(new StringTextComponent(""));
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.canaridesc"));
    }
}
