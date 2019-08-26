package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.GeodeOreBlock;
import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.content.blocks.eggblock.EggBlock;
import WolfShotz.Wyrmroost.content.blocks.eggblock.EggTileEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Wyrmroost.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupBlocks
{
    // List Collection of all Blocks in preparation for BlockItem Registration
    public static List<Block> BLOCKS = new ArrayList<>();

    @ObjectHolder(Wyrmroost.modID + ":platinum_ore")
    public static Block blockplatinumore;

    @ObjectHolder(Wyrmroost.modID + ":platinum_block")
    public static Block blockplatinum;

    @ObjectHolder(Wyrmroost.modID + ":geode_ore")
    public static Block blockgeodeore;

    @ObjectHolder(Wyrmroost.modID + ":geode_block")
    public static Block blockgeode;
    
    @ObjectHolder(Wyrmroost.modID + ":egg")
    public static Block egg;

    @SubscribeEvent
    public static void blockSetup(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll (
                new EggBlock(),
                
                new BlockBase("platinum_ore", ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)),
                new BlockBase("platinum_block", true, ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)),

                new GeodeOreBlock(),
                new BlockBase("geode_block", true, ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL))
        );
    }
    
    // =================
    //   Tile Entities
    // =================
    
    @ObjectHolder(Wyrmroost.modID + ":teegg")
    public static TileEntityType<EggTileEntity> teegg;
    
    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(
                TileEntityType.Builder.create(EggTileEntity::new, SetupBlocks.egg).build(null).setRegistryName("teegg")
        );
    }
}
