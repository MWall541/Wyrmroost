package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.eggblock.EggBlockItem;
import WolfShotz.Wyrmroost.content.items.MinutusItem;
import WolfShotz.Wyrmroost.content.items.ModBookItem;
import WolfShotz.Wyrmroost.content.items.SoulCrystalItem;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Wyrmroost.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupItems
{
    @ObjectHolder(Wyrmroost.modID + ":tarragon_tome")
    public static Item itemmodbook;


    @ObjectHolder(Wyrmroost.modID + ":jewelled_apple")
    public static Item itemfood_jewelledapple;

    @ObjectHolder(Wyrmroost.modID + ":dragon_fruit")
    public static Item itemfood_dragonfruit;

    @ObjectHolder(Wyrmroost.modID + ":minutus")
    public static Item itemminutus;

    @ObjectHolder(Wyrmroost.modID + ":cooked_minutus")
    public static Item itemfood_cookedminutus;
    
    @ObjectHolder(Wyrmroost.modID + ":egg")
    public static Item egg;

    @ObjectHolder(Wyrmroost.modID + ":soul_crystal")
    public static Item soulcrystal;
    
    // Geode start
    @ObjectHolder(Wyrmroost.modID + ":geode")
    public static Item itemgeode;


    @ObjectHolder(Wyrmroost.modID + ":geode_sword")
    public static Item itemswordgeode;

    @ObjectHolder(Wyrmroost.modID + ":geode_pick")
    public static Item itempickgeode;

    @ObjectHolder(Wyrmroost.modID + ":geode_shovel")
    public static Item itemshovelgeode;

    @ObjectHolder(Wyrmroost.modID + ":geode_axe")
    public static Item itemaxegeode;


    @ObjectHolder(Wyrmroost.modID + ":geode_helmet")
    public static Item itemgeodehelm;

    @ObjectHolder(Wyrmroost.modID + ":geode_chestplate")
    public static Item itemgeodechest;

    @ObjectHolder(Wyrmroost.modID + ":geode_leggings")
    public static Item itemgeodelegs;

    @ObjectHolder(Wyrmroost.modID + ":geode_boots")
    public static Item itemgeodeboots;
    // Geode end

    // Platinum start
    @ObjectHolder(Wyrmroost.modID + ":platinum_ingot")
    public static Item itemplatinumingot;


    @ObjectHolder(Wyrmroost.modID + ":platinum_sword")
    public static Item itemplatinumsword;

    @ObjectHolder(Wyrmroost.modID + ":platinum_pick")
    public static Item itemplatinumpick;

    @ObjectHolder(Wyrmroost.modID + ":platinum_axe")
    public static Item itemplatinumaxe;

    @ObjectHolder(Wyrmroost.modID + ":platinum_shovel")
    public static Item itemplatinumshovel;


    @ObjectHolder(Wyrmroost.modID + ":platinum_helmet")
    public static Item itemplatinumhelm;

    @ObjectHolder(Wyrmroost.modID + ":platinum_chestplate")
    public static Item itemplatinumchest;

    @ObjectHolder(Wyrmroost.modID + ":platinum_leggings")
    public static Item itemplatinumlegs;

    @ObjectHolder(Wyrmroost.modID + ":platinum_boots")
    public static Item itemplatinumboots;
    // Platinum end

    // Spawn Eggs start
    @ObjectHolder(Wyrmroost.modID + ":drake_egg")
    public static Item itemegg_drake;

    @ObjectHolder(Wyrmroost.modID + ":minutus_egg")
    public static Item itemegg_minutus;

    @ObjectHolder(Wyrmroost.modID + ":silverglider_egg")
    public static Item itemegg_silverglider;
    
    @ObjectHolder(Wyrmroost.modID + ":rooststalker_egg")
    public static Item itemegg_rooststalker;
    // Spawn Eggs end

    @SubscribeEvent
    public static void itemSetup(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll (
                new ModBookItem(),
                new MinutusItem(),
                new EggBlockItem(),
                new SoulCrystalItem(),

                new Item(ModUtils.itemBuilder().food(FoodList.jewelledapple)).setRegistryName("jewelled_apple"),
                new Item(ModUtils.itemBuilder().food(FoodList.dragonfruit)).setRegistryName("dragon_fruit"),
                new Item(ModUtils.itemBuilder().food(FoodList.cookedminutus)).setRegistryName("cooked_minutus"),

                // SpawnEggs start
                new SpawnEggItem(SetupEntities.overworld_drake, 0x15ff00, 0x085e00, ModUtils.itemBuilder()).setRegistryName("drake_egg"),
                new SpawnEggItem(SetupEntities.minutus, 0xfcc0ea, 0xfcd4f0, ModUtils.itemBuilder()).setRegistryName("minutus_egg"),
                new SpawnEggItem(SetupEntities.silver_glider, 0xffffff, 0xffffff, ModUtils.itemBuilder()).setRegistryName("silverglider_egg"),
                new SpawnEggItem(SetupEntities.roost_stalker, 0xffffff, 0xffffff, ModUtils.itemBuilder()).setRegistryName("rooststalker_egg"),
                // SpawnEggs end

                // Geode start
                new Item(ModUtils.itemBuilder()).setRegistryName("geode"),
                new SwordItem(ToolMaterialList.tool_geode, 4, -2.4f, ModUtils.itemBuilder()).setRegistryName("geode_sword"),
                new PickaxeItem(ToolMaterialList.tool_geode, 2, -2.8f, ModUtils.itemBuilder()).setRegistryName("geode_pick"),
                new AxeItem(ToolMaterialList.tool_geode, 2, -2.8f, ModUtils.itemBuilder()).setRegistryName("geode_axe"),
                new ShovelItem(ToolMaterialList.tool_geode, 1.5f, -3.0f, ModUtils.itemBuilder()).setRegistryName("geode_shovel"),
                new ItemArmorBase("geode_helmet", ArmorMaterialList.armor_geode, EquipmentSlotType.HEAD),
                new ItemArmorBase("geode_chestplate", ArmorMaterialList.armor_geode, EquipmentSlotType.CHEST),
                new ItemArmorBase("geode_leggings", ArmorMaterialList.armor_geode, EquipmentSlotType.LEGS),
                new ItemArmorBase("geode_boots", ArmorMaterialList.armor_geode, EquipmentSlotType.FEET),
                // Geode End

                // Platinum Start
                new Item(ModUtils.itemBuilder()).setRegistryName("platinum_ingot"),
                new SwordItem(ToolMaterialList.tool_platinum, 4, -2.4f, ModUtils.itemBuilder()).setRegistryName("platinum_sword"),
                new PickaxeItem(ToolMaterialList.tool_platinum, 2, -2.8f,ModUtils.itemBuilder()).setRegistryName("platinum_pick"),
                new AxeItem(ToolMaterialList.tool_platinum, 2, -2.8f,ModUtils.itemBuilder()).setRegistryName("platinum_axe"),
                new ShovelItem(ToolMaterialList.tool_platinum, 1.5f, -3.0f, ModUtils.itemBuilder()).setRegistryName("platinum_shovel"),
                new ItemArmorBase("platinum_helmet", ArmorMaterialList.armor_platinum, EquipmentSlotType.HEAD),
                new ItemArmorBase("platinum_chestplate", ArmorMaterialList.armor_platinum, EquipmentSlotType.CHEST),
                new ItemArmorBase("platinum_leggings", ArmorMaterialList.armor_platinum, EquipmentSlotType.LEGS),
                new ItemArmorBase("platinum_boots", ArmorMaterialList.armor_platinum, EquipmentSlotType.FEET)
                // Platinum End
        );
        // BlockItem's
        SetupBlocks.BLOCKS.forEach(block -> event.getRegistry().register(new BlockItem(block, ModUtils.itemBuilder()).setRegistryName(block.getRegistryName())));
    }

//  ===============================
//          Material Lists
//  ===============================

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
                                                    .saturation(0.9f)
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
        // Cooked Desertwyrm
        private static Food cookedminutus = new Food.Builder()
                                                  .hunger(6)
                                                  .saturation(0.7f)
                                                  .build();

    }
}
