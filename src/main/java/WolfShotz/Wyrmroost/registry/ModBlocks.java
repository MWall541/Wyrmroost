package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.PortalBlock;
import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<Block> PORTAL_BLOCK      = register("wyrmroost_portal", new PortalBlock());
    
    public static final RegistryObject<Block> PLATINUM_ORE      = register("platinum_ore", new BlockBase(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)));
    public static final RegistryObject<Block> PLATINUM_BLOCK    = register("platinum_block", new BlockBase(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)).setBeaconBase());
    
    public static final RegistryObject<Block> BLUE_GEODE_ORE    = register("blue_geode_ore", new BlockBase(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 3, 7)));
    public static final RegistryObject<Block> BLUE_GEODE_BLOCK  = register("blue_geode_block", new BlockBase(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5).sound(SoundType.METAL)).setBeaconBase());
    public static final RegistryObject<Block> RED_GEODE_ORE     = register("red_geode_ore", new BlockBase(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 4, 8)));
    public static final RegistryObject<Block> RED_GEODE_BLOCK   = register("red_geode_block", new BlockBase(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(4).hardnessAndResistance(5).sound(SoundType.METAL)).setBeaconBase());
    public static final RegistryObject<Block> PURPLE_GEODE_ORE  = register("purple_geode_ore", new BlockBase(ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.STONE)).setXPDrops(r -> MathHelper.nextInt(r, 8, 11)));
    public static final RegistryObject<Block> PURPLE_GEODE_BLOCK= register("purple_geode_block", new BlockBase(ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(5).hardnessAndResistance(5).sound(SoundType.METAL)).setBeaconBase());
    
    
    private static RegistryObject<Block> register(String name, Block block) {
        ModItems.register(name, new BlockItem(block, ModUtils.itemBuilder()));
        return BLOCKS.register(name, () -> block);
    }
}
