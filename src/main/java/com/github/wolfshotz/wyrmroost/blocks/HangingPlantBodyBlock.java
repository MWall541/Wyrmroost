package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.AbstractBodyPlantBlock;
import net.minecraft.block.AbstractTopPlantBlock;
import net.minecraft.block.Block;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.util.Direction;

import java.util.function.Supplier;

public class HangingPlantBodyBlock extends AbstractBodyPlantBlock
{
    private final Supplier<Block> tip;

    public HangingPlantBodyBlock(Properties properties, Supplier<Block> tip)
    {
        super(properties, Direction.DOWN, WeepingVinesBlock.field_235637_d_, false);
        this.tip = tip;
    }

    @Override
    protected AbstractTopPlantBlock getTopPlantBlock()
    {
        return (AbstractTopPlantBlock) tip.get();
    }
}
