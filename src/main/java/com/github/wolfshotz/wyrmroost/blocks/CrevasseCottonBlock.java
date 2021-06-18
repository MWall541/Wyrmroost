package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class CrevasseCottonBlock extends BushBlock
{
    static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    public CrevasseCottonBlock()
    {
        super(WRBlocks.plant());
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        if (random.nextDouble() < 0.25)
        {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + (random.nextDouble() * 0.25) + 0.25;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.CLOUD, false, x, y, z, Mafs.nextDouble(random) * 0.1, 0.1, Mafs.nextDouble(random) * 0.1);
        }
    }
}
