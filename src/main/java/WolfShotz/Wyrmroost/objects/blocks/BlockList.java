package WolfShotz.Wyrmroost.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class BlockList
{
    /** Add all block registries to this list and initilize each value in RegistryEvents */
    public static final List<Block> BLOCKS = new ArrayList();

    public static final Block blockplatinumore = new BlockBase("platinum_ore", ItemGroup.BUILDING_BLOCKS, Material.ROCK, 3, SoundType.STONE);
    public static final Block blockplatinum = new BlockBase("platinum_block", ItemGroup.BUILDING_BLOCKS, Material.IRON, 5, 5, 1, SoundType.METAL, true);

    public static final Block blockgeodeore = new BlockBase("geode_ore", ItemGroup.BUILDING_BLOCKS, Material.ROCK, 3, SoundType.STONE);
    public static final Block blockgeode = new BlockBase("geode_block", ItemGroup.BUILDING_BLOCKS, Material.IRON, 5, 5, 1, SoundType.METAL, true);
}
