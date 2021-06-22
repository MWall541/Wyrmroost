package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.client.particle.PetalParticle;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public class OseriVinesBlock extends GrowingPlantBlock
{
    private final int particleColor;

    public OseriVinesBlock(Supplier<Block> tip, int particleColor)
    {
        super(WRBlocks.plant().randomTicks(), Direction.DOWN, 0, 0.035, tip);
        this.particleColor = particleColor;
    }

    @Override
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        super.animateTick(state, level, pos, random);
        if (random.nextDouble() < 0.045) PetalParticle.play(level, pos, random, particleColor);
    }
}
