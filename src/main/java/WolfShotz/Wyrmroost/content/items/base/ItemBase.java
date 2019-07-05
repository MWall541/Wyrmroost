package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


/**
 * ItemBase - Helper class allowing for easier item registration
 */
public class ItemBase extends Item
{
    public ItemBase(String name) {
        super(new Item.Properties().group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
