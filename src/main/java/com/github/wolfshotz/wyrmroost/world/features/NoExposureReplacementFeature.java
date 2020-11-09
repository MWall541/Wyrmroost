package com.github.wolfshotz.wyrmroost.world.features;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;

import java.util.Random;

public class NoExposureReplacementFeature extends Feature<ReplaceBlockConfig>
{
    private static final Direction[] DIRECTIONS = Direction.values(); // needed because god forbid vanilla would do it

    public NoExposureReplacementFeature()
    {
        super(ReplaceBlockConfig::deserialize);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ReplaceBlockConfig config)
    {
        if (world.getBlockState(pos).getBlock() == config.target.getBlock() && checkExposure(world, pos))
            world.setBlockState(pos, config.state, 2);
        return true;
    }

    private static boolean checkExposure(IWorld world, BlockPos initialPos)
    {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (Direction direction : DIRECTIONS)
        {
            BlockState state = world.getBlockState(pos.setPos(initialPos).move(direction));
            if (state.isAir(world, pos)) return false;
        }
        return true;
    }
}
