package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.world.features.NoExposureReplacementFeature;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WRWorld
{
    public static List<Consumer<BiomeLoadingEvent>> BIOME_LISTENERS = new ArrayList<>();

    public static final RegistryKey<Dimension> THE_WYRMROOST = RegistryKey.of(Registry.DIMENSION_OPTIONS, Wyrmroost.id("the_wyrmroost"));

    public static final RegistryKey<Biome> TINCTURE_WEALD = biomeKey("tincture_weald");

    public static void onBiomeLoad(BiomeLoadingEvent evt)
    {
        for (Consumer<BiomeLoadingEvent> e : BIOME_LISTENERS) e.accept(evt);

        BiomeGenerationSettingsBuilder generator = evt.getGeneration();

        switch (evt.getCategory())
        {
            case NETHER:
                generator.feature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.RED_GEODE_FEATURE);
                break;
            case THEEND:
                generator.feature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.PURPLE_GEODE_FEATURE);
                break;
            default:
                generator.feature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.PLATINUM_ORE_FEATURE);
                generator.feature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.BLUE_GEODE_FEATURE);
        }
    }

    static RegistryKey<Biome> biomeKey(String name)
    {
        return RegistryKey.of(Registry.BIOME_KEY, Wyrmroost.id(name));
    }

    public static class Features
    {
        public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, Wyrmroost.MOD_ID);

        public static final RegistryObject<Feature<ReplaceBlockConfig>> NO_EXPOSE_REPLACE = REGISTRY.register("no_expose_replace", () -> new NoExposureReplacementFeature(ReplaceBlockConfig.CODEC));

        public static ConfiguredFeature<?, ?> RED_GEODE_FEATURE;
        public static ConfiguredFeature<?, ?> PURPLE_GEODE_FEATURE;
        public static ConfiguredFeature<?, ?> BLUE_GEODE_FEATURE;
        public static ConfiguredFeature<?, ?> PLATINUM_ORE_FEATURE;

        /**
         * Needed because block registry entries don't exist at static. Must defer initalization.
         */
        public static void init()
        {
            RED_GEODE_FEATURE = configure("ore_red_geode", Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WRBlocks.RED_GEODE_ORE.get().getDefaultState(), 4)).rangeOf(128).spreadHorizontally().repeat(8));
            PURPLE_GEODE_FEATURE = configure("ore_purple_geode", NO_EXPOSE_REPLACE.get().configure(new ReplaceBlockConfig(Blocks.END_STONE.getDefaultState(), WRBlocks.PURPLE_GEODE_ORE.get().getDefaultState())).rangeOf(80).spreadHorizontally().repeat(45));
            BLUE_GEODE_FEATURE = configure("ore_blue_geode", Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, WRBlocks.BLUE_GEODE_ORE.get().getDefaultState(), 10)).rangeOf(16).spreadHorizontally());
            PLATINUM_ORE_FEATURE = configure("ore_platinum", Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, WRBlocks.PLATINUM_ORE.get().getDefaultState(), 9)).rangeOf(64).spreadHorizontally().repeat(20));
        }

        private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> configure(String id, ConfiguredFeature<FC, ?> cf)
        {
            return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, id, cf);
        }
    }
}
