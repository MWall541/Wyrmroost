package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.*;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.world.features.OseriTreeFeature;
import com.github.wolfshotz.wyrmroost.world.features.TreeGen;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.block.AbstractBlock.Properties;

public class WRBlocks
{
    public static final ItemGroup BLOCKS_ITEM_GROUP = new ItemGroup("wyrmroost_dimension")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(PURPLE_GEODE_BLOCK.get());
        }
    };

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);
    public static final List<BlockExtension> EXTENSIONS = new ArrayList<>();

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", () -> new Block(mineable(Material.STONE, ToolType.PICKAXE, 1, 3f, SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 1, 5f, SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", () -> new EXPBlock(3, 7, mineable(Material.STONE, ToolType.PICKAXE, 2, 3f, SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 2, 5f, SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", () -> new EXPBlock(4, 8, mineable(Material.STONE, ToolType.PICKAXE, 3, 3f, SoundType.NETHER_ORE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 3, 5f, SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", () -> new EXPBlock(8, 11, mineable(Material.METAL, ToolType.PICKAXE, 4, 5f, SoundType.GILDED_BLACKSTONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 4, 7f, SoundType.METAL)));

    // biomes
    public static final RegistryObject<Block> KARPO_BUSH = register("karpo_bush", () -> new BushBlock(replaceablePlant()), extend().cutoutRenderer().flammability(60, 100).tint(WRBlocks::grassTint));

    // tincture weald
    public static final RegistryObject<Block> MULCH = register("mulch", MulchBlock::new);
    public static final RegistryObject<Block> SILVER_MOSS = register("silver_moss", () -> new GrowingPlantBlock(plant().randomTicks(), Direction.DOWN, 2, 0.0875, WRBlocks.SILVER_MOSS_BODY), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> SILVER_MOSS_BODY = register("silver_moss_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.SILVER_MOSS), extend().cutoutRenderer().noItem().flammability(30, 60));
    public static final RegistryObject<Block> GILLA = register("gilla", GillaBushBlock::new, extend().cutoutRenderer().flammability(60, 100));
    public static final RegistryObject<Block> MOSS_VINE = register("moss_vine", () -> new VineBlock(properties(Material.REPLACEABLE_PLANT, SoundType.VINE).randomTicks().noCollission().strength(0.2f)), extend().cutoutRenderer().flammability(15, 100));
    public static final RegistryObject<Block> BLUE_OSERI_SAPLING = register("blue_oseri_sapling", () -> new SaplingBlock(new TreeGen(WRWorld.Features.CONFIGURED_BLUE_OSERI), plant().randomTicks()), extend().cutoutRenderer());
    public static final RegistryObject<Block> BLUE_OSERI_LEAVES = register("blue_oseri_leaves", () -> new OseriLeaves(OseriTreeFeature.Type.BLUE.color, leaves()), extend().cutoutRenderer().flammability(30, 60));
    public static final RegistryObject<Block> BLUE_OSERI_VINES = register("blue_oseri_vines", () -> new OseriVinesBlock(WRBlocks.BLUE_OSERI_VINES_BODY, OseriTreeFeature.Type.BLUE.color), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> BLUE_OSERI_VINES_BODY = register("blue_oseri_vines_body", () -> new OseriVinesBodyBlock(WRBlocks.BLUE_OSERI_VINES, OseriTreeFeature.Type.BLUE.color), extend().cutoutRenderer().noItem().flammability(30, 80));
    public static final RegistryObject<Block> BLUE_OSERI_PETALS = register("blue_oseri_petals", () -> new PetalsBlock(plant()), extend().cutoutRenderer().flammability(60, 30));
    public static final RegistryObject<Block> GOLD_OSERI_SAPLING = register("gold_oseri_sapling", () -> new SaplingBlock(new TreeGen(WRWorld.Features.CONFIGURED_GOLD_OSERI), plant().randomTicks()), extend().cutoutRenderer());
    public static final RegistryObject<Block> GOLD_OSERI_LEAVES = register("gold_oseri_leaves", () -> new OseriLeaves(OseriTreeFeature.Type.GOLD.color, leaves()), extend().cutoutRenderer().flammability(30, 60));
    public static final RegistryObject<Block> GOLD_OSERI_VINES = register("gold_oseri_vines", () -> new OseriVinesBlock(WRBlocks.GOLD_OSERI_VINES_BODY, OseriTreeFeature.Type.GOLD.color), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> GOLD_OSERI_VINES_BODY = register("gold_oseri_vines_body", () -> new OseriVinesBodyBlock(WRBlocks.GOLD_OSERI_VINES, OseriTreeFeature.Type.GOLD.color), extend().cutoutRenderer().noItem().flammability(30, 80));
    public static final RegistryObject<Block> GOLD_OSERI_PETALS = register("gold_oseri_petals", () -> new PetalsBlock(plant()), extend().cutoutRenderer().flammability(60, 30));
    public static final RegistryObject<Block> PINK_OSERI_SAPLING = register("pink_oseri_sapling", () -> new SaplingBlock(new TreeGen(WRWorld.Features.CONFIGURED_PINK_OSERI), plant().randomTicks()), extend().cutoutRenderer());
    public static final RegistryObject<Block> PINK_OSERI_LEAVES = register("pink_oseri_leaves", () -> new OseriLeaves(OseriTreeFeature.Type.PINK.color, leaves()), extend().cutoutRenderer().flammability(30, 60));
    public static final RegistryObject<Block> PINK_OSERI_VINES = register("pink_oseri_vines", () -> new OseriVinesBlock(WRBlocks.PINK_OSERI_VINES_BODY, OseriTreeFeature.Type.PINK.color), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> PINK_OSERI_VINES_BODY = register("pink_oseri_vines_body", () -> new OseriVinesBodyBlock(WRBlocks.PINK_OSERI_VINES, OseriTreeFeature.Type.PINK.color), extend().cutoutRenderer().noItem().flammability(30, 80));
    public static final RegistryObject<Block> PINK_OSERI_PETALS = register("pink_oseri_petals", () -> new PetalsBlock(plant()), extend().cutoutRenderer().flammability(60, 30));
    public static final RegistryObject<Block> PURPLE_OSERI_SAPLING = register("purple_oseri_sapling", () -> new SaplingBlock(new TreeGen(WRWorld.Features.CONFIGURED_PURPLE_OSERI), plant().randomTicks()), extend().cutoutRenderer());
    public static final RegistryObject<Block> PURPLE_OSERI_LEAVES = register("purple_oseri_leaves", () -> new OseriLeaves(OseriTreeFeature.Type.PURPLE.color, leaves()), extend().cutoutRenderer().flammability(30, 60));
    public static final RegistryObject<Block> PURPLE_OSERI_VINES = register("purple_oseri_vines", () -> new OseriVinesBlock(WRBlocks.PURPLE_OSERI_VINES_BODY, OseriTreeFeature.Type.PURPLE.color), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> PURPLE_OSERI_VINES_BODY = register("purple_oseri_vines_body", () -> new OseriVinesBodyBlock(WRBlocks.PURPLE_OSERI_VINES, OseriTreeFeature.Type.PURPLE.color), extend().cutoutRenderer().noItem().flammability(30, 80));
    public static final RegistryObject<Block> PURPLE_OSERI_PETALS = register("purple_oseri_petals", () -> new PetalsBlock(plant()), extend().cutoutRenderer().flammability(60, 30));
    public static final RegistryObject<Block> WHITE_OSERI_SAPLING = register("white_oseri_sapling", () -> new SaplingBlock(new TreeGen(WRWorld.Features.CONFIGURED_WHITE_OSERI), plant().randomTicks()), extend().cutoutRenderer());
    public static final RegistryObject<Block> WHITE_OSERI_LEAVES = register("white_oseri_leaves", () -> new OseriLeaves(OseriTreeFeature.Type.WHITE.color, leaves()), extend().cutoutRenderer().flammability(30, 60));
    public static final RegistryObject<Block> WHITE_OSERI_VINES = register("white_oseri_vines", () -> new OseriVinesBlock(WRBlocks.WHITE_OSERI_VINES_BODY, OseriTreeFeature.Type.WHITE.color), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> WHITE_OSERI_VINES_BODY = register("white_oseri_vines_body", () -> new OseriVinesBodyBlock(WRBlocks.WHITE_OSERI_VINES, OseriTreeFeature.Type.WHITE.color), extend().cutoutRenderer().noItem().flammability(30, 80));
    public static final RegistryObject<Block> WHITE_OSERI_PETALS = register("white_oseri_petals", () -> new PetalsBlock(plant()), extend().cutoutRenderer().flammability(60, 30));
    public static final WoodGroup OSERI_WOOD = WoodGroup.create("oseri", MaterialColor.SAND, MaterialColor.STONE);

    // frost crevasse
    public static final RegistryObject<Block> FROSTED_GRASS = register("frosted_grass", () -> new GrassBlock(properties(Material.DIRT, WRSounds.Types.FROSTED_GRASS).strength(0.55f)));
    public static final RegistryObject<Block> CREVASSE_COTTON = register("crevasse_cotton", CrevasseCottonBlock::new, extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> FROST_GOWN = register("frost_gown", () -> new TallFlowerBlock(properties(Material.REPLACEABLE_PLANT, SoundType.GRASS).noCollission()), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> HAMA_SHRUB = register("hama_shrub", () -> new GrowingPlantBlock(properties(Material.REPLACEABLE_PLANT, SoundType.GRASS).noCollission(), Direction.UP, 2, 0, WRBlocks.HAMA_SHRUB_BODY), extend().cutoutRenderer().flammability(30, 80));
    public static final RegistryObject<Block> HAMA_SHRUB_BODY = register("hama_shrub_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.HAMA_SHRUB), extend().cutoutRenderer().noItem().flammability(30, 60));
    public static final RegistryObject<Block> HOARFROST = register("hoarfrost", HoarfrostBlock::new, extend().render(() -> RenderHelper::translucent));
    public static final WoodGroup SAL_WOOD = WoodGroup.create("sal", MaterialColor.COLOR_LIGHT_GRAY, MaterialColor.COLOR_GRAY);
    public static final StoneGroup FORAH_STONE = StoneGroup.builder(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 1.5f, 6f, SoundType.GILDED_BLACKSTONE)).stairs().slab().pressurePlateAndButton(PressurePlateBlock.Sensitivity.MOBS).build("forah_stone");
    public static final StoneGroup FORAH_COBBLESTONE = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 1.5f, 6f, SoundType.GILDED_BLACKSTONE)).build("forah_cobblestone");
    public static final StoneGroup POLISHED_FORAH_STONE = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 1.5f, 6f, SoundType.GILDED_BLACKSTONE)).chiseled().build("polished_forah_stone");
    public static final StoneGroup FORAH_STONE_BRICKS = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 1.5f, 6f, SoundType.GILDED_BLACKSTONE)).build("forah_stone_bricks");

    // stygian sea
    public static final RegistryObject<Block> WHITE_SAND = register("white_sand", () -> new SandBlock(0xDEE0D6, properties(Material.SAND, MaterialColor.TERRACOTTA_WHITE, SoundType.SAND).strength(0.5F)));
    public static final WoodGroup PRISMARINE_CORIN_WOOD = ThinLogBlock.thinLogGroup(MaterialColor.COLOR_CYAN, MaterialColor.TERRACOTTA_CYAN).nonFlammable().build("prismarine_corin");
    public static final WoodGroup SILVER_CORIN_WOOD = ThinLogBlock.thinLogGroup(MaterialColor.COLOR_LIGHT_GRAY, MaterialColor.CLAY).nonFlammable().build("silver_corin");
    public static final WoodGroup TEAL_CORIN_WOOD = ThinLogBlock.thinLogGroup(MaterialColor.TERRACOTTA_CYAN, MaterialColor.TERRACOTTA_GREEN).nonFlammable().build("teal_corin");
    public static final WoodGroup RED_CORIN_WOOD = ThinLogBlock.thinLogGroup(MaterialColor.TERRACOTTA_RED, MaterialColor.COLOR_RED).nonFlammable().build("red_corin");
    public static final WoodGroup DYING_CORIN_WOOD = ThinLogBlock.thinLogGroup(MaterialColor.COLOR_GRAY, MaterialColor.TERRACOTTA_BLACK).nonFlammable().build("dying_corin");
    public static final RegistryObject<Block> ABERYTE_STONE = register("aberyte_stone", () -> new Block(mineable(Material.STONE, ToolType.PICKAXE, 0, 2f, 6f, SoundType.STONE)));
    public static final StoneGroup ABERYTE_COBBLESTONE = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 2f, 6f, SoundType.STONE)).build("aberyte_cobblestone");
    public static final StoneGroup POLISHED_ABERYTE_STONE = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 2f, 6f, SoundType.STONE)).pressurePlateAndButton(PressurePlateBlock.Sensitivity.MOBS).build("polished_aberyte_stone");
    public static final StoneGroup ABERYTE_STONE_BRICKS = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 2f, 6f, SoundType.NETHER_BRICKS)).cracked().chiseled().build("aberyte_stone_bricks");
    public static final StoneGroup WHITE_SANDSTONE = StoneGroup.base(() -> mineable(Material.STONE, ToolType.PICKAXE, 0, 0.8f, SoundType.STONE)).chiseled().build("white_sandstone");
    public static final RegistryObject<Block> CUT_WHITE_SANDSTONE = register("cut_white_sandstone", () -> new Block(mineable(Material.STONE, ToolType.PICKAXE, 0, 0.8f, SoundType.STONE)));
    public static final RegistryObject<Block> CUT_WHITE_SANDSTONE_SLAB = register("cut_white_sandstone_slab", () -> new SlabBlock(mineable(Material.STONE, ToolType.PICKAXE, 0, 2f, 6f, SoundType.STONE)));
    public static final RegistryObject<Block> LUMA_KELP = register("luma_kelp", () -> new WaterGrowingPlantBlock(properties(Material.WATER_PLANT, SoundType.WET_GRASS).noCollission().randomTicks().instabreak(), Direction.UP, 25, 0.14, WRBlocks.LUMA_KELP_BODY), extend().cutoutRenderer());
    public static final RegistryObject<Block> LUMA_KELP_BODY = register("luma_kelp_body", () -> new WaterGrowingPlantBodyBlock(properties(Material.WATER_PLANT, SoundType.WET_GRASS).noCollission().randomTicks().instabreak(), LUMA_KELP), extend().noItem().cutoutRenderer());
    public static final RegistryObject<Block> DEAD_LUCENT_CORAL = register("dead_lucent_coral", () -> new DeadCoralPlantBlock(Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).noCollission().instabreak()), extend().cutoutRenderer());
    public static final RegistryObject<Block> BLUE_LUCENT_CORAL = register("blue_lucent_coral", () -> new CoralPlantBlock(DEAD_LUCENT_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> CYAN_LUCENT_CORAL = register("cyan_lucent_coral", () -> new CoralPlantBlock(DEAD_LUCENT_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_CYAN).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> ORANGE_LUCENT_CORAL = register("orange_lucent_coral", () -> new CoralPlantBlock(DEAD_LUCENT_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> PURPLE_LUCENT_CORAL = register("purple_lucent_coral", () -> new CoralPlantBlock(DEAD_LUCENT_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> RED_LUCENT_CORAL = register("red_lucent_coral", () -> new CoralPlantBlock(DEAD_LUCENT_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> DEAD_RUIN_CORAL = register("dead_ruin_coral", () -> new DeadCoralPlantBlock(Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).noCollission().instabreak()), extend().cutoutRenderer());
    public static final RegistryObject<Block> BLUE_RUIN_CORAL = register("blue_ruin_coral", () -> new CoralPlantBlock(DEAD_RUIN_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> CYAN_RUIN_CORAL = register("cyan_ruin_coral", () -> new CoralPlantBlock(DEAD_RUIN_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_CYAN).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> ORANGE_RUIN_CORAL = register("orange_ruin_coral", () -> new CoralPlantBlock(DEAD_RUIN_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> PURPLE_RUIN_CORAL = register("purple_ruin_coral", () -> new CoralPlantBlock(DEAD_RUIN_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> RED_RUIN_CORAL = register("red_ruin_coral", () -> new CoralPlantBlock(DEAD_RUIN_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> DEAD_SPIRE_CORAL = register("dead_spire_coral", () -> new DeadCoralPlantBlock(Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).noCollission().instabreak()), extend().cutoutRenderer());
    public static final RegistryObject<Block> BLUE_SPIRE_CORAL = register("blue_spire_coral", () -> new CoralPlantBlock(DEAD_SPIRE_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> CYAN_SPIRE_CORAL = register("cyan_spire_coral", () -> new CoralPlantBlock(DEAD_SPIRE_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_CYAN).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> ORANGE_SPIRE_CORAL = register("orange_spire_coral", () -> new CoralPlantBlock(DEAD_SPIRE_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> PURPLE_SPIRE_CORAL = register("purple_spire_coral", () -> new CoralPlantBlock(DEAD_SPIRE_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> RED_SPIRE_CORAL = register("red_spire_coral", () -> new CoralPlantBlock(DEAD_SPIRE_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> DEAD_TURED_CORAL = register("dead_tured_coral", () -> new DeadCoralPlantBlock(Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).noCollission().instabreak()), extend().cutoutRenderer());
    public static final RegistryObject<Block> ORANGE_TURED_CORAL = register("orange_tured_coral", () -> new CoralPlantBlock(DEAD_TURED_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> RED_TURED_CORAL = register("red_tured_coral", () -> new CoralPlantBlock(DEAD_TURED_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> DEAD_VELVET_CORAL = register("dead_velvet_coral", () -> new DeadCoralPlantBlock(Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).noCollission().instabreak()), extend().cutoutRenderer());
    public static final RegistryObject<Block> ORANGE_VELVET_CORAL = register("orange_velvet_coral", () -> new CoralPlantBlock(DEAD_VELVET_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());
    public static final RegistryObject<Block> RED_VELVET_CORAL = register("red_velvet_coral", () -> new CoralPlantBlock(DEAD_VELVET_CORAL.get(), Properties.of(Material.STONE, MaterialColor.COLOR_RED).noCollission().instabreak().sound(SoundType.WET_GRASS)), extend().cutoutRenderer());

    // ashen desert
    public static final RegistryObject<Block> ASH_STONE = register("ashstone", () -> new Block(properties(Material.STONE, SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6f)));
    public static final RegistryObject<Block> CHISELED_ASH_STONE = register("chiseled_ashstone", () -> new Block(properties(Material.STONE, SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6f)));
    public static final RegistryObject<Block> CUT_ASH_STONE = register("cut_ashstone", () -> new Block(properties(Material.STONE, SoundType.STONE).requiresCorrectToolForDrops().strength(1.5f, 6f)));
    public static final RegistryObject<Block> CANIS_ROOT = register("canis_root", CanisRootBlock::new, extend().cutoutRenderer().item(b -> new BlockItem(b, new Item.Properties().tab(BLOCKS_ITEM_GROUP).food(WRItems.food(1, 0.6f).build()))));
    public static final RegistryObject<Block> ASH_BLOCK = register("ash_block", () -> new SandBlock(0x2D2A33, properties(Material.SAND, SoundType.SAND).strength(0.75f)));
    public static final RegistryObject<Block> FINE_ASH = register("fine_ash", () -> new SandBlock(0x2D2A33, properties(Material.SAND, SoundType.SAND).strength(0.5f)));
    public static final RegistryObject<Block> ASH = register("ash", () -> new SnowBlock(properties(Material.SAND, SoundType.SAND).strength(0.1f)));
    public static final RegistryObject<Block> EMBER_BLOCK = register("ember_block", EmberBlock::new);
    public static final RegistryObject<Block> EMBERS = register("embers", EmberLayerBlock::new);

    public static RegistryObject<Block> register(String name, Supplier<Block> block)
    {
        return register(name, block, extend());
    }

    public static RegistryObject<Block> register(String name, Supplier<Block> block, BlockExtension extension)
    {
        RegistryObject<Block> delegate = REGISTRY.register(name, block);
        if (extension.itemFactory != null) WRItems.register(name, () -> extension.itemFactory.apply(delegate.get()));
        if (extension.requiresSetup())
        {
            extension.block = delegate;
            EXTENSIONS.add(extension);
        }
        return delegate;
    }

    public static Properties properties(Material material, SoundType sound)
    {
        return Properties
                .of(material)
                .sound(sound);
    }

    public static Properties properties(Material material, MaterialColor color, SoundType sound)
    {
        return Properties
                .of(material, color)
                .sound(sound);
    }

    public static Properties plant()
    {
        return properties(Material.PLANT, SoundType.GRASS).noCollission();
    }

    public static Properties replaceablePlant()
    {
        return properties(Material.REPLACEABLE_PLANT, SoundType.GRASS).noCollission();
    }

    public static Properties leaves()
    {
        return properties(Material.LEAVES, SoundType.GRASS)
                .strength(0.2f)
                .randomTicks()
                .noOcclusion()
                .isValidSpawn((s, r, p, e) -> false)
                .isSuffocating((s, r, p) -> false)
                .isViewBlocking((s, r, p) -> false);
    }

    public static Properties mineable(Material material, ToolType harvestTool, int harvestLevel, float hardnessResistance, SoundType sound)
    {
        return Properties
                .of(material)
                .harvestTool(harvestTool)
                .harvestLevel(harvestLevel)
                .requiresCorrectToolForDrops()
                .strength(hardnessResistance)
                .sound(sound);
    }

    public static Properties mineable(Material material, ToolType harvestTool, int harvestLevel, float hardness, float resistance, SoundType sound)
    {
        return Properties
                .of(material)
                .harvestTool(harvestTool)
                .harvestLevel(harvestLevel)
                .requiresCorrectToolForDrops()
                .strength(hardness, resistance)
                .sound(sound);
    }

    public static class Tags
    {
        public static final Map<INamedTag<Block>, INamedTag<Item>> ITEM_BLOCK_TAGS = new HashMap<>();

        public static final INamedTag<Block> ORES_GEODE = forge("ores/geode");
        public static final INamedTag<Block> ORES_PLATINUM = forge("ores/platinum");

        public static final INamedTag<Block> STORAGE_BLOCKS_GEODE = forge("storage_blocks/geode");
        public static final INamedTag<Block> STORAGE_BLOCKS_PLATINUM = forge("storage_blocks/platinum");
        public static final INamedTag<Block> OSERI_LOGS = tag("oseri_logs");
        public static final INamedTag<Block> SAL_LOGS = tag("sal_logs");
        public static final INamedTag<Block> PRISMARINE_CORIN_LOGS = tag("corin_logs");
        public static final INamedTag<Block> SILVER_CORIN_LOGS = tag("silver_corin_logs");
        public static final INamedTag<Block> TEAL_CORIN_LOGS = tag("teal_corin_logs");
        public static final INamedTag<Block> RED_CORIN_LOGS = tag("red_corin_logs");
        public static final INamedTag<Block> DYING_CORIN_LOGS = tag("dying_corin_logs");

        static INamedTag<Block> forge(String path)
        {
            return getFor("forge:" + path);
        }

        public static INamedTag<Block> tag(String path)
        {
            return getFor(Wyrmroost.MOD_ID + ":" + path);
        }

        public static INamedTag<Block> getFor(String path)
        {
            INamedTag<Block> tag = BlockTags.bind(path);
            ITEM_BLOCK_TAGS.put(tag, ItemTags.bind(path));
            return tag;
        }

        public static INamedTag<Item> getItemTagFor(INamedTag<Block> blockTag)
        {
            return ITEM_BLOCK_TAGS.get(blockTag);
        }
    }

    public static BlockExtension extend()
    {
        return new BlockExtension();
    }

    public static int grassTint(BlockState state, @Nullable IBlockDisplayReader level, @Nullable BlockPos pos, int tint)
    {
        return level == null || pos == null? GrassColors.get(0.5, 1) : BiomeColors.getAverageGrassColor(level, pos);
    }

    public static class BlockExtension
    {
        RegistryObject<Block> block;
        Function<Block, BlockItem> itemFactory = b -> new BlockItem(b, new Item.Properties().tab(BLOCKS_ITEM_GROUP));
        Supplier<Supplier<RenderType>> renderType;
        IBlockTint tintFunc;
        int[] flammability;

        public BlockExtension item(Function<Block, BlockItem> factory)
        {
            this.itemFactory = factory;
            return this;
        }

        public BlockExtension noItem()
        {
            return item(null);
        }

        public BlockExtension render(Supplier<Supplier<RenderType>> renderSupplier)
        {
            this.renderType = renderSupplier;
            return this;
        }

        public BlockExtension cutoutRenderer()
        {
            return render(() -> RenderType::cutout);
        }

        public BlockExtension tint(IBlockTint tint)
        {
            this.tintFunc = tint;
            return this;
        }

        public BlockExtension flammability(int spread, int destroyRate)
        {
            this.flammability = new int[]{spread, destroyRate};
            return this;
        }

        private boolean requiresSetup()
        {
            return renderType != null || flammability != null || tintFunc != null;
        }

        public void callBack()
        {
            if (ModUtils.isClient())
            {
                if (renderType != null) RenderTypeLookup.setRenderLayer(block.get(), renderType.get().get());
                if (tintFunc != null)
                {
                    Block b = block.get();
                    ClientEvents.getClient().getBlockColors().register(tintFunc::getTint, b);
                    ClientEvents.getClient().getItemColors().register((s, i) -> tintFunc.getTint(b.defaultBlockState(), null, null, i), b.asItem());
                }
            }
            if (flammability != null)
                ((FireBlock) Blocks.FIRE).setFlammable(block.get(), flammability[0], flammability[1]);
        }
    }

    /**
     * Small helper interface to avoid having to double-wrap suppliers
     */
    public interface IBlockTint
    {
        /**
         * @param state     The BlockState of the block we're tinting for
         * @param level     Nullable: possible to get the world from the client but not recommended; could be in ITEM context.
         * @param pos       Nullable: could be in ITEM context.
         * @param tintIndex the index of the tint.
         */
        int getTint(BlockState state, @Nullable IBlockDisplayReader level, @Nullable BlockPos pos, int tintIndex);
    }
}