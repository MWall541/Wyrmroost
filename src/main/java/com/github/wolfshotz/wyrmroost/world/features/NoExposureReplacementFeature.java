package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.util.ModUtils;
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
    public NoExposureReplacementFeature()
    {
        super(ReplaceBlockConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator chunkGenerator, Random random, BlockPos pos, ReplaceBlockConfig config)
    {
        if (level.getBlockState(pos).is(config.target.getBlock()) && checkExposure(level, pos))
            level.setBlock(pos, config.state, 2);

        return true;
    }

    private static boolean checkExposure(ISeedReader level, BlockPos initialPos)
    {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (Direction direction : ModUtils.DIRECTIONS)
        {
            BlockState state = level.getBlockState(pos.setWithOffset(initialPos, direction));
            if (state.isAir(level, pos)) return false;
        }
        return true;
    }
}
