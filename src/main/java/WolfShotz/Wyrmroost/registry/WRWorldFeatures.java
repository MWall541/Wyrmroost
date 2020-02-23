package WolfShotz.Wyrmroost.registry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

public class WRWorldFeatures
{


    public static void setupOreGen()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            if (NETHER_FILTER.test(biome)) // Nether Ores
            {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WRBlocks.RED_GEODE_ORE.get().getDefaultState(), 4),
                        Placement.COUNT_RANGE,
                        new CountRangeConfig(16, 0, 0, 128)));
                continue;
            }
            if (END_FILTER.test(biome)) // End Ores
            {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.EMERALD_ORE,
                        new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), WRBlocks.PURPLE_GEODE_ORE.get().getDefaultState()),
                        Placement.RANDOM_COUNT_RANGE,
                        new CountRangeConfig(16, 0, 0, 128)));
                continue;
            }
            // Any other biome not tagged for end or nether
            registerOreEntry(biome, WRBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 8, new CountRangeConfig(1, 0, 0, 20));
            registerOreEntry(biome, WRBlocks.PLATINUM_ORE.get().getDefaultState(), 9, new CountRangeConfig(2, 0, 0, 28));
        }
    }


    private static final Predicate<Biome> NETHER_FILTER = biome -> BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER).stream().anyMatch(biome::equals);
    private static final Predicate<Biome> END_FILTER = biome -> BiomeDictionary.getBiomes(BiomeDictionary.Type.END).stream().anyMatch(biome::equals);

    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     */
    private static void registerOreEntry(Biome biome, BlockState state, int size, CountRangeConfig config)
    {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                Feature.ORE,
                new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, size),
                Placement.COUNT_RANGE,
                config
        ));
    }
}
