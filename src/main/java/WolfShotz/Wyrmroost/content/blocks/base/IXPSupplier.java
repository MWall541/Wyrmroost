package WolfShotz.Wyrmroost.content.blocks.base;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

@FunctionalInterface
public interface IXPSupplier
{
    int xpAmount(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch);
}