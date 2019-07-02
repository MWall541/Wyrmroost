package WolfShotz.Wyrmroost.objects.items.base;

import WolfShotz.Wyrmroost.objects.items.ItemList;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemFoodBase extends Item
{
    public ItemFoodBase(String name, Food builder) {
        super(new Item.Properties().food(builder).group(ItemGroup.FOOD));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
