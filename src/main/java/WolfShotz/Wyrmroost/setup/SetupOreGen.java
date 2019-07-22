package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class SetupOreGen
{
    private static CountRangeConfig platinumConfig = new CountRangeConfig(2, 0, 0, 25);
    private static CountRangeConfig geodeConfig = new CountRangeConfig(1, 0, 0, 16);

    public static void setupOreGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            registerOreEntry(biome, BlockSetup.blockgeodeore.getDefaultState(), 8, geodeConfig);
            registerOreEntry(biome, BlockSetup.blockplatinumore.getDefaultState(), 9, platinumConfig);
        }

        ModUtils.L.debug("Oregen Registry Complete");
    }

    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     * Takes in the biome, ore blockstate, ore size and the chance configuration as params.
     */
    private static void registerOreEntry(Biome biome, BlockState state, int size, CountRangeConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, size), Placement.COUNT_RANGE, config));
    }
}
