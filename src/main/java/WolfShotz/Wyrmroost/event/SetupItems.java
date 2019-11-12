package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.*;
import WolfShotz.Wyrmroost.content.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.content.items.base.ItemArmorBase;
import WolfShotz.Wyrmroost.content.items.base.ToolMaterialList;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SetupItems
{
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<Item> TARRAGON_TOME              = register("tarragon_tome", new TarragonTomeItem());
    public static final RegistryObject<Item> MINUTUS                    = register("minutus", new MinutusItem());
    public static final RegistryObject<Item> DRAGON_EGG                 = register("dragon_egg", new DragonEggItem());
    public static final RegistryObject<Item> SOUL_CRYSTAL               = register("soul_crystal", new SoulCrystalItem());
    public static final RegistryObject<Item> DRAGON_STAFF               = register("dragon_staff", new DragonStaffItem());
    
    public static final RegistryObject<Item> GEODE_BLUE                 = register("geode", SetupItems.basicItem());
    public static final RegistryObject<Item> GEODE_RED                  = register("geode_red", SetupItems.basicItem());
    public static final RegistryObject<Item> GEODE_PURPLE               = register("geode_purple", SetupItems.basicItem());
    public static final RegistryObject<Item> PLATINUM_INGOT             = register("platinum_ingot", SetupItems.basicItem());
    
    public static final RegistryObject<Item> BLUE_GEODE_SWORD           = register("geode_sword", new SwordItem(ToolMaterialList.GEODE, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_PICKAXE         = register("geode_pick", new PickaxeItem(ToolMaterialList.GEODE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_AXE             = register("geode_axe", new AxeItem(ToolMaterialList.GEODE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_SHOVEL          = register("geode_shovel", new ShovelItem(ToolMaterialList.GEODE, 1.5f, -3.0f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_HOE             = register("geode_hoe", new HoeItem(ToolMaterialList.GEODE, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_HELMET          = register("geode_helmet", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> BLUE_GEODE_CHESTPLATE      = register("geode_chestplate", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> BLUE_GEODE_LEGGINGS        = register("geode_leggings", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> BLUE_GEODE_BOOTS           = register("geode_boots", new ItemArmorBase(ArmorMaterialList.GEODE, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> RED_GEODE_SWORD            = register("geode_red_sword", new SwordItem(ToolMaterialList.GEODERED, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_PICKAXE          = register("geode_red_pick", new PickaxeItem(ToolMaterialList.GEODERED, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_AXE              = register("geode_red_axe", new AxeItem(ToolMaterialList.GEODERED, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_SHOVEL           = register("geode_red_shovel", new ShovelItem(ToolMaterialList.GEODERED, 1.5f, -3.0f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_HOE              = register("geode_red_hoe", new HoeItem(ToolMaterialList.GEODERED, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_HELMET           = register("geode_red_helmet", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> RED_GEODE_CHESTPLATE       = register("geode_red_chestplate", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> RED_GEODE_LEGGINGS         = register("geode_red_leggings", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> RED_GEODE_BOOTS            = register("geode_red_boots", new ItemArmorBase(ArmorMaterialList.GEODERED, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> PURPLE_GEODE_SWORD         = register("geode_purple_sword", new SwordItem(ToolMaterialList.GEODEPURPLE, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_PICKAXE       = register("geode_purple_pick", new PickaxeItem(ToolMaterialList.GEODEPURPLE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_AXE           = register("geode_purple_axe", new AxeItem(ToolMaterialList.GEODEPURPLE, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_SHOVEL        = register("geode_purple_shovel", new ShovelItem(ToolMaterialList.GEODEPURPLE, 1.5f, -3.0f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HOE           = register("geode_purple_hoe", new HoeItem(ToolMaterialList.GEODEPURPLE, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HELMET        = register("geode_purple_helmet", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PURPLE_GEODE_CHESTPLATE    = register("geode_purple_chestplate", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PURPLE_GEODE_LEGGINGS      = register("geode_purple_leggings", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PURPLE_GEODE_BOOTS         = register("geode_purple_boots", new ItemArmorBase(ArmorMaterialList.GEODEPURPLE, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> PLATINUM_SWORD             = register("platinum_sword", new SwordItem(ToolMaterialList.PLATINUM, 4, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_PICKAXE           = register("platinum_pick", new PickaxeItem(ToolMaterialList.PLATINUM, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_AXE               = register("platinum_axe", new AxeItem(ToolMaterialList.PLATINUM, 2, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_SHOVEL            = register("platinum_shovel", new ShovelItem(ToolMaterialList.PLATINUM, 1.5f, -3f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_HOE               = register("platinum_hoe", new HoeItem(ToolMaterialList.PLATINUM, 1.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_HELMET            = register("platinum_helmet", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PLATINUM_CHESTPLATE        = register("platinum_chestplate", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PLATINUM_LEGGINGS          = register("platinum_leggings", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PLATINUM_BOOTS             = register("platinum_boots", new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.FEET));
    
    public static final RegistryObject<Item> FOOD_COOKED_MINUTUS        = register("cooked_minutus", new Item(ModUtils.itemBuilder().food(SetupItems.cookedminutus)));
    public static final RegistryObject<Item> FOOD_DRAKE_MEAT_RAW        = register("drake_meat_raw", new Item(ModUtils.itemBuilder().food(SetupItems.rawDrake)));
    public static final RegistryObject<Item> FOOD_DRAKE_MEAT_COOKED     = register("drake_meat_cooked", new Item(ModUtils.itemBuilder().food(SetupItems.cookedDrake)));
    public static final RegistryObject<Item> FOOD_JEWELLED_APPLE        = register("jewelled_apple", new Item(ModUtils.itemBuilder().food(SetupItems.jewelledApple)));
    public static final RegistryObject<Item> FOOD_DRAGON_FRUIT          = register("dragon_fruit", new Item(ModUtils.itemBuilder().food(SetupItems.dragonfruit)));
    
    public static final RegistryObject<Item> DRAGON_ARMOR_IRON          = register("iron_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.IRON));
    public static final RegistryObject<Item> DRAGON_ARMOR_GOLD          = register("gold_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.GOLD));
    public static final RegistryObject<Item> DRAGON_ARMOR_DIAMOND       = register("diamond_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.DIAMOND));
    public static final RegistryObject<Item> DRAGON_ARMOR_PLATINUM      = register("platinum_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.PLATINUM));
    public static final RegistryObject<Item> DRAGON_ARMOR_BLUE_GEODE    = register("blue_geode_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.BLUE_GEODE));
    public static final RegistryObject<Item> DRAGON_ARMOR_RED_GEODE     = register("red_geode_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.RED_GEODE));
    public static final RegistryObject<Item> DRAGON_ARMOR_PUEPLE_GEODE  = register("purple_geode_dragonarmor", new DragonArmorItem(DragonArmorItem.DragonArmorType.PURPLE_GEODE));
    
    public static final RegistryObject<Item> EGG_DRAKE                  = register("drake_egg", new SpawnEggItem(SetupEntities.OVERWORLD_DRAKE.get(), 0x15ff00, 0x085e00, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> EGG_MINUTUS                = register("minutus_egg", new SpawnEggItem(SetupEntities.MINUTUS.get(), 0xfcc0ea, 0xfcd4f0, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> EGG_SILVER_GLIDER          = register("silverglider_egg", new SpawnEggItem(SetupEntities.SILVER_GLIDER.get(), 0xffffff, 0xffffff, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> EGG_ROOSTSTALKER           = register("rooststalker_egg", new SpawnEggItem(SetupEntities.ROOSTSTALKER.get(), 0xffffff, 0xffffff, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> EGG_BUTTERFLYLEVIATHAN     = register("butterflyleviathan_egg", new SpawnEggItem(SetupEntities.BUTTERFLY_LEVIATHAN.get(), 0xffffff, 0xffffff, ModUtils.itemBuilder()));

    //  ===========================
    //          Food List
    //  ===========================
    
    // Jewelled Apple
    private static Food jewelledApple = new Food.Builder()
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
                                                .meat()
                                                .build();
    // Raw Drake Meat
    private static Food rawDrake = new Food.Builder()
                                           .hunger(4)
                                           .saturation(0.45f)
                                           .meat()
                                           .build();
    // Cooked Drake Meat
    private static Food cookedDrake = new Food.Builder()
                                           .hunger(8)
                                           .saturation(1f)
                                           .meat()
                                           .build();
    
    
    public static RegistryObject<Item> register(String name, Item item) { return ITEMS.register(name, () -> item); }
    
    private static Item basicItem() { return new Item(ModUtils.itemBuilder()); }
}