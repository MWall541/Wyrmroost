package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.world.features.NoExposureReplacementFeature;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WRWorld
{
    public static List<Consumer<Biome>> BIOME_LISTENERS = new ArrayList<>();

    public static void onBiomeLoad()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            BIOME_LISTENERS.forEach(e -> e.accept(biome));

            switch (biome.getCategory())
            {
                case NETHER:
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WRBlocks.RED_GEODE_ORE.get().getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 128))));
                    break;
                case THEEND:
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.NO_EXPOSURE_REPLACE.withConfiguration(new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), WRBlocks.PURPLE_GEODE_ORE.get().getDefaultState())).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(40, 0, 0, 80))));
                    break;
                default:
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WRBlocks.PLATINUM_ORE.get().getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WRBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 10)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 16))));

            }
        }
    }

    public static class Features
    {
        public static final Feature<ReplaceBlockConfig> NO_EXPOSURE_REPLACE = new NoExposureReplacementFeature();
    }
}
