package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupBlocks
{
    private static final String ID = Wyrmroost.MOD_ID + ":";
    
    // List Collection of all Blocks in preparation for BlockItem Registration
    public static List<Block> BLOCKS = new ArrayList<>();

    @ObjectHolder(ID + "platinum_ore") public static Block blockplatinumore;
    @ObjectHolder(ID + "platinum_block") public static Block blockplatinum;
    
    @ObjectHolder(ID + "geode_ore") public static Block blockgeodeore;
    @ObjectHolder(ID + "geode_block") public static Block blockgeode;
    
    @SubscribeEvent
    public static void blockSetup(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll (
                new BlockBase("platinum_ore", ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)),
                new BlockBase("platinum_block", ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)).setBeaconBase(true),

                new BlockBase("geode_ore", ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3).sound(SoundType.STONE)).setXPDrops((s, w, p, f, silk) -> silk == 0 ? MathHelper.nextInt(new Random(), 3, 7) : 0),
                new BlockBase("geode_block", ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)).setBeaconBase(true)
        );
    }
}
