package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.world.features.NoExposureReplacementFeature;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WRWorld
{
    public static List<Consumer<BiomeLoadingEvent>> BIOME_LISTENERS = new ArrayList<>();

    public static void onBiomeLoad(BiomeLoadingEvent evt)
    {
        BIOME_LISTENERS.forEach(e -> e.accept(evt));

        Biome.Category category = evt.getCategory();

        if (category == Biome.Category.NETHER)
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Features.RED_GEODE);
        else if (category == Biome.Category.THEEND)
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Features.PURPLE_GEODE);
        else
        {
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Features.PLATINUM_ORE);
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Features.BLUE_GEODE);
        }
    }

    public static class Features
    {
        public static final Feature<ReplaceBlockConfig> NO_EXPOSURE_REPLACE = new NoExposureReplacementFeature(ReplaceBlockConfig.field_236604_a_);

        public static final ConfiguredFeature<?, ?> PLATINUM_ORE = configured("ore_platinum", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, WRBlocks.PLATINUM_ORE.get().getDefaultState(), 9)).func_242733_d(64).func_242728_a().func_242731_b(20));
        public static final ConfiguredFeature<?, ?> BLUE_GEODE = configured("ore_blue_geode", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, WRBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 9)).func_242733_d(16).func_242728_a());
        public static final ConfiguredFeature<?, ?> RED_GEODE = configured("ore_red_geode", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241883_b, WRBlocks.RED_GEODE_ORE.get().getDefaultState(), 4)).func_242733_d(128).func_242728_a().func_242731_b(12));
        public static final ConfiguredFeature<?, ?> PURPLE_GEODE = configured("ore_pruple_geode", NO_EXPOSURE_REPLACE.withConfiguration(new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), WRBlocks.PURPLE_GEODE_ORE.get().getDefaultState())).func_242733_d(80).func_242728_a().func_242731_b(64));

        private static ConfiguredFeature<?, ?> configured(String name, ConfiguredFeature<?, ?> feature)
        {
            return Registry.register(WorldGenRegistries.field_243653_e, Wyrmroost.rl(name), feature);
        }
    }
}
