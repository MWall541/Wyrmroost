package WolfShotz.Wyrmroost.content.world.dimension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WyrmroostBiomeProvider extends BiomeProvider
{
    public static final List<Biome> BIOMES = Lists.newArrayList(Biomes.PLAINS);

    @Override
    public Biome getBiome(int x, int y) { return Biomes.PLAINS; }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag)
    {
        return BIOMES.toArray(new Biome[0]);
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength)
    {
        int i = centerX - sideLength >> 2;
        int j = centerZ - sideLength >> 2;
        int k = centerX + sideLength >> 2;
        int l = centerZ + sideLength >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        return Sets.newHashSet(getBiomeBlock(i, j, i1, j1));
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
    {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Biome[] abiome = getBiomeBlock(i, j, i1, j1);
        BlockPos blockpos = null;
        int k1 = 0;

        for (int l1 = 0; l1 < i1 * j1; ++l1)
        {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            if (biomes.contains(abiome[l1]))
            {
                if (blockpos == null || random.nextInt(k1 + 1) == 0)
                {
                    blockpos = new BlockPos(i2, 0, j2);
                }

                ++k1;
            }
        }

        return blockpos;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn) { return false; /* for now...*/}

    @Override
    public Set<BlockState> getSurfaceBlocks()
    {
        if (topBlocksCache.isEmpty()) BIOMES.forEach(b -> topBlocksCache.add(b.getSurfaceBuilderConfig().getTop()));

        return topBlocksCache;
    }
}
