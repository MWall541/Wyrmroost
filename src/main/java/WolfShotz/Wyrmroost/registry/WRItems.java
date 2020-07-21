package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.items.*;
import WolfShotz.Wyrmroost.items.base.ArmorMaterialList;
import WolfShotz.Wyrmroost.items.base.ItemArmorBase;
import WolfShotz.Wyrmroost.items.base.ToolMaterialList;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WRItems
{
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Wyrmroost.MOD_ID);

    //    public static final RegistryObject<Item> TARRAGON_TOME = register("tarragon_tome", TarragonTomeItem::new);
    public static final RegistryObject<Item> LDWYRM = register("desert_wyrm", LDWyrmItem::new);
    public static final RegistryObject<Item> DRAGON_EGG = register("dragon_egg", DragonEggItem::new);
    public static final RegistryObject<Item> SOUL_CRYSTAL = register("soul_crystal", SoulCrystalItem::new);
    public static final RegistryObject<Item> DRAGON_STAFF = register("dragon_staff", DragonStaffItem::new);
    public static final RegistryObject<Item> COIN_DRAGON = register("coin_dragon", CoinDragonItem::new);
    public static final RegistryObject<Item> TRUMPET = register("trumpet", TrumpetItem::new);

    public static final RegistryObject<Item> BLUE_GEODE = register("blue_geode");
    public static final RegistryObject<Item> RED_GEODE = register("red_geode");
    public static final RegistryObject<Item> PURPLE_GEODE = register("purple_geode");
    public static final RegistryObject<Item> PLATINUM_INGOT = register("platinum_ingot");
    //    public static final RegistryObject<Item> BLUE_SHARD = register("blue_shard");
//    public static final RegistryObject<Item> GREEN_SHARD = register("green_shard");
//    public static final RegistryObject<Item> ORANGE_SHARD = register("orange_shard");
//    public static final RegistryObject<Item> YELLOW_SHARD = register("yellow_shard");
//    public static final RegistryObject<Item> ASH_PILE = register("ash_pile");
    public static final RegistryObject<Item> DRAKE_BACKPLATE = register("drake_backplate");
