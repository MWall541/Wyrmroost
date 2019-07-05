package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class ItemFoodBase extends Item
{
    public ItemFoodBase(String name, Food builder) {
        super(new Item.Properties().food(builder).group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
