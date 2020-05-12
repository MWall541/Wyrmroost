package WolfShotz.Wyrmroost.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Random;
import java.util.function.Function;

public class CrystalBlock extends StainedGlassBlock
{
    private final Function<Random, Integer> xpAmount;

    public CrystalBlock(DyeColor colorIn, Function<Random, Integer> xpAmount, Properties properties)
    {
        super(colorIn, properties);
        this.xpAmount = xpAmount;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        return xpAmount != null && silktouch == 0? xpAmount.apply(RANDOM) : 0;
    }
}
