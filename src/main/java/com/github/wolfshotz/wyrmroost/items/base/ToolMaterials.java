package com.github.wolfshotz.wyrmroost.items.base;

import com.github.wolfshotz.wyrmroost.registry.WRItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum ToolMaterials implements IItemTier
{
    BLUE_GEODE(7.5f, 0.5f, 1732, 3, 20, WRItems.BLUE_GEODE),
    RED_GEODE(8.5f, 0.5f, 2031, 4, 20, WRItems.RED_GEODE),
    PURPLE_GEODE(10f, 0, 4214, 5, 25, WRItems.PURPLE_GEODE),
    PLATINUM(6.5f, 0, 425, 2, 18, WRItems.PLATINUM_INGOT);

    private final float efficiency, attackDamage;
    private final int durability, harvestLevel, enchantibility;
    private final Lazy<Ingredient> repairMaterial;

    ToolMaterials(float efficiency, float attackDmgMod, int durability, int harvestLevel, int enchantibility, Supplier<Item> repairMaterial)
    {
        this.efficiency = efficiency;
        this.attackDamage = attackDmgMod;
        this.durability = durability;
        this.harvestLevel = harvestLevel;
        this.enchantibility = enchantibility;
        this.repairMaterial = Lazy.of(() -> Ingredient.of(repairMaterial.get()));
    }

    @Override
    public int getUses()
    {
        return durability;
    }

    @Override
    public float getSpeed()
    {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return attackDamage;
    }

    @Override
    public int getLevel()
    {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue()
    {
        return enchantibility;
    }

    @Override
    public Ingredient getRepairIngredient()
    {
        return repairMaterial.get();
    }
}
