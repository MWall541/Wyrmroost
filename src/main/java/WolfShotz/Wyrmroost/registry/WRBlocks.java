package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.PortalBlock;
import WolfShotz.Wyrmroost.content.blocks.base.EXPBlock;
import WolfShotz.Wyrmroost.content.blocks.base.LogBlockBase;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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
    
    public static final RegistryObject<Block> PLATINUM_ORE = register("platinum_ore", new Block(ModUtils.blockBuilder(Material.ROCK).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK = register("platinum_block", new Block(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)));
    
    public static final RegistryObject<Block> BLUE_GEODE_ORE = register("blue_geode_ore", new EXPBlock(ModUtils.blockBuilder(Material.ROCK).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 3, 7)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK = register("blue_geode_block", new Block(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> RED_GEODE_ORE = register("red_geode_ore", new EXPBlock(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 4, 8)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK = register("red_geode_block", new Block(ModUtils.blockBuilder(Material.ROCK).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURPLE_GEODE_ORE = register("purple_geode_ore", new EXPBlock(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 8, 11)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK = register("purple_geode_block", new Block(ModUtils.blockBuilder(Material.ROCK).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.METAL)));
    public static final RegistryObject<Block> BLUE_CRYSTAL = register("blue_crystal", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(0.5f).sound(SoundType.GLASS)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> BLUE_CRYSTAL_ORE = register("blue_crystal_ore", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(2f).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> BLUE_CRYSTAL_BLOCK = register("blue_crystal_block", new Block(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(1).sound(SoundType.GLASS)));
    public static final RegistryObject<Block> GREEN_CRYSTAL = register("green_crystal", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(0.5f).sound(SoundType.GLASS)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> GREEN_CRYSTAL_ORE = register("green_crystal_ore", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(2f).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> GREEN_CRYSTAL_BLOCK = register("green_crystal_block", new Block(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(1).sound(SoundType.GLASS)));
    public static final RegistryObject<Block> ORANGE_CRYSTAL = register("orange_crystal", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(0.5f).sound(SoundType.GLASS)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> ORANGE_CRYSTAL_ORE = register("orange_crystal_ore", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(2f).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> ORANGE_CRYSTAL_BLOCK = register("orange_crystal_block", new Block(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(1).sound(SoundType.GLASS)));
    public static final RegistryObject<Block> YELLOW_CRYSTAL = register("yellow_crystal", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(0.5f).sound(SoundType.GLASS)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> YELLOW_CRYSTAL_ORE = register("yellow_crystal_ore", new EXPBlock(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(2f).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 5, 8)));
    public static final RegistryObject<Block> YELLOW_CRYSTAL_BLOCK = register("yellow_crystal_block", new Block(ModUtils.blockBuilder(Material.ICE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(1).sound(SoundType.GLASS)));

    public static final RegistryObject<Block> ASH = register("ash", new SandBlock(0x575757, ModUtils.blockBuilder(Material.SAND).hardnessAndResistance(0.5f)));
    public static final RegistryObject<Block> ASH_BLOCK = register("ash_block", new Block(ModUtils.blockBuilder(Material.MISCELLANEOUS).hardnessAndResistance(1).sound(SoundType.SAND)));
    public static final RegistryObject<Block> ASH_LOG = register("ash_log", new LogBlock(MaterialColor.BROWN, ModUtils.blockBuilder(Material.WOOD)));

    public static final RegistryObject<Block> STRIPPED_CANARI = register("stripped_canari_log", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> CANARI_LOG = register("canari_log", new LogBlockBase(MaterialColor.BROWN, WRBlocks.STRIPPED_CANARI.get(), ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> CANARI_WOOD = register("canari_wood", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> CANARI_PLANKS = register("canari_planks", new Block(ModUtils.blockBuilder(Material.WOOD).hardnessAndResistance(2, 3)));
    public static final RegistryObject<Block> CANARI_LEAVES = register("canari_leaves", new LeavesBlock(ModUtils.blockBuilder(Material.LEAVES).hardnessAndResistance(0.2f).sound(SoundType.PLANT).tickRandomly()));

    public static final RegistryObject<Block> STRIPPED_BLUE_CORIN_LOG = register("stripped_blue_corin_log", new LogBlock(MaterialColor.BLUE, ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> BLUE_CORIN_LOG = register("blue_corin_log", new LogBlockBase(MaterialColor.BLUE, WRBlocks.STRIPPED_BLUE_CORIN_LOG.get(), ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> BLUE_CORIN_WOOD = register("blue_corin_wood", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> BLUE_CORIN_PLANKS = register("blue_corin_planks", new Block(ModUtils.blockBuilder(Material.WOOD).hardnessAndResistance(2, 3)));

    public static final RegistryObject<Block> STRIPPED_TEAL_CORIN_LOG = register("stripped_teal_corin_log", new LogBlock(MaterialColor.BLUE, ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> TEAL_CORIN_LOG = register("teal_corin_log", new LogBlockBase(MaterialColor.BLUE, WRBlocks.STRIPPED_TEAL_CORIN_LOG.get(), ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> TEAL_CORIN_WOOD = register("teal_corin_wood", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> TEAL_CORIN_PLANKS = register("teal_corin_planks", new Block(ModUtils.blockBuilder(Material.WOOD).hardnessAndResistance(2, 3)));

    public static final RegistryObject<Block> STRIPPED_RED_CORIN_LOG = register("stripped_red_corin_log", new LogBlock(MaterialColor.BLUE, ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> RED_CORIN_LOG = register("red_corin_log", new LogBlockBase(MaterialColor.BLUE, WRBlocks.STRIPPED_RED_CORIN_LOG.get(), ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> RED_CORIN_WOOD = register("red_corin_wood", new RotatedPillarBlock(ModUtils.blockBuilder(Material.WOOD)));
    public static final RegistryObject<Block> RED_CORIN_PLANKS = register("red_corin_planks", new Block(ModUtils.blockBuilder(Material.WOOD).hardnessAndResistance(2, 3)));

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