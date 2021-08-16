package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.world.MobSpawnManager;
import com.github.wolfshotz.wyrmroost.world.features.*;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WRWorld
{
    public static final RegistryKey<Dimension> THE_WYRMROOST = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, Wyrmroost.id("the_wyrmroost"));

    public static final RegistryKey<Biome> TINCTURE_WEALD = biomeKey("tincture_weald");
    public static final RegistryKey<Biome> FROST_CREVASSE = biomeKey("frost_crevasse");

    public static void onBiomeLoad(BiomeLoadingEvent event)
    {
        MobSpawnManager.load();

        Biome.Category category = event.getCategory();

        for (MobSpawnManager.Record record : MobSpawnManager.getSpawnList(category, event.getName()))
            event.getSpawns().addSpawn(record.classification, new MobSpawnInfo.Spawners(
                    record.entity,
                    record.weight,
                    record.minCount,
                    record.maxCount));

        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        switch (category)
        {
            case NETHER:
                settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.CONFIGURED_RED_GEODE.get());
                break;
            case THEEND:
                settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.CONFIGURED_PURPLE_GEODE.get());
                break;
            default:
                settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.CONFIGURED_BLUE_GEODE.get());
                settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Features.CONFIGURED_PLATINUM_ORE.get());
        }
    }

    static RegistryKey<Biome> biomeKey(String name)
    {
        return RegistryKey.create(Registry.BIOME_REGISTRY, Wyrmroost.id(name));
    }

    public static class Features
    {
        public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, Wyrmroost.MOD_ID);

        public static final RegistryObject<Feature<ReplaceBlockConfig>> NO_EXPOSE_REPLACE = REGISTRY.register("no_expose_replace", NoExposureReplacementFeature::new);
        public static final RegistryObject<Feature<BlockStateFeatureConfig>> BETTER_LAKE = REGISTRY.register("better_lake", SurfaceAwareLakeFeature::new);
        public static final RegistryObject<Feature<OseriTreeFeature.Type>> OSERI_TREE = REGISTRY.register("oseri_tree", OseriTreeFeature::new);
        public static final RegistryObject<Feature<NoFeatureConfig>> MOSS_VINES = REGISTRY.register("moss_vines", MossVinesFeature::new);
        public static final RegistryObject<Feature<RoofHangingFeature.Config>> ROOF_HANGING = REGISTRY.register("roof_hanging", RoofHangingFeature::new);

        //these are for use mainly in code, for cases where it's impossible to use jsons (for overworld adding, trees for saplings, etc.)
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_PLATINUM_ORE = configured("ore_platinum", () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WRBlocks.PLATINUM_ORE.get().defaultBlockState(), 9)).range(64).squared().count(10));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_BLUE_GEODE = configured("ore_blue_geode", () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, WRBlocks.BLUE_GEODE_ORE.get().defaultBlockState(), 10)).range(16).squared());
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_RED_GEODE = configured("ore_red_geode", () -> Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, WRBlocks.RED_GEODE_ORE.get().defaultBlockState(), 4)).range(128).squared().count(8));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_PURPLE_GEODE = configured("ore_purple_geode", () -> WRWorld.Features.NO_EXPOSE_REPLACE.get().configured(new ReplaceBlockConfig(Blocks.END_STONE.defaultBlockState(), WRBlocks.PURPLE_GEODE_ORE.get().defaultBlockState())).range(80).squared().count(45));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_BLUE_OSERI = configured("blue_oseri", () -> OSERI_TREE.get().configured(OseriTreeFeature.Type.BLUE));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_GOLD_OSERI = configured("gold_oseri", () -> OSERI_TREE.get().configured(OseriTreeFeature.Type.GOLD));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_PINK_OSERI = configured("pink_oseri", () -> OSERI_TREE.get().configured(OseriTreeFeature.Type.PINK));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_PURPLE_OSERI = configured("purple_oseri", () -> OSERI_TREE.get().configured(OseriTreeFeature.Type.PURPLE));
        public static final Lazy<ConfiguredFeature<?, ?>> CONFIGURED_WHITE_OSERI = configured("white_oseri", () -> OSERI_TREE.get().configured(OseriTreeFeature.Type.WHITE));

        private static RegistryKey<ConfiguredFeature<?, ?>> configured(String id)
        {
            return RegistryKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, Wyrmroost.id(id));
        }

        /**
         * @deprecated Directly registering like this is dumb and the only reason im doing is because the default
         * registry doesn't hold custom ones, and its impossible to get the dynamic registry holding the custom ones.
         */
        @Deprecated
        private static Lazy<ConfiguredFeature<?, ?>> configured(String id, Supplier<ConfiguredFeature<?, ?>> sup)
        {
            return Lazy.of(() -> Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Wyrmroost.id(id), sup.get()));
        }
    }
}
