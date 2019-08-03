package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.BlockGeodeOre;
import WolfShotz.Wyrmroost.content.blocks.base.BlockBase;
import WolfShotz.Wyrmroost.util.ModUtils;
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

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupBlock
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

    @SubscribeEvent
    public static void blockSetup(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll (
                new BlockBase("platinum_ore", Material.ROCK, ToolType.PICKAXE, 1, 3, SoundType.STONE),
                new BlockBase("platinum_block", Material.IRON, ToolType.PICKAXE, 1, 5, 5, 1, SoundType.METAL, true),

                new BlockGeodeOre(),
                new BlockBase("geode_block", Material.IRON, ToolType.PICKAXE, 2, 5, 5, 1, SoundType.METAL, true)
        );

        ModUtils.L.info("Block Setup Complete");
    }
}
