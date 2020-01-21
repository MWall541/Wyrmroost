package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.PortalBlock;
import WolfShotz.Wyrmroost.content.blocks.base.EXPBlock;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WRBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<Block> PORTAL_BLOCK = register("wyrmroost_portal", new PortalBlock());
    
    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", new Block(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", new Block(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)));
    
    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", new EXPBlock(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 3, 7)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", new Block(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", new EXPBlock(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 4, 8)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", new Block(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", new EXPBlock(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 8, 11)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", new Block(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.METAL)));
    
    public static final RegistryObject<Block> CANARI_LEAVES = register("canari_leaves", new LeavesBlock(ModUtils.blockBuilder(Material.LEAVES).hardnessAndResistance(0.2f).sound(SoundType.PLANT).tickRandomly()));
    public static final RegistryObject<Block> CANARI_WOOD = register("canari_wood", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD).hardnessAndResistance(0.2f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_CANARI = register("stripped_canari_wood", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD).hardnessAndResistance(0.2f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> ASH_BLOCK = register("ash_block", new SandBlock(0x575757, ModUtils.blockBuilder(Material.SAND).hardnessAndResistance(0.5f).sound(SoundType.SAND)));
    
    private static RegistryObject<Block> register(String name, Block block)
    {
        WRItems.register(name, new BlockItem(block, ModUtils.itemBuilder()));
        return BLOCKS.register(name, () -> block);
    }
    
    public static class Tags
    {
        public static final Tag<Block> STORAGE_BLOCKS_GEODE = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/geode"));
        
        public static Tag<Block> tag(String name) { return new BlockTags.Wrapper(ModUtils.resource(name)); }
    }
}