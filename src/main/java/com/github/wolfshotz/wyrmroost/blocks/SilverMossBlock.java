package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.Random;

public class SilverMossBlock extends HangingPlantBlock
{
    public SilverMossBlock()
    {
        super(WRBlocks.builder(Material.PLANTS).zeroHardnessAndResistance(), WRBlocks.SILVER_MOSS_BODY);
    }

    @Override
    protected int getGrowthAmount(Random rand)
    {
        return 1;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        return !world.getBlockState(pos.up()).isIn(WRBlocks.SILVER_MOSS_BODY.get()) && super.isValidPosition(state, world, pos);
    }
}
