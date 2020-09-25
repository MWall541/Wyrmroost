package WolfShotz.Wyrmroost.items;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class DragonArmorItem extends Item
{
    public static final UUID ARMOR_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    private final int dmgReduction, enchantability;

    public DragonArmorItem(int dmgReduction, int enchantability)
    {
        super(WRItems.builder().maxStackSize(1));
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

    public double getDmgReduction() { return dmgReduction; }

    public static double getDmgReduction(ItemStack stack)
    {
        Item item = stack.getItem();
        if (!(item instanceof DragonArmorItem))
            throw new AssertionError("uhh this isn't a an armor: " + item.getRegistryName().toString());

        DragonArmorItem armor = (DragonArmorItem) item;
        double reduction = armor.dmgReduction;
        double multiplier = EnchantmentHelper.getEnchantments(stack).getOrDefault(Enchantments.PROTECTION, 0) * 1.25;
        return MathHelper.clamp(reduction * multiplier, reduction, 55d);
    }
}
