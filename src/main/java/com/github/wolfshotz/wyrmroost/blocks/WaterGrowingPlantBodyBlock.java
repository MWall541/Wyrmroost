package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.function.Supplier;

public class WaterGrowingPlantBodyBlock extends GrowingPlantBodyBlock implements ILiquidContainer
{
    public WaterGrowingPlantBodyBlock(Properties properties, Supplier<Block> tip)
    {
        super(properties, tip, true);
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
    public FluidState getFluidState(BlockState state)
    {
        return Fluids.WATER.getSource(false);
    }
}
