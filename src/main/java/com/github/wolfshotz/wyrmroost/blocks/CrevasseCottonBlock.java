package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;

public class CrevasseCottonBlock extends BushBlock implements IForgeShearable
{
    static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    public CrevasseCottonBlock()
    {
        super(WRBlocks.replaceablePlant());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext ctx)
    {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, World level, BlockPos pos, Entity entity)
    {
        if (level.isClientSide)
        {
            double x = entity.getX() - entity.xOld;
            double y = entity.getY() - entity.yOld;
            double z = entity.getZ() - entity.zOld;
            double sq = x * x + y * y + z * z;
            if (RANDOM.nextDouble() < sq * 5)
            {
                x += Mafs.nextDouble(level.random) * 0.15;
                z += Mafs.nextDouble(level.random) * 0.15;
                level.addParticle(ParticleTypes.END_ROD, entity.getRandomX(0.3), entity.getY() + 0.5, entity.getRandomZ(0.3), x * 0.35, y * 0.05, z * 0.35);
            }
        }
    }
}
