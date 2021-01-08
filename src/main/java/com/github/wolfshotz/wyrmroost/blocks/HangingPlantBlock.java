package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.AbstractTopPlantBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlockHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.Random;
import java.util.function.Supplier;

public class HangingPlantBlock extends AbstractTopPlantBlock
{
    public static final VoxelShape SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);
    private final Supplier<Block> body;

    public HangingPlantBlock(Properties properties, Supplier<Block> body)
    {
        super(properties, Direction.DOWN, SHAPE, false, 0.1);
        this.body = body;
    }

    @Override
    protected int getGrowthAmount(Random rand)
    {
        return PlantBlockHelper.getGrowthAmount(rand);
    }

    @Override
    protected boolean canGrowIn(BlockState state)
    {
        return state.isAir();
    }

    @Override
    protected Block getBodyPlantBlock()
    {
        return body.get();
    }
}
