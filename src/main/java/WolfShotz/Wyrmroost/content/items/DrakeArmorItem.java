package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class DrakeArmorItem extends ItemArmorBase
{
    private static final UUID WEAPON_KNOCKBACK_MODIFER = UUID.fromString("bc5d758a-f8ba-4859-8bb3-503209cbbce8");

    public DrakeArmorItem(EquipmentSlotType equipType)
    {
        super(ArmorMaterialList.DRAKE, equipType);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lines, ITooltipFlag p_77624_4_)
    {
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.set", new StringTextComponent("Drake").applyTextStyle(TextFormatting.DARK_GREEN)));
        lines.add(new StringTextComponent(""));
        lines.add(new TranslationTextComponent("item.wyrmroost.armors.drakedesc"));
    }
}
