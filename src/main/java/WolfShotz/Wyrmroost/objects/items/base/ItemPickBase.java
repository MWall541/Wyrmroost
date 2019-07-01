package WolfShotz.Wyrmroost.objects.items.base;

import WolfShotz.Wyrmroost.objects.items.ItemList;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;

public class ItemPickBase extends PickaxeItem
{
    public ItemPickBase(String name, IItemTier tier) {
        super(tier, 2, -2.8f, new Item.Properties().group(ItemGroup.TOOLS));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
