package com.github.wolfshotz.wyrmroost.world.features;

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
    public boolean generate(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, ReplaceBlockConfig config)
    {
        if (level.getBlockState(pos).isOf(config.target.getBlock()) && checkExposure(level, pos))
            level.setBlockState(pos, config.state, 2);

        return true;
    }

    private static boolean checkExposure(ISeedReader world, BlockPos initialPos)
    {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (Direction direction : DIRECTIONS)
        {
            BlockState state = level.getBlockState(pos.set(initialPos, direction));
            if (state.isAir(level, pos)) return false;
        }
        return true;
    }
}
