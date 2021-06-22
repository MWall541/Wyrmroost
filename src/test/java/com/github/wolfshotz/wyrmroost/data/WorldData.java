package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import com.github.wolfshotz.wyrmroost.world.features.OseriTreeFeature;
import com.github.wolfshotz.wyrmroost.world.features.RoofHangingFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public abstract class WorldData<T> implements IDataProvider
{
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static DataGenerator generator;

    private final String name;
    private final String path;
    private final Codec<T> codec;
    private DirectoryCache cache;

    public WorldData(String name, String path, Codec<T> codec)
    {
        this.name = name;
        this.path = path;
        this.codec = codec;
    }

    protected abstract void begin();

    protected void add(String name, T obj)
    {
        try
        {
            IDataProvider.save(GSON, cache, codec.encodeStart(JsonOps.INSTANCE, obj).getOrThrow(false, Wyrmroost.LOG::error), generator.getOutputFolder().resolve("data/wyrmroost/worldgen/" + path + "/" + name + ".json"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(DirectoryCache cache)
    {
        this.cache = cache;
        begin();
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
            add("blue_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(OseriTreeFeature.Type.BLUE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(17));
            add("gold_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(OseriTreeFeature.Type.GOLD).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(6));
            add("pink_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(OseriTreeFeature.Type.PINK).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(4).count(2));
            add("purple_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(OseriTreeFeature.Type.PURPLE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(4).count(2));
            add("white_oseri_tree", WRWorld.Features.OSERI_TREE.get().configured(OseriTreeFeature.Type.WHITE).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(2).count(2));
            add("moss_vines", WRWorld.Features.MOSS_VINES.get().configured(NoFeatureConfig.INSTANCE).squared().count(35));
            add("silver_moss", WRWorld.Features.ROOF_HANGING.get().configured(new RoofHangingFeature.Config(WRBlocks.SILVER_MOSS.get(), 63)).squared().count(30));
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
