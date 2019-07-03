package WolfShotz.Wyrmroost.objects.items.base;

import WolfShotz.Wyrmroost.objects.items.ItemList;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;

public class ItemSpawnEggBase extends SpawnEggItem
{
    public ItemSpawnEggBase(String name, EntityType<?> type, int color1, int color2) {
        super(type, color1, color2, new Item.Properties().group(ItemGroup.MISC));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
