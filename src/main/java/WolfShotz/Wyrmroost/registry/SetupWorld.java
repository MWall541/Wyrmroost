package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.world.EndOrePlacement;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Set;
import java.util.function.Predicate;

public class SetupWorld
{
    // =========
    // Dimension
    // =========
    
    @ObjectHolder(Wyrmroost.MOD_ID + ":dim_wyrmroost")
    public static final ModDimension DIM_WYRMROOST = null;
    
    // ==============
    // Ore Generation
    // ==============
    
    private static final Set<Biome> NETHER_BIOMES = BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER);
    private static final Set<Biome> END_BIOMES = BiomeDictionary.getBiomes(BiomeDictionary.Type.END);
    private static final Predicate<Biome> NETHER_FILTER = biome -> NETHER_BIOMES.stream().anyMatch(biome::equals);
    private static final Predicate<Biome> END_FILTER = biome -> END_BIOMES.stream().anyMatch(biome::equals);
    private static final Predicate<Biome> OVERWORLD_FILTER = biome -> !NETHER_FILTER.test(biome) && !END_FILTER.test(biome);
    
    private static final CountRangeConfig PLATINUM_CONFIG = new CountRangeConfig(2, 0, 0, 28);
    private static final CountRangeConfig BLUE_GEODE_CONFIG = new CountRangeConfig(1, 0, 0, 20);
    private static final CountRangeConfig RED_GEODE_CONFIG = new CountRangeConfig(16, 0, 0, 128);

    public static void setupOreGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (OVERWORLD_FILTER.test(biome)) { // Filter Ores so they dont gen in the nether or end
                registerOreEntry(biome, ModBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 8, BLUE_GEODE_CONFIG);
                registerOreEntry(biome, ModBlocks.PLATINUM_ORE.get().getDefaultState(), 9, PLATINUM_CONFIG);
                continue;
            }
            if (NETHER_FILTER.test(biome)) {
                registerOreEntry(biome, OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.RED_GEODE_ORE.get().getDefaultState(), 4, RED_GEODE_CONFIG);
                continue;
            }
            if (END_FILTER.test(biome))
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.EMERALD_ORE, new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), ModBlocks.PURPLE_GEODE_ORE.get().getDefaultState()), EndOrePlacement.endOre, IPlacementConfig.NO_PLACEMENT_CONFIG));
        }
    }

    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     * Takes in the biome, ore blockstate, ore size and the chance configuration as params.
     */
    private static void registerOreEntry(Biome biome, BlockState state, int size, CountRangeConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                        Feature.ORE,
                        new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, size),
                        Placement.COUNT_RANGE,
                        config
                ));
    }
    
    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     * Takes in the biome, ore blockstate, ore size and the chance configuration as params.
     */
    private static void registerOreEntry(Biome biome, OreFeatureConfig.FillerBlockType filler, BlockState state, int size, CountRangeConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                Feature.ORE,
                new OreFeatureConfig(filler, state, size),
                Placement.COUNT_RANGE,
                config
        ));
    }
}
