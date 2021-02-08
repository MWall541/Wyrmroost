package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class GrowingPlantBlock extends AbstractTopPlantBlock
{
    private final Supplier<Block> body;
    private final int maxGrowthHeight;

    public GrowingPlantBlock(Properties properties, Direction dir, int maxGrowthHeight, Supplier<Block> body)
    {
        super(properties, dir, WeepingVinesBlock.field_235637_d_, false, 0.1);
        this.body = body;
        this.maxGrowthHeight = maxGrowthHeight;
    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        return (!hasMaxHeight() || getHeight((IWorldReader) worldIn, pos) < maxGrowthHeight) && super.canGrow(worldIn, pos, state, isClient);
    }

    @Override
    protected boolean canGrowIn(BlockState state)
    {
        return state.isAir();
    }

    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state)
    {
        BlockPos.Mutable mutable = pos.toMutable().move(growthDirection);
        int i = 0;
        int amount = getGrowthAmount(rand);
        if (hasMaxHeight()) amount = Math.min(amount, maxGrowthHeight - getHeight(world, pos));

        for (int k = 0; k < amount && canGrowIn(world.getBlockState(mutable)); k++)
        {
            world.setBlockState(mutable, state.with(AGE, k == maxGrowthHeight - 1? 25 : (i = Math.min(i + 1, 25))));
            mutable.move(growthDirection);
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockPos below = pos.offset(growthDirection.getOpposite());
        BlockState belowState = worldIn.getBlockState(below);
        Block belowBlock = belowState.getBlock();

        if (canGrowOn(belowBlock))
        {
            if (belowBlock == getTopPlantBlock() || belowBlock == getBodyPlantBlock() || Block.doesSideFillSquare(belowState.getCollisionShape(worldIn, pos), growthDirection))
                return (!hasMaxHeight() || getHeight(worldIn, pos.offset(growthDirection.getOpposite()), true) + 1 <= maxGrowthHeight);
        }
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        if (hasMaxHeight())
        {
            World world = context.getWorld();
            BlockPos pos = context.getPos().offset(growthDirection.getOpposite());
            if (getHeight(world, pos, true) + 1 >= maxGrowthHeight) return getDefaultState().with(AGE, 25);
        }
        return super.getStateForPlacement(context);
    }

    @Override
    protected int getGrowthAmount(Random rand)
    {
        return PlantBlockHelper.getGrowthAmount(rand);
    }

    @Override
    protected Block getBodyPlantBlock()
    {
        return body.get();
    }

    public boolean hasMaxHeight()
    {
        return maxGrowthHeight != -1;
    }

    public int getHeight(IWorldReader world, BlockPos pos)
    {
        return getHeight(world, pos, true) + getHeight(world, pos, false) - 1;
    }

    public int getHeight(IWorldReader world, BlockPos pos, boolean below)
    {
        Direction dir = below? growthDirection.getOpposite() : growthDirection;
        BlockPos.Mutable mutable = pos.toMutable();
        BlockState state = world.getBlockState(mutable);
        int i = 0;
        for (; i < maxGrowthHeight + 1 && (state.isIn(getBodyPlantBlock()) || state.isIn(this)); ++i)
            state = world.getBlockState(mutable.move(dir));
        return i;
    }

    public int getMaxGrowthHeight()
    {
        return maxGrowthHeight;
    }
}
