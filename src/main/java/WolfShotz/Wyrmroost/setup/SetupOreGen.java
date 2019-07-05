package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.BlockList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class SetupOreGen
{
    private static CountRangeConfig platinumPlacement = new CountRangeConfig(2, 0, 0, 25);
    private static CountRangeConfig geodePlacement = new CountRangeConfig(1, 0, 0, 16);

    public static void setupOreGen() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(8, geodePlacement));
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(9, platinumPlacement));
        }

        Wyrmroost.L.debug("Oregen Registry Complete");
    }

    /**
     * Helper method that turns this rediculously long line into something more convenient and readable...
     * Takes in the ore size and the chance configuration as params.
     */
    private static ConfiguredFeature<?> createOreFeature(int size, CountRangeConfig config) {
        return Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockList.blockgeodeore.getDefaultState(), size), Placement.COUNT_RANGE, config);
    }
}
