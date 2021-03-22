package com.github.wolfshotz.wyrmroost.world.features;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Random;

public class SurfaceAwareLakeFeature extends Feature<BlockStateFeatureConfig>
{
    public SurfaceAwareLakeFeature()
    {
        super(BlockStateFeatureConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos pos, BlockStateFeatureConfig config)
    {
        while (pos.getY() > 5 && level.isEmptyBlock(pos)) pos = pos.below();

        if (pos.getY() <= 4) return false;
        else
        {
            pos = pos.below(4);
            if (level.startsForFeature(SectionPos.of(pos), Structure.VILLAGE).findAny().isPresent()) return false;
            else
            {
                boolean[] complex = new boolean[2048];
                int i = random.nextInt(4) + 4;

                for (int j = 0; j < i; ++j)
                {
                    double d0 = random.nextDouble() * 6.0D + 3.0D;
                    double d1 = random.nextDouble() * 4.0D + 2.0D;
                    double d2 = random.nextDouble() * 6.0D + 3.0D;
                    double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                    double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                    double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                    for (int l = 1; l < 15; ++l)
                    {
                        for (int i1 = 1; i1 < 15; ++i1)
                        {
                            for (int j1 = 1; j1 < 7; ++j1)
                            {
                                double d6 = ((double) l - d3) / (d0 / 2.0D);
                                double d7 = ((double) j1 - d4) / (d1 / 2.0D);
                                double d8 = ((double) i1 - d5) / (d2 / 2.0D);
                                double d9 = d6 * d6 + d7 * d7 + d8 * d8;
                                if (d9 < 1.0D)
                                {
                                    complex[(l * 16 + i1) * 8 + j1] = true;
                                }
                            }
                        }
                    }
                }

                for (int k1 = 0; k1 < 16; ++k1)
                {
                    for (int l2 = 0; l2 < 16; ++l2)
                    {
                        for (int k = 0; k < 8; ++k)
                        {
                            boolean flag = !complex[(k1 * 16 + l2) * 8 + k] && (k1 < 15 && complex[((k1 + 1) * 16 + l2) * 8 + k] || k1 > 0 && complex[((k1 - 1) * 16 + l2) * 8 + k] || l2 < 15 && complex[(k1 * 16 + l2 + 1) * 8 + k] || l2 > 0 && complex[(k1 * 16 + (l2 - 1)) * 8 + k] || k < 7 && complex[(k1 * 16 + l2) * 8 + k + 1] || k > 0 && complex[(k1 * 16 + l2) * 8 + (k - 1)]);
                            if (flag)
                            {
                                Material material = level.getBlockState(pos.offset(k1, k, l2)).getMaterial();
                                if (k >= 4 && material.isLiquid())
                                    return false;

                                if (k < 4 && !material.isSolid() && level.getBlockState(pos.offset(k1, k, l2)) != config.state)
                                    return false;
                            }
                        }
                    }
                }

                for (int l1 = 0; l1 < 16; ++l1)
                {
                    for (int i3 = 0; i3 < 16; ++i3)
                    {
                        for (int i4 = 0; i4 < 8; ++i4)
                        {
                            if (complex[(l1 * 16 + i3) * 8 + i4])
                                level.setBlock(pos.offset(l1, i4, i3), i4 >= 4? Blocks.AIR.defaultBlockState() : config.state, 2);
                        }
                    }
                }

                for (int i2 = 0; i2 < 16; ++i2)
                {
                    for (int j3 = 0; j3 < 16; ++j3)
                    {
                        for (int j4 = 4; j4 < 8; ++j4)
                        {
                            if (complex[(i2 * 16 + j3) * 8 + j4])
                            {
                                BlockPos blockpos = pos.offset(i2, j4 - 1, j3);
                                if (isDirt(level.getBlockState(blockpos).getBlock()) && level.getBrightness(LightType.SKY, pos.offset(i2, j4, j3)) > 0)
                                {
                                    // use the biomes surface block instead
                                    level.setBlock(blockpos,
                                            level.getBiome(blockpos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial(),
                                            2);
                                }
                            }
                        }
                    }
                }

                // no fooliage around hot fluids
                if (config.state.getFluidState().getType().getAttributes().getTemperature() > 1000)
                {
                    for (int j2 = 0; j2 < 16; ++j2)
                    {
                        for (int k3 = 0; k3 < 16; ++k3)
                        {
                            for (int k4 = 0; k4 < 8; ++k4)
                            {
                                boolean flag1 = !complex[(j2 * 16 + k3) * 8 + k4] && (j2 < 15 && complex[((j2 + 1) * 16 + k3) * 8 + k4] || j2 > 0 && complex[((j2 - 1) * 16 + k3) * 8 + k4] || k3 < 15 && complex[(j2 * 16 + k3 + 1) * 8 + k4] || k3 > 0 && complex[(j2 * 16 + (k3 - 1)) * 8 + k4] || k4 < 7 && complex[(j2 * 16 + k3) * 8 + k4 + 1] || k4 > 0 && complex[(j2 * 16 + k3) * 8 + (k4 - 1)]);
                                if (flag1 && (k4 < 4 || random.nextInt(2) != 0) && level.getBlockState(pos.offset(j2, k4, k3)).getMaterial().isSolid())
                                {
                                    level.setBlock(pos.offset(j2, k4, k3), Blocks.STONE.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }

                if (config.state.getMaterial() == Material.WATER)
                {
                    for (int k2 = 0; k2 < 16; ++k2)
                    {
                        for (int l3 = 0; l3 < 16; ++l3)
                        {
                            int l4 = 4;
                            BlockPos blockpos1 = pos.offset(k2, 4, l3);
                            if (level.getBiome(blockpos1).shouldFreeze(level, blockpos1, false))
                            {
                                level.setBlock(blockpos1, Blocks.ICE.defaultBlockState(), 2);
                            }
                        }
                    }
                }

                return true;
            }
        }
    }
}
