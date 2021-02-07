package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.EXPBlock;
import com.github.wolfshotz.wyrmroost.blocks.GillaBushBlock;
import com.github.wolfshotz.wyrmroost.blocks.GrowingPlantBlock;
import com.github.wolfshotz.wyrmroost.blocks.GrowingPlantBodyBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", () -> new Block(mineable(Material.ROCK, ToolType.PICKAXE, 1, 3f, SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", () -> new Block(mineable(Material.IRON, ToolType.PICKAXE, 1, 5f, SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", () -> new EXPBlock(3, 7, mineable(Material.ROCK, ToolType.PICKAXE, 2, 3f, SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", () -> new Block(mineable(Material.IRON, ToolType.PICKAXE, 2, 5f, SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", () -> new EXPBlock(4, 8, mineable(Material.ROCK, ToolType.PICKAXE, 3, 3f, SoundType.NETHER_ORE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", () -> new Block(mineable(Material.IRON, ToolType.PICKAXE, 3, 5f, SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", () -> new EXPBlock(8, 11, mineable(Material.IRON, ToolType.PICKAXE, 4, 5f, SoundType.GILDED_BLACKSTONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", () -> new Block(mineable(Material.IRON, ToolType.PICKAXE, 4, 7f, SoundType.METAL)));

    public static final RegistryObject<Block> MULCH = register("mulch", () -> new SnowyDirtBlock(properties(Material.EARTH, SoundType.GROUND).hardnessAndResistance(0.5f).harvestTool(ToolType.SHOVEL)));
    public static final RegistryObject<Block> SILVER_MOSS = register("silver_moss", () -> new GrowingPlantBlock(plant().tickRandomly(), Direction.DOWN, 2, WRBlocks.SILVER_MOSS_BODY));
    public static final RegistryObject<Block> SILVER_MOSS_BODY = register("silver_moss_body", () -> new GrowingPlantBodyBlock(plant(), WRBlocks.SILVER_MOSS), null);
    public static final RegistryObject<Block> GILLA = register("gilla", GillaBushBlock::new);
    public static final RegistryObject<Block> MOSS_VINE = register("moss_vine", () -> new VineBlock(properties(Material.TALL_PLANTS, SoundType.VINE).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0.2f)));
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
                .create(material)
                .sound(sound);
    }

    public static AbstractBlock.Properties plant()
    {
        return properties(Material.PLANTS, SoundType.PLANT).doesNotBlockMovement();
    }

    public static AbstractBlock.Properties mineable(Material material, ToolType harvestTool, int harvestLevel, float hardnessResistance, SoundType sound)
    {
        return AbstractBlock.Properties
                .create(material)
                .harvestTool(harvestTool)
                .harvestLevel(harvestLevel)
                .setRequiresTool()
                .hardnessAndResistance(hardnessResistance)
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
            INamedTag<Block> tag = BlockTags.makeWrapperTag(path);
            ITEM_BLOCK_TAGS.put(tag, ItemTags.makeWrapperTag(path));
            return tag;
        }

        public static INamedTag<Item> getItemTagFor(INamedTag<Block> blockTag)
        {
            return ITEM_BLOCK_TAGS.get(blockTag);
        }
    }

    public static class WoodGroup
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
//        final RegistryObject<Block> sign; todo signs
//        final RegistryObject<Block> wallSign;

        public WoodGroup(String name, MaterialColor color, MaterialColor logColor)
        {
            this.planks = register(name + "_planks", () -> new Block(props(color)));
            this.log = register(name + "_log", createLogBlock(color, logColor));
            this.strippedLog = register("stripped_" + name + "_log", createLogBlock(color, color));
            this.wood = register(name + "_wood", createLogBlock(logColor, logColor));
            this.strippedWood = register("stripped_" + name + "_wood", createLogBlock(color, color));
            this.slab = register(name + "_slab", () -> new SlabBlock(props(color)));
            this.pressurePlate = register(name + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, props(color).doesNotBlockMovement().hardnessAndResistance(0.5f)));
            this.fence = register(name + "_fence", () -> new FenceBlock(props(color)));
            this.fenceGate = register(name + "_fence_gate", () -> new FenceGateBlock(props(color)));
            this.trapDoor = register(name + "_trapdoor", () -> new TrapDoorBlock(props(color).hardnessAndResistance(3f).notSolid().setAllowsSpawn((s, r, p, e) -> false)));
            this.stairs = register(name + "_stairs", () -> new StairsBlock(() -> getPlanks().getDefaultState(), props(color)));
            this.button = register(name + "_button", () -> new WoodButtonBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().harvestTool(ToolType.AXE).hardnessAndResistance(0.5f).sound(SoundType.WOOD)));
            this.door = register(name + "_door", () -> new DoorBlock(props(color).hardnessAndResistance(3f).notSolid()));
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

//        public Block getSign()
//        {
//            return sign.get();
//        }

//        public Block getWallSign()
//        {
//            return wallSign.get();
//        }

        private static AbstractBlock.Properties props(MaterialColor color)
        {
            return AbstractBlock.Properties
                    .create(Material.WOOD, color)
                    .hardnessAndResistance(2f, 3f)
                    .harvestTool(ToolType.AXE)
                    .sound(SoundType.WOOD);
        }

        private static Supplier<Block> createLogBlock(MaterialColor top, MaterialColor bark)
        {
            return () -> new RotatedPillarBlock(AbstractBlock.Properties
                    .create(Material.WOOD, state -> (top == bark || state.get(RotatedPillarBlock.AXIS) == Direction.Axis.Y)? top : bark)
                    .hardnessAndResistance(2f)
                    .harvestTool(ToolType.AXE)
                    .sound(SoundType.WOOD));
        }
    }
}