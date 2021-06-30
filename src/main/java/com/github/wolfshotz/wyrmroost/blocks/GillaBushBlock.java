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
import net.minecraftforge.common.IForgeShearable;

public class GillaBushBlock extends BushBlock implements IForgeShearable
{
    static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    public GillaBushBlock()
    {
        super(WRBlocks.replaceablePlant());
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return SHAPE;
    }

    @Override
    public AbstractBlock.OffsetType getOffsetType()
    {
        return AbstractBlock.OffsetType.XZ;
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entity)
    {
        if (entity instanceof LivingEntity)
        {
            entity.makeStuckInBlock(state, new Vector3d(0.5d, 0.6d, 0.5d));
            if (!worldIn.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ()))
            {
                if (Math.abs(entity.getX() - entity.xOld) >= 0.003 || Math.abs(entity.getZ() - entity.zOld) >= 0.003)
                    entity.hurt(DamageSource.SWEET_BERRY_BUSH, 1f);
            }
        }
    }
}
