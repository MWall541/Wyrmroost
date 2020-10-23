package WolfShotz.Wyrmroost.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;

import java.util.Random;

public class NoExposureReplacementFeature extends Feature<ReplaceBlockConfig>
{
    private static final Direction[] DIRECTIONS = Direction.values(); // needed because god forbid vanilla would do it

    public NoExposureReplacementFeature(Codec<ReplaceBlockConfig> p_i231953_1_)
    {
        super(p_i231953_1_);
    }

    @Override
    public boolean func_241855_a(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, ReplaceBlockConfig config)
    {
        if (world.getBlockState(pos).isIn(config.target.getBlock()) && checkExposure(world, pos))
            world.setBlockState(pos, config.state, 2);

        return true;
    }

    private static boolean checkExposure(ISeedReader world, BlockPos initialPos)
    {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (Direction direction : DIRECTIONS)
        {
            BlockState state = world.getBlockState(pos.setAndMove(initialPos, direction));
            if (state.isAir(world, pos)) return false;
        }
        return true;
    }
}
