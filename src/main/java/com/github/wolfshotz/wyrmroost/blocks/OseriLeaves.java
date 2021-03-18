package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class OseriLeaves extends LeavesBlock
{
    public OseriLeaves(Properties properties)
    {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random)
    {
        if (random.nextDouble() < 0.1 && level.getBlockState(pos = pos.below()).isAir())
        {
            // todo: make falling petals?
            level.addParticle(ParticleTypes.DRIPPING_HONEY, pos.getX(), pos.getY() + 0.9, pos.getZ(), 0, 0, 0);
        }
    }
}
