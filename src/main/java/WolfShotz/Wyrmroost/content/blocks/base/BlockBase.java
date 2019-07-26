package WolfShotz.Wyrmroost.content.blocks.base;

import WolfShotz.Wyrmroost.setup.BlockSetup;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

/** Blockbase - Helper Class allowing for easier block registration */
public class BlockBase extends Block
{
    private boolean isBeaconBase;

    /**
     * @param name The internal name
     * @param material Material the block is
     * @param tool ToolType it takes to break this block
     * @param harvestLevel Tool level it takes to break this block
     * @param toughness The Resistance and hardness of the block
     * @param sound The sound the block makes
     */
    public BlockBase(String name, Material material, ToolType tool, int harvestLevel, float toughness, SoundType sound)
        { this(name, material, tool, harvestLevel, toughness, toughness, 0, sound, false); }

    /**
     * Constructor used for more advanced block properties
     * @param name The Resource Location
     * @param material Material the block is
     * @param hardness How hard the block is (i.e how fast it takes to break it)
     * @param resistance How resistant the block is to explosives
     * @param light The light level the block produces
     * @param sound The sound the block makes
     * @param isBeaconBase Does this block work with beacons?
     */
    public BlockBase(String name, Material material, ToolType tool, int harvestLevel, float hardness, float resistance, int light, SoundType sound, boolean isBeaconBase) {
        super(Block.Properties
                      .create(material)
                      .harvestTool(tool)
                      .harvestLevel(harvestLevel)
                      .hardnessAndResistance(hardness, resistance)
                      .lightValue(light)
                      .sound(sound)
        );
        setRegistryName(name);
        this.isBeaconBase = isBeaconBase;

        BlockSetup.BLOCKS.add(this);
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) { return isBeaconBase; }
}
