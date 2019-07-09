package WolfShotz.Wyrmroost.content.items;

import WolfShotz.Wyrmroost.content.entities.EntitySetup;
import WolfShotz.Wyrmroost.content.items.base.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ItemList
{
    /** Register all items in this list and initialize every value in SetupRegistryEvents */
    public static final List<Item> ITEMS = new ArrayList<>();

    // Geode start
    public static final Item itemgeode = new ItemBase("geode");

    public static final Item itemswordgeode = new ItemSwordBase("geode_sword", 4, ToolMaterialList.tool_geode);
    public static final Item itempickgeode = new ItemPickBase("geode_pick", ToolMaterialList.tool_geode);
    public static final Item itemshovelgeode = new ItemShovelBase("geode_shovel", ToolMaterialList.tool_geode);
    public static final Item itemaxegeode = new ItemAxeBase("geode_axe", ToolMaterialList.tool_geode);

    public static final Item itemgeodehelm = new ItemArmorBase("geode_helmet", ArmorMaterialList.armor_geode, EquipmentSlotType.HEAD);
    public static final Item itemgeodechest = new ItemArmorBase("geode_chestplate", ArmorMaterialList.armor_geode, EquipmentSlotType.CHEST);
    public static final Item itemgeodelegs = new ItemArmorBase("geode_legs", ArmorMaterialList.armor_geode, EquipmentSlotType.LEGS);
    public static final Item itemgeodeboots = new ItemArmorBase("geode_boots", ArmorMaterialList.armor_geode, EquipmentSlotType.FEET);
    // Geode end

    // Platinum start
    public static final Item itemplatinumingot = new ItemBase("platinum_ingot");

    public static final Item itemplatinumsword = new ItemSwordBase("platinum_sword", 3, ToolMaterialList.tool_platinum);
    public static final Item itemplatinumpick = new ItemPickBase("platinum_pick", ToolMaterialList.tool_platinum);
    public static final Item itemplatinumaxe = new ItemAxeBase("platinum_axe", ToolMaterialList.tool_platinum);
    public static final Item itemplatinumshovel = new ItemShovelBase("platinum_shovel", ToolMaterialList.tool_platinum);

    public static final Item itemplatinumhelm = new ItemArmorBase("platinum_helmet", ArmorMaterialList.armor_platinum, EquipmentSlotType.HEAD);
    public static final Item itemplatinumchest = new ItemArmorBase("platinum_chestplate", ArmorMaterialList.armor_platinum, EquipmentSlotType.CHEST);
    public static final Item itemplatinumlegs = new ItemArmorBase("platinum_leggings", ArmorMaterialList.armor_platinum, EquipmentSlotType.LEGS);
    public static final Item itemplatinumboots = new ItemArmorBase("platinum_boots", ArmorMaterialList.armor_platinum, EquipmentSlotType.FEET);
    // Platinum end

    // Spawn Eggs start
    public static final Item itemegg_drake = new ItemSpawnEggBase("drake_egg", EntitySetup.overworld_drake, 0x15ff00, 0x085e00);
    // Spawn Eggs end

    // Food start
    public static final Item itemfood_jewelledapple = new ItemFoodBase("jewelled_apple", FoodList.jewelledapple);
    public static final Item itemfood_dragonfruit = new ItemFoodBase("dragon_fruit", FoodList.dragonfruit);
    // Food end

    public static final Item itemmodbook = new ItemModBook("wyrmpedia");



//  ======== Material Lists ========

    /** Enum Handling the tool materials - Manages the mining speed, attck dmg, repair item etc. */
    protected enum ToolMaterialList implements IItemTier
    {
        tool_geode(9.3f, 4.0f, 2164, 4, 25, itemgeode),
        tool_platinum(5.5f, 2.5f, 645, 3, 20, itemplatinumingot);

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

    /** Enum Handling the armor materials - Manages dmg reduction, enchantability, durability etc. */
    protected enum ArmorMaterialList implements IArmorMaterial
    {
        armor_geode("geode", new int[] {4, 7, 9, 4}, 2.8f, 48, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, itemgeode),
        armor_platinum("platinum", new int[] {2, 5, 7, 2}, 0.2f, 20, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, itemplatinumingot);

        private int[] durabilityArray = new int[]{13, 15, 16, 11};
        private int durability, enchantability;
        private int[] dmgReduction;
        private float toughness;
        private String name;
        private SoundEvent sound;
        private Item repairMaterial;

        ArmorMaterialList(String name, int[] dmgReduction, float toughness, int durability, int enchantability, SoundEvent sound, Item repairMaterial) {
            this.durability = durability;
            this.dmgReduction = dmgReduction;
            this.enchantability = enchantability;
            this.toughness = toughness;
            this.name = name;
            this.sound = sound;
            this.repairMaterial = repairMaterial;
        }

        @Override
        public int getDurability(EquipmentSlotType slotIn) { return durabilityArray[slotIn.getIndex()] * this.durability; }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) { return dmgReduction[slotIn.getIndex()]; }

        @Override
        public int getEnchantability() { return enchantability; }

        @Override
        public SoundEvent getSoundEvent() { return sound; }

        @Override
        public Ingredient getRepairMaterial() { return Ingredient.fromItems(repairMaterial); }

        @Override
        @OnlyIn(Dist.CLIENT)
        public String getName() { return name; }

        @Override
        public float getToughness() { return toughness; }
    }

//  ================================

    /** Static class used to store the food items */
    private static class FoodList
    {
        // Jewelled Apple
        private static Food jewelledapple = new Food.Builder()
                                                    .hunger(8)
                                                    .saturation(0.8f)
                                                    .setAlwaysEdible()
                                                    .effect(new EffectInstance(Effects.GLOWING, 800), 1.0f)
                                                    .effect(new EffectInstance(Effects.REGENERATION, 100, 2), 1.0f)
                                                    .effect(new EffectInstance(Effects.RESISTANCE, 800), 1.0f)
                                                    .effect(new EffectInstance(Effects.ABSORPTION, 6000, 2), 1.0f)
                                                    .effect(new EffectInstance(Effects.NIGHT_VISION, 800), 1.0f)
                                                    .build();
        // Dragon Fruit
        private static Food dragonfruit = new Food.Builder()
                                                  .hunger(6)
                                                  .saturation(0.55f)
                                                  .build();
    }
}
