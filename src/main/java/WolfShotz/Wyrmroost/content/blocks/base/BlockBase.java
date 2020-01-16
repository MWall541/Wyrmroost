package WolfShotz.Wyrmroost.content.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Random;
import java.util.function.Function;

/**
 * Blockbase - Helper Class allowing for easier block registration
 */
public class BlockBase extends Block
{
    private boolean isBeaconBase;
    private Function<Random, Integer> xpAmount;
    
    public BlockBase(Block.Properties properties)
    {
        super(properties);
    }
    
    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon)
    {
        return isBeaconBase;
    }
    
    public BlockBase setBeaconBase()
    {
        this.isBeaconBase = true;
        
        return this;
    }
    
    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        return xpAmount != null && silktouch == 0? xpAmount.apply(RANDOM) : 0;
    }
    
    public BlockBase setXPDrops(Function<Random, Integer> function)
    {
        this.xpAmount = function;
        
        return this;
    }
}