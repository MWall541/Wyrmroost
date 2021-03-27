package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.world.features.NoExposureReplacementFeature;
import com.github.wolfshotz.wyrmroost.world.features.OseriTreeFeature;
import com.github.wolfshotz.wyrmroost.world.features.SurfaceAwareLakeFeature;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
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
        {
            if (entry instanceof WREntities.Type)
            {
                WREntities.Type<?> type = (WREntities.Type<?>) entry;
                if (type.spawnBiomes != null) type.spawnBiomes.accept(event);
            }
        }

        BiomeGenerationSettingsBuilder settings = event.getGeneration();
        ConfiguredFeature<?, ?>[] features = new ConfiguredFeature[] {
                getConfiguredFeature(Features.CONFIGURED_RED_GEODE),
                getConfiguredFeature(Features.CONFIGURED_PURPLE_GEODE),
                getConfiguredFeature(Features.CONFIGURED_BLUE_GEODE),
                getConfiguredFeature(Features.CONFIGURED_PLATINUM_ORE)};
        switch (event.getCategory())
        {
            case NETHER:
                if (features[0] != null) settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, features[0]);
                break;
            case THEEND:
                if (features[1] != null) settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, features[1]);
                break;
            default:
                if (features[2] != null) settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, features[2]);
                if (features[3] != null) settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, features[3]);
        }
    }

    static RegistryKey<Biome> biomeKey(String name)
    {
        return RegistryKey.create(Registry.BIOME_REGISTRY, Wyrmroost.id(name));
    }

    public static ConfiguredFeature<?, ?> getConfiguredFeature(RegistryKey<ConfiguredFeature<?, ?>> key)
    {
        return WorldGenRegistries.CONFIGURED_FEATURE.get(key);
    }

    public static class Features
    {
        public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, Wyrmroost.MOD_ID);

        public static final RegistryObject<Feature<ReplaceBlockConfig>> NO_EXPOSE_REPLACE = REGISTRY.register("no_expose_replace", NoExposureReplacementFeature::new);
        public static final RegistryObject<Feature<BlockStateFeatureConfig>> BETTER_LAKE = REGISTRY.register("better_lake", SurfaceAwareLakeFeature::new);
        public static final RegistryObject<Feature<OseriTreeFeature.Config>> OSERI_TREE = REGISTRY.register("oseri_tree", OseriTreeFeature::new);

        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_PLATINUM_ORE = configured("ore_platinum");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_BLUE_GEODE = configured("ore_blue_geode");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_RED_GEODE = configured("ore_red_geode");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_PURPLE_GEODE = configured("ore_purple_geode");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_BLUE_OSERI_TREE = configured("blue_oseri_tree");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_GOLD_OSERI_TREE = configured("gold_oseri_tree");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_PINK_OSERI_TREE = configured("pink_oseri_tree");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_PURPLE_OSERI_TREE = configured("purple_oseri_tree");
        public static final RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_WHITE_OSERI_TREE = configured("white_oseri_tree");

        private static RegistryKey<ConfiguredFeature<?, ?>> configured(String id)
        {
            return RegistryKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, Wyrmroost.id(id));
        }
    }
}
