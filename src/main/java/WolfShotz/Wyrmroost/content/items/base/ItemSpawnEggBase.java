package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class ItemSpawnEggBase extends SpawnEggItem
{
    public ItemSpawnEggBase(String name, EntityType<?> type, int color1, int color2) {
        super(type, color1, color2, new Item.Properties().group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
