package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class GillaBushBlock extends BushBlock
{
    static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public GillaBushBlock()
    {
        super(WRBlocks.plant());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public AbstractBlock.OffsetType getOffsetType()
    {
        return AbstractBlock.OffsetType.XZ;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            entity.setMotionMultiplier(state, new Vector3d(0.5d, 0.6d, 0.5d));
            if (!worldIn.isRemote && (entity.lastTickPosX != entity.getPosX() || entity.lastTickPosZ != entity.getPosZ()))
            {
                double x = Math.abs(entity.getPosX() - entity.lastTickPosX);
                double z = Math.abs(entity.getPosZ() - entity.lastTickPosZ);
                if (x >= 0.003 || z >= 0.003)
                    entity.attackEntityFrom(DamageSource.SWEET_BERRY_BUSH, 1f);
            }

        }
    }
}
