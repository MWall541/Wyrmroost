package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;

public class ItemSwordBase extends SwordItem
{
    public ItemSwordBase(String name, int damage, IItemTier tier) {
        super(tier, damage, -2.4f, new Item.Properties().group(Wyrmroost.creativeTab));
        setRegistryName(name);

        ItemList.ITEMS.add(this);
    }
}
