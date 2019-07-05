package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;

public class ItemPickBase extends PickaxeItem
{
    public ItemPickBase(String name, IItemTier tier) {
        super(tier, 2, -2.8f, new Item.Properties().group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
