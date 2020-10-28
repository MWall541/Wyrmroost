package WolfShotz.Wyrmroost.items.base;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ToolMaterials implements IItemTier
{
    BLUE_GEODE(7.5f, 4f, 1732, 4, 20, WRItems.BLUE_GEODE.get()),
    RED_GEODE(8.5f, 4.5f, 2031, 5, 20, WRItems.RED_GEODE.get()),
    PURPLE_GEODE(10f, 8f, 4214, 6, 25, WRItems.PURPLE_GEODE.get()),
    PLATINUM(6.5f, 3.25f, 425, 2, 18, WRItems.PLATINUM_INGOT.get());

    private final float efficiency;
    private final float attackDamage;
    private final int durability;
    private final int harvestLevel;
    private final int enchantibility;
    private final Item repairMaterial;

    ToolMaterials(float efficiency, float attackDamage, int durability, int harvestLevel, int enchantibility, Item repairMaterial)
    {
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.durability = durability;
        this.harvestLevel = harvestLevel;
        this.enchantibility = enchantibility;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getMaxUses() { return durability; }

    @Override
    public float getEfficiency() { return efficiency; }

    /**
     * This is the base attack damage. ToolItem Constructors take in an additional attack damage parameter
     * and is added to this value. Choose numbers wisely
     * In Wyrmroost, the base damage is the lowest this tool group can be (usually pickaxes)
     */
    @Override
    public float getAttackDamage() { return attackDamage; }

    @Override
    public int getHarvestLevel() { return harvestLevel; }

    @Override
    public int getEnchantability() { return enchantibility; }

    @Override
    public Ingredient getRepairMaterial() { return Ingredient.fromItems(repairMaterial); }
}
