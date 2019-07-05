package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;

public class ItemShovelBase extends ShovelItem
{
    public ItemShovelBase(String name, IItemTier tier) {
        super(tier, 1.5f, -3.0f, new Item.Properties().group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
