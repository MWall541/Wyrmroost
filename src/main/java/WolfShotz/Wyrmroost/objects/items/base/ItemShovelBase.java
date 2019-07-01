package WolfShotz.Wyrmroost.objects.items.base;

import WolfShotz.Wyrmroost.objects.items.ItemList;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;

public class ItemShovelBase extends ShovelItem
{
    public ItemShovelBase(String name, IItemTier tier) {
        super(tier, 1.5f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
