package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.blocks.base.EXPBlock;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public class WRBlocks
{
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", new Block(builder(Material.ROCK).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", new Block(builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", new EXPBlock(3, 7, builder(Material.ROCK).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", new Block(builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", new EXPBlock(4, 8, builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", new Block(builder(Material.ROCK).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", new EXPBlock(8, 11, builder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", new Block(builder(Material.ROCK).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static RegistryObject<Block> register(String name, Block block)
    {
        return register(name, block, () -> new BlockItem(block, WRItems.builder()));
    }

    public static RegistryObject<Block> register(String name, Block block, Supplier<Item> itemBlock)
    {
        WRItems.register(name, itemBlock);
        return REGISTRY.register(name, () -> block);
    }

    public static Block.Properties builder(Material material)
    {
        Block.Properties properties = Block.Properties.create(material);
        if (material == Material.WOOD) properties.harvestTool(ToolType.AXE).hardnessAndResistance(2).sound(SoundType.WOOD);
        else if (material == Material.ROCK) properties.harvestTool(ToolType.PICKAXE);
        else if (material == Material.SAND) properties.harvestTool(ToolType.SHOVEL).sound(SoundType.SAND);
        return properties;
    }

    public static class WRTags
    {
        public static final Map<Tags.IOptionalNamedTag<Block>, Tags.IOptionalNamedTag<Item>> ITEM_BLOCK_TAGS = Maps.newHashMap();

        public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_GEODE = tag(new ResourceLocation("forge", "storage_blocks/geode"));
        public static final Tags.IOptionalNamedTag<Block> STORAGE_BLOCKS_PLATINUM = tag(new ResourceLocation("forge", "storage_blocks/platinum"));

        public static Tags.IOptionalNamedTag<Block> tag(String name) { return tag(Wyrmroost.rl(name));}

        public static Tags.IOptionalNamedTag<Block> tag(ResourceLocation name)
        {
            Tags.IOptionalNamedTag<Block> tag = BlockTags.createOptional(name);
            ITEM_BLOCK_TAGS.put(tag, ItemTags.createOptional(name));
            return tag;
        }
    }
}