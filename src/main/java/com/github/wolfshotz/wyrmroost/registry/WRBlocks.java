package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.EXPBlock;
import com.github.wolfshotz.wyrmroost.blocks.GillaBushBlock;
import com.github.wolfshotz.wyrmroost.blocks.GrowingPlantBlock;
import com.github.wolfshotz.wyrmroost.blocks.GrowingPlantBodyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.VineBlock;
import net.minecraft.block.material.Material;
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
    public static final RegistryObject<Block> SILVER_MOSS_BODY = register("silver_moss_body", () -> new GrowingPlantBodyBlock(builder(Material.PLANTS).zeroHardnessAndResistance(), WRBlocks.SILVER_MOSS), null);

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", () -> new Block(builder(Material.ROCK).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", () -> new Block(builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", () -> new EXPBlock(3, 7, builder(Material.ROCK).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", () -> new Block(builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", () -> new EXPBlock(4, 8, builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", () -> new Block(builder(Material.ROCK).harvestLevel(3).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", () -> new EXPBlock(8, 11, builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", () -> new Block(builder(Material.ROCK).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static final RegistryObject<Block> SILVER_MOSS = register("silver_moss", () -> new GrowingPlantBlock(builder(Material.PLANTS).zeroHardnessAndResistance().tickRandomly(), Direction.DOWN, 2, WRBlocks.SILVER_MOSS_BODY));
    static final ItemGroup BLOCKS_ITEM_GROUP = new ItemGroup("wyrmroostDimension")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(PURPLE_GEODE_BLOCK.get());
        }
    };
    public static final RegistryObject<Block> GILLA = register("gilla", GillaBushBlock::new);
    public static final RegistryObject<Block> MOSS_VINE = register("moss_vine", () -> new VineBlock(builder(Material.TALL_PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.2f).sound(SoundType.VINE)));

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

    public static Block.Properties builder(Material material)
    {
        Block.Properties properties = Block.Properties.create(material);
        if (material == Material.WOOD)
            properties.harvestTool(ToolType.AXE).hardnessAndResistance(2).sound(SoundType.WOOD);
        else if (material == Material.ROCK) properties.harvestTool(ToolType.PICKAXE);
        else if (material == Material.SAND) properties.harvestTool(ToolType.SHOVEL).sound(SoundType.SAND);
        else if (material == Material.PLANTS) properties.sound(SoundType.PLANT).doesNotBlockMovement();
        return properties;
    }

    public static class Tags
    {
        public static final Map<INamedTag<Block>, INamedTag<Item>> ITEM_BLOCK_TAGS = new HashMap<>();

        public static final INamedTag<Block> ORES_GEODE = forge("ores/geode");

        public static final INamedTag<Block> STORAGE_BLOCKS_GEODE = forge("storage_blocks/geode");
        public static final INamedTag<Block> STORAGE_BLOCKS_PLATINUM = forge("storage_blocks/platinum");

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
    }
}