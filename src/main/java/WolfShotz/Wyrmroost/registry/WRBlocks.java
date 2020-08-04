package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.blocks.base.EXPBlock;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public class WRBlocks
{
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);

    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", new Block(ModUtils.blockBuilder(Material.ROCK).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", new Block(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", new EXPBlock(r -> MathHelper.nextInt(r, 3, 7), ModUtils.blockBuilder(Material.ROCK).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", new Block(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", new EXPBlock(r -> MathHelper.nextInt(r, 4, 8), ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", new Block(ModUtils.blockBuilder(Material.ROCK).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", new EXPBlock(r -> MathHelper.nextInt(r, 8, 11), ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", new Block(ModUtils.blockBuilder(Material.ROCK).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.METAL)));

    public static RegistryObject<Block> register(String name, Block block)
    {
        return register(name, block, () -> new BlockItem(block, ModUtils.itemBuilder()));
    }

    public static RegistryObject<Block> register(String name, Block block, Supplier<Item> itemBlock)
    {
        WRItems.register(name, itemBlock);
        return REGISTRY.register(name, () -> block);
    }

    public static class Tags
    {
        public static final Map<Tag<Block>, Tag<Item>> ITEM_BLOCK_TAGS = Maps.newHashMap();

        public static final Tag<Block> STORAGE_BLOCKS_GEODE = tag(new ResourceLocation("forge", "storage_blocks/geode"));
        public static final Tag<Block> STORAGE_BLOCKS_PLATINUM = tag(new ResourceLocation("forge", "storage_blocks/platinum"));
//        public static final Tag<Block> CANARI_LOGS = tag(new ResourceLocation("logs/canari_logs"));
//        public static final Tag<Block> BLUE_CORIN_LOGS = tag(new ResourceLocation("logs/blue_corin_logs"));
//        public static final Tag<Block> TEAL_CORIN_LOGS = tag(new ResourceLocation("logs/teal_corin_logs"));
//        public static final Tag<Block> RED_CORIN_LOGS = tag(new ResourceLocation("logs/red_corin_logs"));

        public static Tag<Block> tag(String name) { return tag(Wyrmroost.rl(name));}

        public static Tag<Block> tag(ResourceLocation name)
        {
            Tag<Block> tag = new BlockTags.Wrapper(name);
            ITEM_BLOCK_TAGS.put(tag, new ItemTags.Wrapper(name));
            return tag;
        }
    }
}