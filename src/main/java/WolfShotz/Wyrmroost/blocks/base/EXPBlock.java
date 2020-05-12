package WolfShotz.Wyrmroost.blocks.base;

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
    private final Function<Random, Integer> xpAmount;
    
    public EXPBlock(Function<Random, Integer> xpAmount, Block.Properties properties)
    {
        super(properties);
        this.xpAmount = xpAmount;
    }
    
    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        return xpAmount != null && silktouch == 0? xpAmount.apply(RANDOM) : 0;
    }
}