package WolfShotz.Wyrmroost.objects.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


/**
 * ItemBase - Helper class allowing for easier item registration
 */
public class ItemBase extends Item
{
    public ItemBase(String name, ItemGroup group) {
        super(new Item.Properties().group(group));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
