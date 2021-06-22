package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.client.particle.PetalParticle;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class OseriVinesBodyBlock extends GrowingPlantBodyBlock
{
    private final int particleColor;

    public OseriVinesBodyBlock(Supplier<Block> tip, int particleColor)
    {
        super(WRBlocks.plant(), tip);
        this.particleColor = particleColor;
    }

    @Override
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        super.animateTick(state, level, pos, random);
        if (random.nextDouble() < 0.0285) PetalParticle.play(level, pos, random, particleColor);
    }
}
