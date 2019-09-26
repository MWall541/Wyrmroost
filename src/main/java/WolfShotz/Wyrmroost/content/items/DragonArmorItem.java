package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.item.Item;

public class DragonArmorItem extends Item
{
    private int enchantability;
    
    public DragonArmorItem(String name, int enchantabilityIn) {
        super(ModUtils.itemBuilder().maxStackSize(1));
        setRegistryName(name);
        this.enchantability = enchantabilityIn;
    }
    
    @Override
    public int getItemEnchantability() { return enchantability; }
}
