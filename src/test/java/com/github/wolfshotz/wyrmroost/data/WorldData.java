package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import com.github.wolfshotz.wyrmroost.world.features.OseriTreeFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.ForestFlowerBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class WorldData<T> implements IDataProvider
{
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static DataGenerator generator;

    private final Map<String, JsonElement> built = new HashMap<>();
    private final String name;
    private final String path;
    private final Codec<T> codec;

    public WorldData(String name, String path, Codec<T> codec)
    {
        this.name = name;
        this.path = path;
        this.codec = codec;
    }

    protected abstract void begin();

    protected void add(String name, T obj)
    {
        built.put(name, codec.encodeStart(JsonOps.INSTANCE, obj).getOrThrow(false, Wyrmroost.LOG::error));
    }

    @Override
    public void run(DirectoryCache cache)
    {
        begin();
        built.forEach((name, json) ->
        {
            try
            {
                IDataProvider.save(GSON, cache, json, generator.getOutputFolder().resolve("data/wyrmroost/worldgen/" + path + "/" + name + ".json"));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public String getName()
    {
        return name;
    }

    static void provide(DataGenerator gen)
    {
        generator = gen;

        gen.addProvider(new ConfiguredFeatures());
        gen.addProvider(new SurfaceBuilders());
    }

    private static class ConfiguredFeatures extends WorldData<ConfiguredFeature<?, ?>>
    {
        ConfiguredFeatures()
        {
            super("World Features", "configured_feature", ConfiguredFeature.DIRECT_CODEC);
        }

        @Override
        protected void begin()
        {
            add("better_water_lake", WRWorld.Features.BETTER_LAKE.get().configured(new BlockStateFeatureConfig(Blocks.WATER.defaultBlockState())).decorated(Placement.WATER_LAKE.configured(new ChanceConfig(4))));
            add("patch_gilla_bush", Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(WRBlocks.GILLA.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE).noProjection().build()).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(7));
            add("tincture_flowers", Feature.FLOWER.configured(new BlockClusterFeatureConfig.Builder(ForestFlowerBlockStateProvider.INSTANCE, SimpleBlockPlacer.INSTANCE).build()).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(10));
            add("blue_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(new OseriTreeFeature.Config(OseriTreeFeature.Type.BLUE)).decorated(Features.Placements.HEIGHTMAP_SQUARE));
            add("gold_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(new OseriTreeFeature.Config(OseriTreeFeature.Type.GOLD)).decorated(Features.Placements.HEIGHTMAP_SQUARE));
            add("pink_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(new OseriTreeFeature.Config(OseriTreeFeature.Type.PINK)).decorated(Features.Placements.HEIGHTMAP_SQUARE));
            add("purple_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(new OseriTreeFeature.Config(OseriTreeFeature.Type.PURPLE)).decorated(Features.Placements.HEIGHTMAP_SQUARE));
            add("white_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(new OseriTreeFeature.Config(OseriTreeFeature.Type.WHITE)).decorated(Features.Placements.HEIGHTMAP_SQUARE));
        }
    }

    private static class SurfaceBuilders extends WorldData<ConfiguredSurfaceBuilder<?>>
    {
        SurfaceBuilders()
        {
            super("Surface Builders", "configured_surface_builder", ConfiguredSurfaceBuilder.DIRECT_CODEC);
        }

        @Override
        protected void begin()
        {
            add("mulch", defaulted(WRBlocks.MULCH.get(), Blocks.DIRT, Blocks.GRAVEL));
        }

        static ConfiguredSurfaceBuilder<?> defaulted(Block topSoil, Block underSoil, Block underWaterMaterial)
        {
            return SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(topSoil.defaultBlockState(), underSoil.defaultBlockState(), underWaterMaterial.defaultBlockState()));
        }
    }
}
