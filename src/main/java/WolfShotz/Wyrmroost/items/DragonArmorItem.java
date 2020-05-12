package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class DragonArmorItem extends Item
{
    public static final UUID ARMOR_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    private final int dmgReduction, enchantability;

    public DragonArmorItem(int dmgReduction, int enchantability)
    {
        super(ModUtils.itemBuilder().maxStackSize(1));
        this.dmgReduction = dmgReduction;
        this.enchantability = enchantability;
    }

    @Override
    public int getItemEnchantability() { return enchantability; }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.PROTECTION;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

    public int getDmgReduction() { return dmgReduction; }
}
