package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.GeodeOreBlock;
import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupBlocks
{
    // List Collection of all Blocks in preparation for BlockItem Registration
    public static List<Block> BLOCKS = new ArrayList<>();

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_ore")
    public static Block blockplatinumore;

    @ObjectHolder(Wyrmroost.MOD_ID + ":platinum_block")
    public static Block blockplatinum;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_ore")
    public static Block blockgeodeore;

    @ObjectHolder(Wyrmroost.MOD_ID + ":geode_block")
    public static Block blockgeode;

    @SubscribeEvent
    public static void blockSetup(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll (
                new BlockBase("platinum_ore", ModUtils.blockBuilder(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3).sound(SoundType.STONE)),
                new BlockBase("platinum_block", true, ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL)),

                new GeodeOreBlock(),
                new BlockBase("geode_block", true, ModUtils.blockBuilder(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5).sound(SoundType.METAL))
        );
    }
}
