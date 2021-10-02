package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class HoarfrostBlock extends Block
{
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

    public HoarfrostBlock()
    {
        super(WRBlocks.replaceablePlant().sound(SoundType.WET_GRASS));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, World level, BlockPos pos, Entity entity)
    {
        if (entity instanceof LivingEntity) entity.makeStuckInBlock(state, new Vector3d(0.9, 0.85, 0.9));
    }
}

