package WolfShotz.Wyrmroost.content.items.base;

import WolfShotz.Wyrmroost.registry.WRItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ToolMaterialList implements IItemTier
{
    GEODE(9.3f, 4f, 2164, 4, 25, WRItems.BLUE_GEODE.get()),
    GEODERED(10f, 5.5f, 3264, 5, 25, WRItems.RED_GEODE.get()),
    GEODEPURPLE(15f, 5.5f, 4214, 6, 28, WRItems.PURPLE_GEODE.get()),
    PLATINUM(5.5f, 2.5f, 645, 3, 20, WRItems.PLATINUM_INGOT.get());
    
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
    public int getMaxUses()
    {
        return durability;
    }
    
    @Override
    public float getEfficiency()
    {
        return efficiency;
    }
    
    @Override
    public float getAttackDamage()
    {
        return attackDamage;
    }
    
    @Override
    public int getHarvestLevel()
    {
        return harvestLevel;
    }
    
    @Override
    public int getEnchantability()
    {
        return enchantibility;
    }
    
    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(repairMaterial);
    }
}
