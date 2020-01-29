package WolfShotz.Wyrmroost.content.world.dimension;

import WolfShotz.Wyrmroost.registry.WRBiomes;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WyrmroostBiomeProvider extends BiomeProvider
{
//    @ObjectHolder(Wyrmroost.MOD_ID + "wyrmroost")
    public static final BiomeProviderType<WyrmroostBiomeProviderSettings, WyrmroostBiomeProvider> WYRMROOST = null;

    public WyrmroostBiomeProvider()
    {

    }

    @Override
    public Biome getBiome(int x, int y)
    {
        return null;
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag)
    {
        return new Biome[0];
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength)
    {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
    {
        return null;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn)
    {
        return hasStructureCache.computeIfAbsent(structureIn, (structure) -> getBiomes().stream().anyMatch(b -> b.hasStructure(structure)));
    }

    @Override
    public Set<BlockState> getSurfaceBlocks()
    {
        if (topBlocksCache.isEmpty())
            getBiomes().forEach(b -> topBlocksCache.add(b.getSurfaceBuilderConfig().getTop()));

        return topBlocksCache;
    }

    private static Set<Biome> getBiomes()
    {
        return ModUtils.getRegistryEntries(WRBiomes.BIOMES);
    }
}
