package com.github.wolfshotz.wyrmroost.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class EXPBlock extends OreBlock
{
    private final int minXp, maxXp;

    public EXPBlock(int minXp, int maxXp, Block.Properties properties)
    {
        super(properties);
        this.minXp = minXp;
        this.maxXp = maxXp;
    }

    @Override
    protected int getExperience(Random rand)
    {
        return MathHelper.nextInt(rand, minXp, maxXp);
    }
}