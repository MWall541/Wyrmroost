package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.world.features.NoExposureReplacementFeature;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
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

public class WRWorld
{
    public static final RegistryKey<Dimension> THE_WYRMROOST = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, Wyrmroost.id("the_wyrmroost"));

    public static final RegistryKey<Biome> TINCTURE_WEALD = biomeKey("tincture_weald");

    public static void onBiomeLoad(BiomeLoadingEvent event)
    {
        for (EntityType<?> entry : ModUtils.getRegistryEntries(WREntities.REGISTRY))
            if (entry instanceof WREntities.Type)
            {
                WREntities.Type<?> type = (WREntities.Type<?>) entry;
                if (type.spawnBiomes != null) type.spawnBiomes.accept(event);
            }

        BiomeGenerationSettingsBuilder generator = event.getGeneration();
        switch (event.getCategory())
        {
            case NETHER:
                generator.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.RED_GEODE_FEATURE);
                break;
            case THEEND:
                generator.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.PURPLE_GEODE_FEATURE);
                break;
            default:
                generator.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.PLATINUM_ORE_FEATURE);
                generator.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.BLUE_GEODE_FEATURE);
        }
    }

    static RegistryKey<Biome> biomeKey(String name)
    {
        return RegistryKey.create(Registry.BIOME_REGISTRY, Wyrmroost.id(name));
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
            RED_GEODE_FEATURE = configure("ore_red_geode", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WRBlocks.RED_GEODE_ORE.get().defaultBlockState(), 4)).range(128).squared().count(8));
            PURPLE_GEODE_FEATURE = configure("ore_purple_geode", NO_EXPOSE_REPLACE.get().configured(new ReplaceBlockConfig(Blocks.END_STONE.defaultBlockState(), WRBlocks.PURPLE_GEODE_ORE.get().defaultBlockState())).range(80).squared().count(45));
            BLUE_GEODE_FEATURE = configure("ore_blue_geode", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WRBlocks.BLUE_GEODE_ORE.get().defaultBlockState(), 10)).range(16).squared());
            PLATINUM_ORE_FEATURE = configure("ore_platinum", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WRBlocks.PLATINUM_ORE.get().defaultBlockState(), 9)).range(64).squared().count(20));
        }

        private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> configure(String id, ConfiguredFeature<FC, ?> cf)
        {
            return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, id, cf);
        }
    }
}
