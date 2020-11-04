package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.items.*;
import com.github.wolfshotz.wyrmroost.items.base.ArmorBase;
import com.github.wolfshotz.wyrmroost.items.base.ArmorMaterials;
import com.github.wolfshotz.wyrmroost.items.base.ToolMaterials;
import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WRItems
{
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Item> LDWYRM = register("desert_wyrm", LDWyrmItem::new);
    public static final RegistryObject<Item> DRAGON_EGG = register("dragon_egg", DragonEggItem::new);
    public static final RegistryObject<Item> SOUL_CRYSTAL = register("soul_crystal", SoulCrystalItem::new);
    public static final RegistryObject<Item> DRAGON_STAFF = register("dragon_staff", DragonStaffItem::new);
    public static final RegistryObject<Item> COIN_DRAGON = register("coin_dragon", CoinDragonItem::new);
    public static final RegistryObject<Item> TRUMPET = register("trumpet", TrumpetItem::new);
    public static final RegistryObject<Item> SILK_GLAND = register("orbwyrm_silk_gland", SilkGlandItem::new);
//    public static final RegistryObject<Item> FOG_WRAITH_TAILS = register("fog_wraith_tails", FogWraithTailsItem::new);

    public static final RegistryObject<Item> BLUE_GEODE = register("blue_geode");
    public static final RegistryObject<Item> RED_GEODE = register("red_geode");
    public static final RegistryObject<Item> PURPLE_GEODE = register("purple_geode");
    public static final RegistryObject<Item> PLATINUM_INGOT = register("platinum_ingot");
    public static final RegistryObject<Item> DRAKE_BACKPLATE = register("drake_backplate");

    public static final RegistryObject<Item> BLUE_GEODE_SWORD = register("blue_geode_sword", () -> new SwordItem(ToolMaterials.BLUE_GEODE, 5, -2.4f, builder()));
    public static final RegistryObject<Item> BLUE_GEODE_PICKAXE = register("blue_geode_pickaxe", () -> new PickaxeItem(ToolMaterials.BLUE_GEODE, 3, -2.8f, builder()));
    public static final RegistryObject<Item> BLUE_GEODE_AXE = register("blue_geode_axe", () -> new AxeItem(ToolMaterials.BLUE_GEODE, 7.5f, -3f, builder()));
    public static final RegistryObject<Item> BLUE_GEODE_SHOVEL = register("blue_geode_shovel", () -> new ShovelItem(ToolMaterials.BLUE_GEODE, 3.5f, -3f, builder()));
    public static final RegistryObject<Item> BLUE_GEODE_HOE = register("blue_geode_hoe", () -> new HoeItem(ToolMaterials.BLUE_GEODE, 1, 2.5f, builder()));
    public static final RegistryObject<Item> BLUE_GEODE_HELMET = register("blue_geode_helmet", () -> new ArmorBase(ArmorMaterials.BLUE_GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> BLUE_GEODE_CHESTPLATE = register("blue_geode_chestplate", () -> new ArmorBase(ArmorMaterials.BLUE_GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> BLUE_GEODE_LEGGINGS = register("blue_geode_leggings", () -> new ArmorBase(ArmorMaterials.BLUE_GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> BLUE_GEODE_BOOTS = register("blue_geode_boots", () -> new ArmorBase(ArmorMaterials.BLUE_GEODE, EquipmentSlotType.FEET));
    public static final RegistryObject<Item> BLUE_GEODE_ARROW = register("blue_geode_tipped_arrow", () -> new GeodeTippedArrowItem(3));

    public static final RegistryObject<Item> RED_GEODE_SWORD = register("red_geode_sword", () -> new SwordItem(ToolMaterials.RED_GEODE, 6, -2.4f, builder()));
    public static final RegistryObject<Item> RED_GEODE_PICKAXE = register("red_geode_pickaxe", () -> new PickaxeItem(ToolMaterials.RED_GEODE, 4, -2.8f, builder()));
    public static final RegistryObject<Item> RED_GEODE_AXE = register("red_geode_axe", () -> new AxeItem(ToolMaterials.RED_GEODE, 8f, -3f, builder()));
    public static final RegistryObject<Item> RED_GEODE_SHOVEL = register("red_geode_shovel", () -> new ShovelItem(ToolMaterials.RED_GEODE, 4f, -3f, builder()));
    public static final RegistryObject<Item> RED_GEODE_HOE = register("red_geode_hoe", () -> new HoeItem(ToolMaterials.RED_GEODE, 2, 0, builder()));
    public static final RegistryObject<Item> RED_GEODE_HELMET = register("red_geode_helmet", () -> new ArmorBase(ArmorMaterials.RED_GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> RED_GEODE_CHESTPLATE = register("red_geode_chestplate", () -> new ArmorBase(ArmorMaterials.RED_GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> RED_GEODE_LEGGINGS = register("red_geode_leggings", () -> new ArmorBase(ArmorMaterials.RED_GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> RED_GEODE_BOOTS = register("red_geode_boots", () -> new ArmorBase(ArmorMaterials.RED_GEODE, EquipmentSlotType.FEET));
    public static final RegistryObject<Item> RED_GEODE_ARROW = register("red_geode_tipped_arrow", () -> new GeodeTippedArrowItem(3.5));

    public static final RegistryObject<Item> PURPLE_GEODE_SWORD = register("purple_geode_sword", () -> new SwordItem(ToolMaterials.PURPLE_GEODE, 8, -2.4f, builder()));
    public static final RegistryObject<Item> PURPLE_GEODE_PICKAXE = register("purple_geode_pickaxe", () -> new PickaxeItem(ToolMaterials.PURPLE_GEODE, 6, -3f, builder()));
    public static final RegistryObject<Item> PURPLE_GEODE_AXE = register("purple_geode_axe", () -> new AxeItem(ToolMaterials.PURPLE_GEODE, 10f, -2.9f, builder()));
    public static final RegistryObject<Item> PURPLE_GEODE_SHOVEL = register("purple_geode_shovel", () -> new ShovelItem(ToolMaterials.PURPLE_GEODE, 6.5f, -2.7f, builder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HOE = register("purple_geode_hoe", () -> new HoeItem(ToolMaterials.PURPLE_GEODE, 0, 4f, builder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HELMET = register("purple_geode_helmet", () -> new ArmorBase(ArmorMaterials.PURPLE_GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PURPLE_GEODE_CHESTPLATE = register("purple_geode_chestplate", () -> new ArmorBase(ArmorMaterials.PURPLE_GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PURPLE_GEODE_LEGGINGS = register("purple_geode_leggings", () -> new ArmorBase(ArmorMaterials.PURPLE_GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PURPLE_GEODE_BOOTS = register("purple_geode_boots", () -> new ArmorBase(ArmorMaterials.PURPLE_GEODE, EquipmentSlotType.FEET));
    public static final RegistryObject<Item> PURPLE_GEODE_ARROW = register("purple_geode_tipped_arrow", () -> new GeodeTippedArrowItem(4));

    public static final RegistryObject<Item> PLATINUM_SWORD = register("platinum_sword", () -> new SwordItem(ToolMaterials.PLATINUM, 5, -2.4f, builder()));
    public static final RegistryObject<Item> PLATINUM_PICKAXE = register("platinum_pickaxe", () -> new PickaxeItem(ToolMaterials.PLATINUM, 3, -2.8f, builder()));
    public static final RegistryObject<Item> PLATINUM_AXE = register("platinum_axe", () -> new AxeItem(ToolMaterials.PLATINUM, 7.5f, -2.8f, builder()));
    public static final RegistryObject<Item> PLATINUM_SHOVEL = register("platinum_shovel", () -> new ShovelItem(ToolMaterials.PLATINUM, 3f, -3f, builder()));
    public static final RegistryObject<Item> PLATINUM_HOE = register("platinum_hoe", () -> new HoeItem(ToolMaterials.PLATINUM, 0, -1, builder()));
    public static final RegistryObject<Item> PLATINUM_HELMET = register("platinum_helmet", () -> new ArmorBase(ArmorMaterials.PLATINUM, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PLATINUM_CHESTPLATE = register("platinum_chestplate", () -> new ArmorBase(ArmorMaterials.PLATINUM, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PLATINUM_LEGGINGS = register("platinum_leggings", () -> new ArmorBase(ArmorMaterials.PLATINUM, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PLATINUM_BOOTS = register("platinum_boots", () -> new ArmorBase(ArmorMaterials.PLATINUM, EquipmentSlotType.FEET));

    public static final RegistryObject<Item> DRAKE_HELMET = register("drake_helmet", () -> new DrakeArmorItem(EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> DRAKE_CHESTPLATE = register("drake_chestplate", () -> new DrakeArmorItem(EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> DRAKE_LEGGINGS = register("drake_leggings", () -> new DrakeArmorItem(EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> DRAKE_BOOTS = register("drake_boots", () -> new DrakeArmorItem(EquipmentSlotType.FEET));

    public static final RegistryObject<Item> RAW_LOWTIER_MEAT = register("raw_lowtier_meat", () -> new Item(builder().food(Foods.RAW_LOWTIER_MEAT)));
    public static final RegistryObject<Item> RAW_COMMON_MEAT = register("raw_common_meat", () -> new Item(builder().food(Foods.RAW_COMMON_MEAT)));
    public static final RegistryObject<Item> RAW_APEX_MEAT = register("raw_apex_meat", () -> new Item(builder().food(Foods.RAW_APEX_MEAT)));
    public static final RegistryObject<Item> RAW_BEHEMOTH_MEAT = register("raw_behemoth_meat", () -> new Item(builder().food(Foods.RAW_BEHEMOTH_MEAT)));
    public static final RegistryObject<Item> COOKED_LOWTIER_MEAT = register("cooked_lowtier_meat", () -> new Item(builder().food(Foods.COOKED_LOWTIER_MEAT)));
    public static final RegistryObject<Item> COOKED_COMMON_MEAT = register("cooked_common_meat", () -> new Item(builder().food(Foods.COOKED_COMMON_MEAT)));
    public static final RegistryObject<Item> COOKED_APEX_MEAT = register("cooked_apex_meat", () -> new Item(builder().food(Foods.COOKED_APEX_MEAT)));
    public static final RegistryObject<Item> COOKED_BEHEMOTH_MEAT = register("cooked_behemoth_meat", () -> new Item(builder().food(Foods.COOKED_BEHEMOTH_MEAT)));
    public static final RegistryObject<Item> COOKED_MINUTUS = register("cooked_desertwyrm", () -> new Item(builder().food(Foods.COOKED_DESERTWYRM)));
    public static final RegistryObject<Item> JEWELLED_APPLE = register("jewelled_apple", () -> new Item(builder().food(Foods.JEWELLED_APPLE)));

    public static final RegistryObject<Item> DRAGON_ARMOR_IRON = register("iron_dragon_armor", () -> new DragonArmorItem(4, ArmorMaterial.IRON.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_GOLD = register("gold_dragon_armor", () -> new DragonArmorItem(6, ArmorMaterial.GOLD.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_DIAMOND = register("diamond_dragon_armor", () -> new DragonArmorItem(8, ArmorMaterial.DIAMOND.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_PLATINUM = register("platinum_dragon_armor", () -> new DragonArmorItem(7, ArmorMaterials.PLATINUM.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_BLUE_GEODE = register("blue_geode_dragon_armor", () -> new DragonArmorItem(10, ArmorMaterials.BLUE_GEODE.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_RED_GEODE = register("red_geode_dragon_armor", () -> new DragonArmorItem(13, ArmorMaterials.RED_GEODE.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_PURPLE_GEODE = register("purple_geode_dragon_armor", () -> new DragonArmorItem(18, ArmorMaterials.PURPLE_GEODE.getEnchantability()));

    static RegistryObject<Item> register(String name, Supplier<Item> item) { return REGISTRY.register(name, item); }

    static RegistryObject<Item> register(String name) { return REGISTRY.register(name, () -> new Item(builder())); }

    public static Item.Properties builder() { return new Item.Properties().group(Wyrmroost.ITEM_GROUP); }

    private static class Foods
    {
        static final Food RAW_LOWTIER_MEAT = new Food.Builder().hunger(1).saturation(0.1f).meat().build();
        static final Food RAW_COMMON_MEAT = new Food.Builder().hunger(3).saturation(0.3f).meat().build();
        static final Food RAW_APEX_MEAT = new Food.Builder().hunger(5).saturation(0.45f).meat().build();
        static final Food RAW_BEHEMOTH_MEAT = new Food.Builder().hunger(7).saturation(0.7f).meat().build();
        static final Food COOKED_LOWTIER_MEAT = new Food.Builder().hunger(3).saturation(0.7f).meat().build();
        static final Food COOKED_COMMON_MEAT = new Food.Builder().hunger(8).saturation(0.8f).meat().build();
        static final Food COOKED_APEX_MEAT = new Food.Builder().hunger(16).saturation(1f).meat().build();
        static final Food COOKED_BEHEMOTH_MEAT = new Food.Builder().hunger(20).saturation(2f).meat().build();
        static final Food COOKED_DESERTWYRM = new Food.Builder().hunger(10).saturation(1f).meat().build();
        static final Food JEWELLED_APPLE = new Food.Builder().hunger(8).saturation(0.9f).setAlwaysEdible()
                .effect(() -> new EffectInstance(Effects.GLOWING, 800), 1f)
                .effect(() -> new EffectInstance(Effects.REGENERATION, 100, 2), 1f)
                .effect(() -> new EffectInstance(Effects.RESISTANCE, 800), 1f)
                .effect(() -> new EffectInstance(Effects.ABSORPTION, 6000, 2), 1f)
                .effect(() -> new EffectInstance(Effects.NIGHT_VISION, 800), 1f)
                .build();
    }

    public static class Tags
    {
        public static final INamedTag<Item> GEODES = tag("geodes");
        public static final INamedTag<Item> MEATS = forgeTag("meats");
        public static final INamedTag<Item> DRAGON_MEATS = tag("dragon_meats");
        public static final INamedTag<Item> PLATINUM = forgeTag("ingots/platinum");

        private static INamedTag<Item> tag(String name) { return ItemTags.makeWrapperTag(Wyrmroost.MOD_ID + ":" + name); }

        private static INamedTag<Item> forgeTag(String name) { return ItemTags.makeWrapperTag("forge:" + name); }
    }
}