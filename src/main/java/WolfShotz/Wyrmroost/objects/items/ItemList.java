package WolfShotz.Wyrmroost.objects.items;

import WolfShotz.Wyrmroost.objects.items.base.*;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ItemList
{
    /** Register all items in this list and initialize every value in RegistryEvents */
    public static final List<Item> ITEMS = new ArrayList<>();

    // Geode
    public static final Item itemgeode = new ItemBase("geode", ItemGroup.MATERIALS);
    public static final Item itemswordgeode = new ItemSwordBase("geode_sword", 4, ToolMaterialList.geode);
    public static final Item itempickgeode = new ItemPickBase("geode_pick", ToolMaterialList.geode);
    public static final Item itemshovelgeode = new ItemShovelBase("geode_shovel", ToolMaterialList.geode);
    public static final Item itemaxegeode = new ItemAxeBase("geode_axe", ToolMaterialList.geode);



//  ======== Material Lists ========

    /** Enum Handling the tool materials - Manages the mining speed, attck dmg, repair item etc. */
    public enum ToolMaterialList implements IItemTier
    {
        geode(9.3f, 4.0f, 2164, 4, 20, itemgeode);

        private float efficiency, attackDamage;
        private int durability, harvestLevel, enchantibility;
        private Item repairMaterial;

        ToolMaterialList(float efficiency, float attackDamage, int durability, int harvestLevel, int enchantibility, Item repairMaterial) {
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

        @Override
        public float getAttackDamage() { return attackDamage; }

        @Override
        public int getHarvestLevel() { return harvestLevel; }

        @Override
        public int getEnchantability() { return enchantibility; }

        @Override
        public Ingredient getRepairMaterial() { return Ingredient.fromItems(repairMaterial); }
    }

}
