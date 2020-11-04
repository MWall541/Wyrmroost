package com.github.wolfshotz.wyrmroost.items.base;

import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ToolMaterials implements IItemTier
{
    BLUE_GEODE(7.5f, 0.5f, 1732, 3, 20, WRItems.BLUE_GEODE.get()),
    RED_GEODE(8.5f, 0.5f, 2031, 4, 20, WRItems.RED_GEODE.get()),
    PURPLE_GEODE(10f, 0, 4214, 5, 25, WRItems.PURPLE_GEODE.get()),
    PLATINUM(6.5f, 0, 425, 2, 18, WRItems.PLATINUM_INGOT.get());

    private final float efficiency, attackDamage;
    private final int durability, harvestLevel, enchantibility;
    private final Item repairMaterial;

    ToolMaterials(float efficiency, float attackDmgMod, int durability, int harvestLevel, int enchantibility, Item repairMaterial)
    {
        this.efficiency = efficiency;
        this.attackDamage = attackDmgMod;
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
     * Literally used for decimal additions to attack damage. It's fucking stupid but it's what has to be done.
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
