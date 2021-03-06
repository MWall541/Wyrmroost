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
        super(properties, Direction.DOWN, WeepingVinesBlock.SHAPE, false);
        this.tip = tip;
    }

    @Override
    public boolean canPlaceAt(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockPos below = pos.offset(growthDirection.getOpposite());
        BlockState belowState = worldIn.getBlockState(below);
        Block belowBlock = belowState.getBlock();

        return canAttachTo(belowBlock) && (belowBlock == getStem() || belowBlock == getPlant() || Block.isFaceFullSquare(belowState.getCollisionShape(worldIn, pos), growthDirection));
    }

    @Override
    public boolean isFertilizable(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        BlockPos tipPos = getTipPos(worldIn, pos, state);
        if (tipPos != null)
        {
            BlockState tip = worldIn.getBlockState(tipPos);
            return getStem().isFertilizable(worldIn, tipPos, tip, isClient);
        }
        return false;
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
        BlockPos tipPos = getTipPos(worldIn, pos, state);
        if (tipPos != null)
        {
            BlockState bstate = worldIn.getBlockState(tipPos);
            ((AbstractTopPlantBlock) bstate.getBlock()).grow(worldIn, rand, tipPos, bstate);
        }
    }

    @Nullable
    private BlockPos getTipPos(IBlockReader reader, BlockPos pos, BlockState state)
    {
        BlockPos.Mutable mutable = pos.mutableCopy();
        BlockState bstate = state;
        int j = getStem().getMaxGrowthHeight();
        for (int i = 0; i < j && bstate.isOf(state.getBlock()); i++)
        {
            if ((bstate = reader.getBlockState(mutable.move(growthDirection))).isOf(getStem()))
                return mutable.toImmutable();
        }

        return null;
    }

    @Override
    public Item asItem()
    {
        return getStem().asItem();
    }

    @Override
    protected GrowingPlantBlock getStem()
    {
        return (GrowingPlantBlock) tip.get();
    }
}
