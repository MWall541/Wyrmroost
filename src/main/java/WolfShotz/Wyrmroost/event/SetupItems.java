package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.eggblock.EggBlockItem;
import WolfShotz.Wyrmroost.content.items.MinutusItem;
import WolfShotz.Wyrmroost.content.items.ModBookItem;
import WolfShotz.Wyrmroost.content.items.SoulCrystalItem;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
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

@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupItems
{
    @ObjectHolder(Wyrmroost.MOD_ID + ":tarragon_tome")
    public static Item modBook;


    @ObjectHolder(Wyrmroost.MOD_ID + ":jewelled_apple")
    public static Item foodJewelledApple;

    @ObjectHolder(Wyrmroost.MOD_ID + ":dragon_fruit")
    public static Item foodDragonFruit;

    @ObjectHolder(Wyrmroost.MOD_ID + ":minutus")
    public static Item minutus;

    @ObjectHolder(Wyrmroost.MOD_ID + ":cooked_minutus")
    public static Item foodCookedMinutus;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":egg")
    public static Item egg;

    @ObjectHolder(Wyrmroost.MOD_ID + ":soul_crystal")
    public static Item soulCrystal;
    
    // Geode start
    @ObjectHolder(Wyrmroost.MOD_ID + ":geode")
    public static Item geode;


    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_sword")
    public static Item swordGeode;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_pick")
    public static Item pickGeode;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_shovel")
    public static Item shovelGeode;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_axe")
    public static Item axeGeode;


    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_helmet")
    public static Item geodeHelm;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_chestplate")
    public static Item geodeChest;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_leggings")
    public static Item geodeLegs;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_boots")
    public static Item geodeBoots;
    // Geode end

    // Platinum start
    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_ingot")
    public static Item platinumIngot;


    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_sword")
    public static Item platinumSword;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_pick")
    public static Item platinumPick;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_axe")
    public static Item platinumAxe;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_shovel")
    public static Item platinumShovel;


    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_helmet")
    public static Item platinumHelm;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_chestplate")
    public static Item platinumChest;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_leggings")
    public static Item platinumLegs;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_boots")
    public static Item platinumBoots;
    // Platinum end

    // Spawn Eggs start
    @ObjectHolder(Wyrmroost.MOD_ID + ":drake_egg")
    public static Item eggDrake;

    @ObjectHolder(Wyrmroost.MOD_ID + ":minutus_egg")
    public static Item eggMinutus;

    @ObjectHolder(Wyrmroost.MOD_ID + ":silverglider_egg")
    public static Item eggSilverGlider;
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":rooststalker_egg")
    public static Item eggRoostStalker;
    // Spawn Eggs end

    @SubscribeEvent
    public static void itemSetup(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll (
                // Misc Start
                new ModBookItem(),
                new MinutusItem(),
                new EggBlockItem(),
                new SoulCrystalItem(),
                // Misc End

                // Food Start
                new Item(ModUtils.itemBuilder().food(jewelledapple)).setRegistryName("jewelled_apple"),
                new Item(ModUtils.itemBuilder().food(dragonfruit)).setRegistryName("dragon_fruit"),
                new Item(ModUtils.itemBuilder().food(cookedminutus)).setRegistryName("cooked_minutus"),
                // Food End
                
                // SpawnEggs start
                new SpawnEggItem(SetupEntities.overworld_drake, 0x15ff00, 0x085e00, ModUtils.itemBuilder()).setRegistryName("drake_egg"),
                new SpawnEggItem(SetupEntities.minutus, 0xfcc0ea, 0xfcd4f0, ModUtils.itemBuilder()).setRegistryName("minutus_egg"),
                new SpawnEggItem(SetupEntities.silver_glider, 0xffffff, 0xffffff, ModUtils.itemBuilder()).setRegistryName("silverglider_egg"),
                new SpawnEggItem(SetupEntities.roost_stalker, 0xffffff, 0xffffff, ModUtils.itemBuilder()).setRegistryName("rooststalker_egg"),
                // SpawnEggs end

                // Geode start
                new Item(ModUtils.itemBuilder()).setRegistryName("geode"),
                new SwordItem(ToolMaterialList.GEODE, 4, -2.4f, ModUtils.itemBuilder()).setRegistryName("geode_sword"),
                new PickaxeItem(ToolMaterialList.GEODE, 2, -2.8f, ModUtils.itemBuilder()).setRegistryName("geode_pick"),
                new AxeItem(ToolMaterialList.GEODE, 2, -2.8f, ModUtils.itemBuilder()).setRegistryName("geode_axe"),
                new ShovelItem(ToolMaterialList.GEODE, 1.5f, -3.0f, ModUtils.itemBuilder()).setRegistryName("geode_shovel"),
                new ItemArmorBase("geode_helmet", ArmorMaterialList.GEODE, EquipmentSlotType.HEAD),
                new ItemArmorBase("geode_chestplate", ArmorMaterialList.GEODE, EquipmentSlotType.CHEST),
                new ItemArmorBase("geode_leggings", ArmorMaterialList.GEODE, EquipmentSlotType.LEGS),
                new ItemArmorBase("geode_boots", ArmorMaterialList.GEODE, EquipmentSlotType.FEET),
                // Geode End

                // Platinum Start
                new Item(ModUtils.itemBuilder()).setRegistryName("platinum_ingot"),
                new SwordItem(ToolMaterialList.PLATINUM, 4, -2.4f, ModUtils.itemBuilder()).setRegistryName("platinum_sword"),
                new PickaxeItem(ToolMaterialList.PLATINUM, 2, -2.8f, ModUtils.itemBuilder()).setRegistryName("platinum_pick"),
                new AxeItem(ToolMaterialList.PLATINUM, 2, -2.8f, ModUtils.itemBuilder()).setRegistryName("platinum_axe"),
                new ShovelItem(ToolMaterialList.PLATINUM, 1.5f, -3.0f, ModUtils.itemBuilder()).setRegistryName("platinum_shovel"),
                new ItemArmorBase("platinum_helmet", ArmorMaterialList.PLATINUM, EquipmentSlotType.HEAD),
                new ItemArmorBase("platinum_chestplate", ArmorMaterialList.PLATINUM, EquipmentSlotType.CHEST),
                new ItemArmorBase("platinum_leggings", ArmorMaterialList.PLATINUM, EquipmentSlotType.LEGS),
                new ItemArmorBase("platinum_boots", ArmorMaterialList.PLATINUM, EquipmentSlotType.FEET)
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
        GEODE(9.3f, 4.0f, 2164, 4, 25, geode),
        PLATINUM(5.5f, 2.5f, 645, 3, 20, platinumIngot);

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

        @Override public int getMaxUses() { return durability; }
        @Override public float getEfficiency() { return efficiency; }
        @Override public float getAttackDamage() { return attackDamage; }
        @Override public int getHarvestLevel() { return harvestLevel; }
        @Override public int getEnchantability() { return enchantibility; }
        @Override public Ingredient getRepairMaterial() { return Ingredient.fromItems(repairMaterial); }
    }

    /** Enum Handling the armor materials - Manages dmg reduction, enchantability, durability etc. */
    protected enum ArmorMaterialList implements IArmorMaterial
    {
        GEODE("geode", new int[] {4, 7, 9, 4}, 2.8f, 48, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, geode),
        PLATINUM("platinum", new int[] {2, 5, 7, 2}, 0.2f, 20, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, platinumIngot);

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

        @Override public int getDurability(EquipmentSlotType slotIn) { return durabilityArray[slotIn.getIndex()] * this.durability; }
        @Override public int getDamageReductionAmount(EquipmentSlotType slotIn) { return dmgReduction[slotIn.getIndex()]; }
        @Override public int getEnchantability() { return enchantability; }
        @Override public SoundEvent getSoundEvent() { return sound; }
        @Override public Ingredient getRepairMaterial() { return Ingredient.fromItems(repairMaterial); }
        @Override public float getToughness() { return toughness; }
        @Override
        @OnlyIn(Dist.CLIENT)
        public String getName() { return name; }
    }

    //  ===========================
    //          Food List
    //  ===========================
    
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