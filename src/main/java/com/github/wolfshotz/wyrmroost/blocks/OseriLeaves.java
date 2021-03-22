package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.client.particle.data.ColoredParticleData;
import com.github.wolfshotz.wyrmroost.registry.WRParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class OseriLeaves extends LeavesBlock
{
    private final int color;

    public OseriLeaves(int rgb, Properties properties)
    {
        super(properties);
        this.color = rgb;
    }

    @Override
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        if (random.nextDouble() < 0.06 && !level.getBlockState(pos = pos.below()).isCollisionShapeFullBlock(level, pos))
        {
            level.addParticle(new ColoredParticleData(WRParticles.PETAL.get(), color),
                    pos.getX() + random.nextDouble(),
                    pos.getY() + 0.9,
                    pos.getZ() + random.nextDouble(),
                    0,
                    0,
                    0);
        }
    }
}
