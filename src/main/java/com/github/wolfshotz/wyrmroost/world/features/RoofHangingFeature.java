package com.github.wolfshotz.wyrmroost.world.features;

import com.github.wolfshotz.wyrmroost.blocks.GrowingPlantBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class RoofHangingFeature extends Feature<RoofHangingFeature.Config>
{
    public RoofHangingFeature()
    {
        super(Config.CODEC);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator gen, Random random, BlockPos pos, Config config)
    {
        BlockPos.Mutable mutable = pos.mutable();
        for (int i = config.height; i > 0; i--) // todo 1.17: replace hard-coded minimum world height '0' to level's minimum level
        {
            mutable.setY(i);
            if (level.getBlockState(mutable).isAir(level, mutable) && Block.isFaceFull(level.getBlockState(mutable.move(Direction.UP)).getCollisionShape(level, mutable), Direction.DOWN))
            {
                int plantLength = MathHelper.nextInt(random, 1, config.plant.getMaxGrowthHeight() - 1);
                for (int j = 0; j <= plantLength; j++)
                {
                    mutable.setY(i - j);
                    if (level.getBlockState(mutable).isAir(level, mutable))
                    {
                        BlockState state = j == plantLength? config.plant.getStateForPlacement(level) : config.plant.getBodyBlock().defaultBlockState();
                        level.setBlock(mutable, state, 2);
                    }
                }
            }
        }

        return true;
    }

    public static class Config implements IFeatureConfig
    {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(i -> i.group(
                Registry.BLOCK.fieldOf("block").forGetter(c -> c.plant),
                Codec.INT.fieldOf("height").forGetter(c -> c.height)
        ).apply(i, Config::new));

        final GrowingPlantBlock plant;
        private final int height;

        public Config(Block block, int height)
        {
            if (!(block instanceof GrowingPlantBlock))
                throw new IllegalArgumentException("RoofHangingFeature requires a GrowingPlantBlock");
            this.plant = (GrowingPlantBlock) block;
            this.height = height;
        }
    }
}
