package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;

public class ItemAxeBase extends AxeItem
{
    public ItemAxeBase(String name, IItemTier tier) {
        super(tier, 2, -2.8f, new Item.Properties().group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
