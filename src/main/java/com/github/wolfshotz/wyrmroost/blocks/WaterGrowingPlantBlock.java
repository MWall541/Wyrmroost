package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class WaterGrowingPlantBlock extends GrowingPlantBlock implements ILiquidContainer
{
    public WaterGrowingPlantBlock(Properties properties, Direction dir, int maxGrowthHeight, double growthChance, Supplier<Block> body)
    {
        super(properties, dir, maxGrowthHeight, growthChance, true, body);
    }

    @Override
    protected boolean canGrowInto(BlockState state)
    {
        return state.getFluidState().is(FluidTags.WATER);
    }

    @Override
    protected boolean canAttachToBlock(Block block)
    {
        return block != Blocks.MAGMA_BLOCK;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.getStateForPlacement(context) : null;
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(IBlockReader p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_)
    {
        return false;
    }

    @Override
    public boolean placeLiquid(IWorld p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_, FluidState p_204509_4_)
    {
        return false;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(Random rand)
    {
        return 1;
    }
}