//    public static final RegistryObject<Item> CANARI_FEATHER = register("canari_feather");

    public static final RegistryObject<Item> BLUE_GEODE_SWORD = register("blue_geode_sword", () -> new SwordItem(ToolMaterialList.BLUE_GEODE, 2, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_PICKAXE = register("blue_geode_pickaxe", () -> new PickaxeItem(ToolMaterialList.BLUE_GEODE, 0, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_AXE = register("blue_geode_axe", () -> new AxeItem(ToolMaterialList.BLUE_GEODE, 3.5f, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_SHOVEL = register("blue_geode_shovel", () -> new ShovelItem(ToolMaterialList.BLUE_GEODE, 1f, -3f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_HOE = register("blue_geode_hoe", () -> new HoeItem(ToolMaterialList.BLUE_GEODE, 1f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> BLUE_GEODE_HELMET = register("blue_geode_helmet", () -> new ItemArmorBase(ArmorMaterialList.BLUE_GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> BLUE_GEODE_CHESTPLATE = register("blue_geode_chestplate", () -> new ItemArmorBase(ArmorMaterialList.BLUE_GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> BLUE_GEODE_LEGGINGS = register("blue_geode_leggings", () -> new ItemArmorBase(ArmorMaterialList.BLUE_GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> BLUE_GEODE_BOOTS = register("blue_geode_boots", () -> new ItemArmorBase(ArmorMaterialList.BLUE_GEODE, EquipmentSlotType.FEET));
    public static final RegistryObject<Item> BLUE_GEODE_ARROW = register("blue_geode_tipped_arrow", () -> new GeodeTippedArrowItem(3));

    public static final RegistryObject<Item> RED_GEODE_SWORD = register("red_geode_sword", () -> new SwordItem(ToolMaterialList.RED_GEODE, 2, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_PICKAXE = register("red_geode_pickaxe", () -> new PickaxeItem(ToolMaterialList.RED_GEODE, 0, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_AXE = register("red_geode_axe", () -> new AxeItem(ToolMaterialList.RED_GEODE, 3.5f, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_SHOVEL = register("red_geode_shovel", () -> new ShovelItem(ToolMaterialList.RED_GEODE, 1f, -3f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_HOE = register("red_geode_hoe", () -> new HoeItem(ToolMaterialList.RED_GEODE, 2f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> RED_GEODE_HELMET = register("red_geode_helmet", () -> new ItemArmorBase(ArmorMaterialList.RED_GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> RED_GEODE_CHESTPLATE = register("red_geode_chestplate", () -> new ItemArmorBase(ArmorMaterialList.RED_GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> RED_GEODE_LEGGINGS = register("red_geode_leggings", () -> new ItemArmorBase(ArmorMaterialList.RED_GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> RED_GEODE_BOOTS = register("red_geode_boots", () -> new ItemArmorBase(ArmorMaterialList.RED_GEODE, EquipmentSlotType.FEET));
    public static final RegistryObject<Item> RED_GEODE_ARROW = register("red_geode_tipped_arrow", () -> new GeodeTippedArrowItem(3.5));

    public static final RegistryObject<Item> PURPLE_GEODE_SWORD = register("purple_geode_sword", () -> new SwordItem(ToolMaterialList.PURPLE_GEODE, 2, -2f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_PICKAXE = register("purple_geode_pickaxe", () -> new PickaxeItem(ToolMaterialList.PURPLE_GEODE, 0, -2.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_AXE = register("purple_geode_axe", () -> new AxeItem(ToolMaterialList.PURPLE_GEODE, 3.5f, -2.6f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_SHOVEL = register("purple_geode_shovel", () -> new ShovelItem(ToolMaterialList.PURPLE_GEODE, 1f, -2.7f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HOE = register("purple_geode_hoe", () -> new HoeItem(ToolMaterialList.PURPLE_GEODE, 3f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PURPLE_GEODE_HELMET = register("purple_geode_helmet", () -> new ItemArmorBase(ArmorMaterialList.PURPLE_GEODE, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PURPLE_GEODE_CHESTPLATE = register("purple_geode_chestplate", () -> new ItemArmorBase(ArmorMaterialList.PURPLE_GEODE, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PURPLE_GEODE_LEGGINGS = register("purple_geode_leggings", () -> new ItemArmorBase(ArmorMaterialList.PURPLE_GEODE, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PURPLE_GEODE_BOOTS = register("purple_geode_boots", () -> new ItemArmorBase(ArmorMaterialList.PURPLE_GEODE, EquipmentSlotType.FEET));
    public static final RegistryObject<Item> PURPLE_GEODE_ARROW = register("purple_geode_tipped_arrow", () -> new GeodeTippedArrowItem(4));

    public static final RegistryObject<Item> PLATINUM_SWORD = register("platinum_sword", () -> new SwordItem(ToolMaterialList.PLATINUM, 2, -2.4f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_PICKAXE = register("platinum_pickaxe", () -> new PickaxeItem(ToolMaterialList.PLATINUM, 0, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_AXE = register("platinum_axe", () -> new AxeItem(ToolMaterialList.PLATINUM, 3.5f, -2.8f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_SHOVEL = register("platinum_shovel", () -> new ShovelItem(ToolMaterialList.PLATINUM, 1f, -3f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_HOE = register("platinum_hoe", () -> new HoeItem(ToolMaterialList.PLATINUM, -0.5f, ModUtils.itemBuilder()));
    public static final RegistryObject<Item> PLATINUM_HELMET = register("platinum_helmet", () -> new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> PLATINUM_CHESTPLATE = register("platinum_chestplate", () -> new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> PLATINUM_LEGGINGS = register("platinum_leggings", () -> new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> PLATINUM_BOOTS = register("platinum_boots", () -> new ItemArmorBase(ArmorMaterialList.PLATINUM, EquipmentSlotType.FEET));

    public static final RegistryObject<Item> DRAKE_HELMET = register("drake_helmet", () -> new DrakeArmorItem(EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> DRAKE_CHESTPLATE = register("drake_chestplate", () -> new DrakeArmorItem(EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> DRAKE_LEGGINGS = register("drake_leggings", () -> new DrakeArmorItem(EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> DRAKE_BOOTS = register("drake_boots", () -> new DrakeArmorItem(EquipmentSlotType.FEET));

//    public static final RegistryObject<Item> CANARI_HELMET = register("canari_helmet", () -> new CanariArmorItem(EquipmentSlotType.HEAD));
//    public static final RegistryObject<Item> CANARI_CHESTPLATE = register("canari_chestplate", () -> new CanariArmorItem(EquipmentSlotType.CHEST));
//    public static final RegistryObject<Item> CANARI_BOOTS = register("canari_boots", () -> new CanariArmorItem(EquipmentSlotType.FEET));

    public static final RegistryObject<Item> RAW_LOWTIER_MEAT = register("raw_lowtier_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.RAW_LOWTIER_MEAT)));
    public static final RegistryObject<Item> RAW_COMMON_MEAT = register("raw_common_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.RAW_COMMON_MEAT)));
    public static final RegistryObject<Item> RAW_APEX_MEAT = register("raw_apex_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.RAW_APEX_MEAT)));
    public static final RegistryObject<Item> RAW_BEHEMOTH_MEAT = register("raw_behemoth_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.RAW_BEHEMOTH_MEAT)));
    public static final RegistryObject<Item> COOKED_LOWTIER_MEAT = register("cooked_lowtier_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.COOKED_LOWTIER_MEAT)));
    public static final RegistryObject<Item> COOKED_COMMON_MEAT = register("cooked_common_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.COOKED_COMMON_MEAT)));
    public static final RegistryObject<Item> COOKED_APEX_MEAT = register("cooked_apex_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.COOKED_APEX_MEAT)));
    public static final RegistryObject<Item> COOKED_BEHEMOTH_MEAT = register("cooked_behemoth_meat", () -> new Item(ModUtils.itemBuilder().food(WRFoods.COOKED_BEHEMOTH_MEAT)));
    public static final RegistryObject<Item> COOKED_MINUTUS = register("cooked_desertwyrm", () -> new Item(ModUtils.itemBuilder().food(WRFoods.COOKED_DESERTWYRM)));
    public static final RegistryObject<Item> JEWELLED_APPLE = register("jewelled_apple", () -> new Item(ModUtils.itemBuilder().food(WRFoods.JEWELLED_APPLE)));
//    public static final RegistryObject<Item> DRAGON_FRUIT = register("dragon_fruit", () -> new Item(ModUtils.itemBuilder().food(WRFoods.DRAGON_FRUIT)));
//    public static final RegistryObject<Item> BLUEBERRIES = register("blueberries", () -> new Item(ModUtils.itemBuilder().food(WRFoods.BLUEBERRIES)));
//    public static final RegistryObject<Item> CANARI_CHERRY = register("canari_cherry", () -> new Item(ModUtils.itemBuilder().food(WRFoods.CANARI_CHERRY)));

    public static final RegistryObject<Item> DRAGON_ARMOR_IRON = register("iron_dragon_armor", () -> new DragonArmorItem(5, ArmorMaterial.IRON.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_GOLD = register("gold_dragon_armor", () -> new DragonArmorItem(7, ArmorMaterial.GOLD.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_DIAMOND = register("diamond_dragon_armor", () -> new DragonArmorItem(11, ArmorMaterial.DIAMOND.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_PLATINUM = register("platinum_dragon_armor", () -> new DragonArmorItem(6, ArmorMaterialList.PLATINUM.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_BLUE_GEODE = register("blue_geode_dragon_armor", () -> new DragonArmorItem(16, ArmorMaterialList.BLUE_GEODE.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_RED_GEODE = register("red_geode_dragon_armor", () -> new DragonArmorItem(18, ArmorMaterialList.RED_GEODE.getEnchantability()));
    public static final RegistryObject<Item> DRAGON_ARMOR_PURPLE_GEODE = register("purple_geode_dragon_armor", () -> new DragonArmorItem(20, ArmorMaterialList.PURPLE_GEODE.getEnchantability()));

    static RegistryObject<Item> register(String name, Supplier<Item> item) { return REGISTRY.register(name, item); }

    static RegistryObject<Item> register(String name) { return REGISTRY.register(name, () -> new Item(ModUtils.itemBuilder())); }

    public static class WRFoods
    {
        public static final Food RAW_LOWTIER_MEAT = new Food.Builder().hunger(2).saturation(0.25f).meat().build();
        public static final Food RAW_COMMON_MEAT = new Food.Builder().hunger(3).saturation(0.35f).meat().build();
        public static final Food RAW_APEX_MEAT = new Food.Builder().hunger(5).saturation(0.45f).meat().build();
        public static final Food RAW_BEHEMOTH_MEAT = new Food.Builder().hunger(7).saturation(0.7f).meat().build();
        public static final Food COOKED_LOWTIER_MEAT = new Food.Builder().hunger(5).saturation(0.7f).meat().build();
        public static final Food COOKED_COMMON_MEAT = new Food.Builder().hunger(8).saturation(0.8f).meat().build();
        public static final Food COOKED_APEX_MEAT = new Food.Builder().hunger(12).saturation(1f).meat().build();
        public static final Food COOKED_BEHEMOTH_MEAT = new Food.Builder().hunger(16).saturation(1.4f).meat().build();
        public static final Food DRAGON_FRUIT = new Food.Builder().hunger(6).saturation(0.55f).build();
        public static final Food BLUEBERRIES = new Food.Builder().hunger(2).saturation(0.1f).build();
        public static final Food COOKED_DESERTWYRM = new Food.Builder().hunger(6).saturation(0.7f).meat().build();
        public static final Food CANARI_CHERRY = new Food.Builder().hunger(2).saturation(1f).build();
        public static final Food CINIS_ROOT = new Food.Builder().hunger(2).saturation(0.5f).effect(() -> new EffectInstance(Effects.HASTE, 300), 1f).build();
        public static final Food JEWELLED_APPLE = new Food.Builder().hunger(8).saturation(0.9f).setAlwaysEdible()
                .effect(() -> new EffectInstance(Effects.GLOWING, 800), 1.0f)
                .effect(() -> new EffectInstance(Effects.REGENERATION, 100, 2), 1.0f)
                .effect(() -> new EffectInstance(Effects.RESISTANCE, 800), 1.0f)
                .effect(() -> new EffectInstance(Effects.ABSORPTION, 6000, 2), 1.0f)
                .effect(() -> new EffectInstance(Effects.NIGHT_VISION, 800), 1.0f)
                .build();
    }

    public static class Tags extends Tag<Item>
    {
        public Tags(ResourceLocation resourceLocationIn) { super(resourceLocationIn); }

        public static final Tag<Item> GEODES = tag("geodes");
        public static final Tag<Item> MEATS = forgeTag("meats");
        public static final Tag<Item> DRAGON_MEATS = tag("dragon_meats");
        public static final Tag<Item> PLATINUM = forgeTag("ingots/platinum");

        private static Tag<Item> tag(String name) { return new ItemTags.Wrapper(Wyrmroost.rl(name)); }

        private static Tag<Item> forgeTag(String name) { return new ItemTags.Wrapper(new ResourceLocation("forge", name)); }
    }
}