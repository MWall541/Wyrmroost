package WolfShotz.Wyrmroost.content.blocks.base;

import WolfShotz.Wyrmroost.event.SetupBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

/** Blockbase - Helper Class allowing for easier block registration */
public class BlockBase extends Block
{
    private boolean isBeaconBase;

    /**
     * Constructor used for more advanced block properties
     * @param name The Resource Location
     * @param properties the block properties
     */
    public BlockBase(String name, Block.Properties properties) { this(name, false, properties); }

    /**
     * Constructor used for more advanced block properties
     * @param name The Resource Location
     * @param properties the block properties
     * @param isBeaconBase Does this block work with beacons?
     */
    public BlockBase(String name, boolean isBeaconBase, Block.Properties properties) {
        super(properties);

        setRegistryName(name);
        this.isBeaconBase = isBeaconBase;
        SetupBlock.BLOCKS.add(this);
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) { return isBeaconBase; }
}