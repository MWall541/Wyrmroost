package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.blocks.EXPBlock;
import com.github.wolfshotz.wyrmroost.blocks.GillaBushBlock;
import com.github.wolfshotz.wyrmroost.blocks.HangingPlantBodyBlock;
import com.github.wolfshotz.wyrmroost.blocks.SilverMossBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
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
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", () -> new Block(builder(Material.ROCK).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", () -> new Block(builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", () -> new EXPBlock(3, 7, builder(Material.ROCK).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", () -> new Block(builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", () -> new EXPBlock(4, 8, builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", () -> new Block(builder(Material.ROCK).harvestLevel(3).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", () -> new EXPBlock(8, 11, builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", () -> new Block(builder(Material.ROCK).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SILVER_MOSS_BODY = register("silver_moss_body", () -> new HangingPlantBodyBlock(builder(Material.PLANTS).zeroHardnessAndResistance(), WRBlocks.SILVER_MOSS), null);
    public static final RegistryObject<Block> SILVER_MOSS = register("silver_moss", SilverMossBlock::new);
    public static final RegistryObject<Block> GILLA = register("gilla", GillaBushBlock::new);

    public static RegistryObject<Block> register(String name, Supplier<Block> block)
    {
        return register(name, block, b -> new BlockItem(b, WRItems.builder()));
    }

    public static RegistryObject<Block> register(String name, Supplier<Block> block, @Nullable Function<Block, BlockItem> blockItem)
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
        public static final Map<IOptionalNamedTag<Block>, IOptionalNamedTag<Item>> ITEM_BLOCK_TAGS = new HashMap<>();

        public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_GEODE = tag(new ResourceLocation("forge", "storage_blocks/geode"));
        public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_PLATINUM = tag(new ResourceLocation("forge", "storage_blocks/platinum"));

        public static IOptionalNamedTag<Block> tag(String name)
        {
            return tag(Wyrmroost.rl(name));
        }

        public static IOptionalNamedTag<Block> tag(ResourceLocation name)
        {
            IOptionalNamedTag<Block> tag = BlockTags.createOptional(name);
            ITEM_BLOCK_TAGS.put(tag, ItemTags.createOptional(name));
            return tag;
        }
    }
}