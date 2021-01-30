package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

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
    protected boolean canGrowIn(BlockState state)
    {
        return state.isAir();
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
        BlockPos.Mutable blockpos = pos.toMutable().move(growthDirection);
        int i = Math.min(state.get(AGE) + 1, 25);
        int j = Math.min(getGrowthAmount(rand), getHeight(worldIn, pos));

        for (int k = 0; k < j && canGrowIn(worldIn.getBlockState(blockpos)); ++k)
        {
            worldIn.setBlockState(blockpos, state.with(AGE, i = Math.min(i + 1, 25)));
            blockpos.move(growthDirection);
        }
    }

    int getHeight(IWorldReader world, BlockPos pos)
    {
        BlockPos.Mutable mutable = pos.toMutable();
        BlockState state = world.getBlockState(mutable.move(growthDirection.getOpposite()));
        int i = 0;
        while (i < maxGrowthHeight + 1 && state.isIn(getBodyPlantBlock())) i++;
        return i;
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

    public int getMaxGrowthHeight()
    {
        return maxGrowthHeight;
    }
}
