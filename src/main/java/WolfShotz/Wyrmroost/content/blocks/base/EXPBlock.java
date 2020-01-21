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
public class EXPBlock extends Block
{
    private Function<Random, Integer> xpAmount;
    
    public EXPBlock(Block.Properties properties)
    {
        super(properties);
    }
    
    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        return xpAmount != null && silktouch == 0? xpAmount.apply(RANDOM) : 0;
    }
    
    public EXPBlock setXPDrops(Function<Random, Integer> function)
    {
        this.xpAmount = function;
        
        return this;
    }
}