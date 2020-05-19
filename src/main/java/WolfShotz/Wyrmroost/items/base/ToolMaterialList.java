package WolfShotz.Wyrmroost.items.base;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ToolMaterialList implements IItemTier
{
    BLUE_GEODE(9.3f, 5f, 2164, 4, 20, WRItems.BLUE_GEODE.get()),
    RED_GEODE(10f, 6f, 3264, 5, 24, WRItems.RED_GEODE.get()),
    PURPLE_GEODE(15f, 8f, 4214, 6, 28, WRItems.PURPLE_GEODE.get()),
    PLATINUM(5.5f, 3.5f, 645, 3, 19, WRItems.PLATINUM_INGOT.get());

    private float efficiency, attackDamage;
    private int durability, harvestLevel, enchantibility;
    private Item repairMaterial;

    ToolMaterialList(float efficiency, float attackDamage, int durability, int harvestLevel, int enchantibility, Item repairMaterial)
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
