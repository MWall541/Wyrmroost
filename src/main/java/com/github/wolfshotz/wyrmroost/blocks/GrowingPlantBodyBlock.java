package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class GrowingPlantBodyBlock extends AbstractBodyPlantBlock
{
    private final Supplier<Block> tip;

    public GrowingPlantBodyBlock(Properties properties, Supplier<Block> tip)
    {
        this(properties, tip, false);
    }

    public GrowingPlantBodyBlock(Properties properties, Supplier<Block> tip, boolean scheduleFluidTicks)
    {
        super(properties, Direction.DOWN, WeepingVinesBlock.SHAPE, scheduleFluidTicks);
        this.tip = tip;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockPos below = pos.relative(growthDirection.getOpposite());
        BlockState belowState = worldIn.getBlockState(below);
        Block belowBlock = belowState.getBlock();

        return canAttachToBlock(belowBlock) && (belowBlock == getHeadBlock() || belowBlock == getBodyBlock() || Block.isFaceFull(belowState.getCollisionShape(worldIn, pos), growthDirection));
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        BlockPos tipPos = getHeadPos(worldIn, pos, state);
        if (tipPos != null)
        {
            BlockState tip = worldIn.getBlockState(tipPos);
            return getHeadBlock().isValidBonemealTarget(worldIn, tipPos, tip, isClient);
        }
        return false;
    }

    @Override
    public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
        BlockPos headPos = getHeadPos(worldIn, pos, state);
        if (headPos != null)
        {
            BlockState bstate = worldIn.getBlockState(headPos);
            ((AbstractTopPlantBlock) bstate.getBlock()).performBonemeal(worldIn, rand, headPos, bstate);
        }
    }

    @Nullable
    private BlockPos getHeadPos(IBlockReader reader, BlockPos pos, BlockState state)
    {
        BlockPos.Mutable mutable = pos.mutable();
        BlockState bstate = state;
        int j = getHeadBlock().getMaxGrowthHeight();
        for (int i = 0; i < j && bstate.is(state.getBlock()); i++)
        {
            if ((bstate = reader.getBlockState(mutable.move(growthDirection))).is(getHeadBlock()))
                return mutable.immutable();
        }

        return null;
    }

    @Override
    public Item asItem()
    {
        return getHeadBlock().asItem();
    }

    @Override
    protected GrowingPlantBlock getHeadBlock()
    {
        return (GrowingPlantBlock) tip.get();
    }
}
