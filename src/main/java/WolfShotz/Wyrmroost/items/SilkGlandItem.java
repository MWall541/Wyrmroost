package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class SilkGlandItem extends Item
{
    public SilkGlandItem()
    {
        super(WRItems.builder().maxStackSize(1));
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }
}
