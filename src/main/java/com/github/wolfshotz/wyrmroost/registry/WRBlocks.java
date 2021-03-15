package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.*;
import com.github.wolfshotz.wyrmroost.world.features.OseriTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class WRBlocks
{
    static final ItemGroup BLOCKS_ITEM_GROUP = new ItemGroup("wyrmroost_dimension")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(PURPLE_GEODE_BLOCK.get());
        }
    };

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", () -> new Block(mineable(Material.STONE, ToolType.PICKAXE, 1, 3f, SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 1, 5f, SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", () -> new EXPBlock(3, 7, mineable(Material.STONE, ToolType.PICKAXE, 2, 3f, SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 2, 5f, SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", () -> new EXPBlock(4, 8, mineable(Material.STONE, ToolType.PICKAXE, 3, 3f, SoundType.NETHER_ORE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 3, 5f, SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", () -> new EXPBlock(8, 11, mineable(Material.METAL, ToolType.PICKAXE, 4, 5f, SoundType.GILDED_BLACKSTONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", () -> new Block(mineable(Material.METAL, ToolType.PICKAXE, 4, 7f, SoundType.METAL)));

    // tincture weald
    public static final RegistryObject<Block> MULCH = register("mulch", () -> new SnowyDirtBlock(properties(Material.SOIL, SoundType.GRAVEL).strength(0.5f).harvestTool(ToolType.SHOVEL)));
    public static final RegistryObject<Block> SILVER_MOSS = register("silver_moss", () -> new GrowingPlantBlock(plant().ticksRandomly(), Direction.DOWN, 2, WRBlocks.SILVER_MOSS_BODY));
    public static final RegistryObject<Block> SILVER_MOSS_BODY = register("silver_moss_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.SILVER_MOSS), null);
    public static final RegistryObject<Block> GILLA = register("gilla", GillaBushBlock::new);
    public static final RegistryObject<Block> MOSS_VINE = register("moss_vine", () -> new VineBlock(properties(Material.REPLACEABLE_PLANT, SoundType.VINE).ticksRandomly().noCollision().strength(0.2f)));
    public static final RegistryObject<Block> BLUE_OSERI_SAPLING = register("blue_oseri_sapling", () -> new SaplingBlock(new OseriTree(OseriTree.Type.BLUE), plant().ticksRandomly()));
    public static final RegistryObject<Block> BLUE_OSERI_LEAVES = register("blue_oseri_leaves", () -> new OseriLeaves(leaves()));
    public static final RegistryObject<Block> BLUE_OSERI_VINES = register("blue_oseri_vines", () -> new GrowingPlantBlock(plant().ticksRandomly(), Direction.DOWN, 0, WRBlocks.BLUE_OSERI_VINES_BODY));
    public static final RegistryObject<Block> BLUE_OSERI_VINES_BODY = register("blue_oseri_vines_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.BLUE_OSERI_VINES), null);
    public static final RegistryObject<Block> BLUE_OSERI_PETALS = register("blue_oseri_petals", () -> new PetalsBlock(plant()));
    public static final RegistryObject<Block> GOLD_OSERI_SAPLING = register("gold_oseri_sapling", () -> new SaplingBlock(new OseriTree(OseriTree.Type.GOLD), plant().ticksRandomly()));
    public static final RegistryObject<Block> GOLD_OSERI_LEAVES = register("gold_oseri_leaves", () -> new OseriLeaves(leaves()));
    public static final RegistryObject<Block> GOLD_OSERI_VINES = register("gold_oseri_vines", () -> new GrowingPlantBlock(plant().ticksRandomly(), Direction.DOWN, 0, WRBlocks.GOLD_OSERI_VINES_BODY));
    public static final RegistryObject<Block> GOLD_OSERI_VINES_BODY = register("gold_oseri_vines_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.GOLD_OSERI_VINES), null);
    public static final RegistryObject<Block> GOLD_OSERI_PETALS = register("gold_oseri_petals", () -> new PetalsBlock(plant()));
    public static final RegistryObject<Block> PINK_OSERI_SAPLING = register("pink_oseri_sapling", () -> new SaplingBlock(new OseriTree(OseriTree.Type.PINK), plant().ticksRandomly()));
    public static final RegistryObject<Block> PINK_OSERI_LEAVES = register("pink_oseri_leaves", () -> new OseriLeaves(leaves()));
    public static final RegistryObject<Block> PINK_OSERI_VINES = register("pink_oseri_vines", () -> new GrowingPlantBlock(plant().ticksRandomly(), Direction.DOWN, 0, WRBlocks.PINK_OSERI_VINES_BODY));
    public static final RegistryObject<Block> PINK_OSERI_VINES_BODY = register("pink_oseri_vines_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.PINK_OSERI_VINES), null);
    public static final RegistryObject<Block> PINK_OSERI_PETALS = register("pink_oseri_petals", () -> new PetalsBlock(plant()));
    public static final RegistryObject<Block> PURPLE_OSERI_SAPLING = register("purple_oseri_sapling", () -> new SaplingBlock(new OseriTree(OseriTree.Type.PURPLE), plant().ticksRandomly()));
    public static final RegistryObject<Block> PURPLE_OSERI_LEAVES = register("purple_oseri_leaves", () -> new OseriLeaves(leaves()));
    public static final RegistryObject<Block> PURPLE_OSERI_VINES = register("purple_oseri_vines", () -> new GrowingPlantBlock(plant().ticksRandomly(), Direction.DOWN, 0, WRBlocks.PURPLE_OSERI_VINES_BODY));
    public static final RegistryObject<Block> PURPLE_OSERI_VINES_BODY = register("purple_oseri_vines_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.PURPLE_OSERI_VINES), null);
    public static final RegistryObject<Block> PURPLE_OSERI_PETALS = register("purple_oseri_petals", () -> new PetalsBlock(plant()));
    public static final RegistryObject<Block> WHITE_OSERI_SAPLING = register("white_oseri_sapling", () -> new SaplingBlock(new OseriTree(OseriTree.Type.WHITE), plant().ticksRandomly()));
    public static final RegistryObject<Block> WHITE_OSERI_LEAVES = register("white_oseri_leaves", () -> new OseriLeaves(leaves()));
    public static final RegistryObject<Block> WHITE_OSERI_VINES = register("white_oseri_vines", () -> new GrowingPlantBlock(plant().ticksRandomly(), Direction.DOWN, 0, WRBlocks.WHITE_OSERI_VINES_BODY));
    public static final RegistryObject<Block> WHITE_OSERI_VINES_BODY = register("white_oseri_vines_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.WHITE_OSERI_VINES), null);
    public static final RegistryObject<Block> WHITE_OSERI_PETALS = register("white_oseri_petals", () -> new PetalsBlock(plant()));
    public static final WoodGroup OSERI_WOOD = new WoodGroup("oseri", MaterialColor.SAND, MaterialColor.STONE);

    static RegistryObject<Block> register(String name, Supplier<Block> block)
    {
        return register(name, block, b -> new BlockItem(b, new Item.Properties().group(BLOCKS_ITEM_GROUP)));
    }

    static RegistryObject<Block> register(String name, Supplier<Block> block, @Nullable Function<Block, BlockItem> blockItem)
    {
        RegistryObject<Block> reg = REGISTRY.register(name, block);
        if (blockItem != null) WRItems.register(name, () -> blockItem.apply(reg.get()));
        return reg;
    }

    public static AbstractBlock.Properties properties(Material material, SoundType sound)
    {
        return AbstractBlock.Properties
                .of(material)
                .sounds(sound);
    }

    public static AbstractBlock.Properties plant()
    {
        return properties(Material.LEAVES, SoundType.GRASS).noCollision();
    }

    public static AbstractBlock.Properties leaves()
    {
        return properties(Material.LEAVES, SoundType.GRASS)
                .strength(0.2f)
                .ticksRandomly()
                .nonOpaque()
                .allowsSpawning((s, r, p, e) -> false)
                .suffocates((s, r, p) -> false)
                .blockVision((s, r, p) -> false);
    }

    public static AbstractBlock.Properties mineable(Material material, ToolType harvestTool, int harvestLevel, float hardnessResistance, SoundType sound)
    {
        return AbstractBlock.Properties
                .of(material)
                .harvestTool(harvestTool)
                .harvestLevel(harvestLevel)
                .requiresTool()
                .strength(hardnessResistance)
                .sounds(sound);
    }

    public static class Tags
    {
        public static final Map<INamedTag<Block>, INamedTag<Item>> ITEM_BLOCK_TAGS = new HashMap<>();

        public static final INamedTag<Block> ORES_GEODE = forge("ores/geode");
        public static final INamedTag<Block> ORES_PLATINUM = forge("ores/platinum");

        public static final INamedTag<Block> STORAGE_BLOCKS_GEODE = forge("storage_blocks/geode");
        public static final INamedTag<Block> STORAGE_BLOCKS_PLATINUM = forge("storage_blocks/platinum");
        public static final INamedTag<Block> OSERI_LOGS = tag("oseri_logs");

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
            INamedTag<Block> tag = BlockTags.register(path);
            ITEM_BLOCK_TAGS.put(tag, ItemTags.register(path));
            return tag;
        }

        public static INamedTag<Item> getItemTagFor(INamedTag<Block> blockTag)
        {
            return ITEM_BLOCK_TAGS.get(blockTag);
        }
    }

    public static class WoodGroup extends WoodType
    {
        final RegistryObject<Block> planks;
        final RegistryObject<Block> log;
        final RegistryObject<Block> strippedLog;
        final RegistryObject<Block> wood;
        final RegistryObject<Block> strippedWood;
        final RegistryObject<Block> slab;
        final RegistryObject<Block> pressurePlate;
        final RegistryObject<Block> fence;
        final RegistryObject<Block> fenceGate;
        final RegistryObject<Block> trapDoor;
        final RegistryObject<Block> stairs;
        final RegistryObject<Block> button;
        final RegistryObject<Block> door;
        final RegistryObject<Block> sign;
        final RegistryObject<Block> wallSign;

        public WoodGroup(String name, MaterialColor color, MaterialColor logColor)
        {
            super(name);
            WoodType.register(this);

            this.planks = WRBlocks.register(name + "_planks", () -> new Block(props(color)));
            this.log = WRBlocks.register(name + "_log", () -> new LogBlock(color, logColor, self().strippedLog));
            this.strippedLog = WRBlocks.register("stripped_" + name + "_log", () -> new RotatedPillarBlock(LogBlock.properties(color, logColor)));
            this.wood = WRBlocks.register(name + "_wood", () -> new LogBlock(logColor, logColor, self().strippedWood));
            this.strippedWood = WRBlocks.register("stripped_" + name + "_wood", () -> new RotatedPillarBlock(LogBlock.properties(color, color)));
            this.slab = WRBlocks.register(name + "_slab", () -> new SlabBlock(props(color)));
            this.pressurePlate = WRBlocks.register(name + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, props(color).noCollision().strength(0.5f)));
            this.fence = WRBlocks.register(name + "_fence", () -> new FenceBlock(props(color)));
            this.fenceGate = WRBlocks.register(name + "_fence_gate", () -> new FenceGateBlock(props(color)));
            this.trapDoor = WRBlocks.register(name + "_trapdoor", () -> new TrapDoorBlock(props(color).strength(3f).nonOpaque().allowsSpawning((s, r, p, e) -> false)));
            this.stairs = WRBlocks.register(name + "_stairs", () -> new StairsBlock(() -> getPlanks().getDefaultState(), props(color)));
            this.button = WRBlocks.register(name + "_button", () -> new WoodButtonBlock(AbstractBlock.Properties.of(Material.SUPPORTED).noCollision().harvestTool(ToolType.AXE).strength(0.5f).sounds(SoundType.WOOD)));
            this.door = WRBlocks.register(name + "_door", () -> new DoorBlock(props(color).strength(3f).nonOpaque()));
            this.wallSign = WRBlocks.register(name + "_wall_sign", () -> new WRSignBlock.Wall(props(color).noCollision().strength(1f).lootFrom(self().sign), this), null);
            this.sign = WRBlocks.register(name + "_sign", () -> new WRSignBlock(props(color).noCollision().strength(1f), this), b -> new SignItem(new Item.Properties().maxCount(16).group(BLOCKS_ITEM_GROUP), b, getWallSign()));
        }

        public Block getPlanks()
        {
            return planks.get();
        }

        public Block getLog()
        {
            return log.get();
        }

        public Block getStrippedLog()
        {
            return strippedLog.get();
        }

        public Block getWood()
        {
            return wood.get();
        }

        public Block getStrippedWood()
        {
            return strippedWood.get();
        }

        public Block getSlab()
        {
            return slab.get();
        }

        public Block getPressurePlate()
        {
            return pressurePlate.get();
        }

        public Block getFence()
        {
            return fence.get();
        }

        public Block getFenceGate()
        {
            return fenceGate.get();
        }

        public Block getTrapDoor()
        {
            return trapDoor.get();
        }

        public Block getStairs()
        {
            return stairs.get();
        }

        public Block getButton()
        {
            return button.get();
        }

        public Block getDoor()
        {
            return door.get();
        }

        public Block getSign()
        {
            return sign.get();
        }

        public Block getWallSign()
        {
            return wallSign.get();
        }

        private WoodGroup self()
        {
            return this;
        }

        private static AbstractBlock.Properties props(MaterialColor color)
        {
            return AbstractBlock.Properties
                    .of(Material.WOOD, color)
                    .strength(2f, 3f)
                    .harvestTool(ToolType.AXE)
                    .sounds(SoundType.WOOD);
        }
    }
}