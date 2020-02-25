package WolfShotz.Wyrmroost.content.world.dimension;

import WolfShotz.Wyrmroost.registry.WRBiomes;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class WyrmroostChunkGenerator extends NoiseChunkGenerator<WyrmroostChunkGenerator.Config>
{
    private static final float[] field_222576_h = Util.make(new float[25], (array) -> {
        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
                array[i + 2 + (j + 2) * 5] = f;
            }
        }
    });

    private final OctavesNoiseGenerator depthNoise;

    public WyrmroostChunkGenerator(World world)
    {
        super(world, new SingleBiomeProvider(new SingleBiomeProviderSettings().setBiome(WRBiomes.CAUSTIC_SWAMP.get())), 4, 8, 256, new Config(), true);
        this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 16);
    }

    @Override
    public void spawnMobs(WorldGenRegion region)
    {
        int i = region.getMainChunkX();
        int j = region.getMainChunkZ();
        Biome biome = region.getChunk(i, j).getBiomes()[0];
        SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
        sharedseedrandom.setDecorationSeed(region.getSeed(), i << 4, j << 4);
        WorldEntitySpawner.performWorldGenSpawning(region, biome, i, j, sharedseedrandom);
    }

    @Override
    protected double[] getBiomeNoiseColumn(int noiseX, int noiseZ)
    {
        double[] adouble = new double[2];
        float f = 0;
        float f1 = 0;
        float f2 = 0;
        float f3 = this.biomeProvider.getBiomeAtFactorFour(noiseX, noiseZ).getDepth();

        for(int j = -2; j <= 2; ++j) {
            for(int k = -2; k <= 2; ++k) {
                Biome biome = this.biomeProvider.getBiomeAtFactorFour(noiseX + j, noiseZ + k);
                float f4 = biome.getDepth();
                float f5 = biome.getScale();

                float f6 = field_222576_h[j + 2 + (k + 2) * 5] / (f4 + 2.0F);
                if (biome.getDepth() > f3) {
                    f6 /= 2.0F;
                }

                f += f5 * f6;
                f1 += f4 * f6;
                f2 += f6;
            }
        }

        f = f / f2;
        f1 = f1 / f2;
        f = f * 0.9F + 0.1F;
        f1 = (f1 * 4.0F - 1.0F) / 8.0F;
        adouble[0] = (double) f1 + this.getNoiseDepthAt(noiseX, noiseZ);
        adouble[1] = f;
        return adouble;
    }

    @Override
    protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_)
    {
        double d1 = ((double) p_222545_5_ - (8.5d + p_222545_1_ * 8.5d / 8d * 4d)) * 12D * 128d / 256d / p_222545_3_;
        if (d1 < 0) d1 *= 4d;

        return d1;
    }

    @Override
    protected void fillNoiseColumn(double[] noiseColumn, int noiseX, int noiseZ)
    {
        func_222546_a(noiseColumn, noiseX, noiseZ, 684.412F, 684.412F, 8.555149841308594D, 4.277574920654297D, 3, -10);
    }

    @Override
    public int getGroundHeight() { return world.getSeaLevel() + 1; }

    @Override
    public int getSeaLevel() { return 63; }

    private double getNoiseDepthAt(int noiseX, int noiseZ)
    {
        double d0 = depthNoise.getValue((noiseX * 200), 10d, (noiseZ * 200), 1d, 0, true) / 8000d;
        if (d0 < 0) d0 = -d0 * 0;
        d0 = d0 * 3.0D - 2;
        if (d0 < 0.0D) d0 = d0 / 28;
        else
        {
            if (d0 > 1) d0 = 1;
            d0 = d0 / 40.0D;
        }

        return d0;
    }

    public static class Config extends GenerationSettings
    {
        Config() // dummy.. do more later?
        {
            defaultBlock = Blocks.STONE.getDefaultState(); // ex.
        }
    }
}
