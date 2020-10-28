package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.world.features.NoExposureReplacementFeature;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
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
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241883_b, WRBlocks.RED_GEODE_ORE.get().getDefaultState(), 4)).func_242733_d(128).func_242728_a().func_242731_b(12));
        else if (category == Biome.Category.THEEND)
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Features.NO_EXPOSURE_REPLACE.withConfiguration(new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), WRBlocks.PURPLE_GEODE_ORE.get().getDefaultState())).func_242733_d(80).func_242728_a().func_242731_b(64));
        else
        {
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, WRBlocks.PLATINUM_ORE.get().getDefaultState(), 9)).func_242733_d(64).func_242728_a().func_242731_b(20));
            evt.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, WRBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 9)).func_242733_d(16).func_242728_a());
        }
    }

    public static class Features
    {
        public static final Feature<ReplaceBlockConfig> NO_EXPOSURE_REPLACE = new NoExposureReplacementFeature(ReplaceBlockConfig.field_236604_a_);
    }
}
