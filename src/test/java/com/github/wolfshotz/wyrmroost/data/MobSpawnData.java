package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.world.MobSpawnManager;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import static net.minecraft.world.biome.Biome.Category.*;

public class MobSpawnData implements IDataProvider
{
    private final DataGenerator generator;
    private DirectoryCache cache;

    public MobSpawnData(DataGenerator generator)
    {
        this.generator = generator;
    }

    public void register() throws IOException
    {
        add(WREntities.LESSER_DESERTWYRM.get(), 11, 1, 3, DESERT, EntityClassification.AMBIENT);
        add(WREntities.ROOSTSTALKER.get(), entry(7, 2, 5, PLAINS), entry(6, 2, 3, FOREST));
        add(WREntities.OVERWORLD_DRAKE.get(), entry(5, 1, 3, PLAINS), entry(4, 1, 5, SAVANNA));
        add(WREntities.SILVER_GLIDER.get(), entry(30, 2, 6, OCEAN), entry(11, 1, 4, BEACH));
        add(WREntities.DRAGON_FRUIT_DRAKE.get(), 25, 3, 6, JUNGLE);
    }

    public void add(EntityType<?> entity, MobSpawnManager.Data... entries) throws IOException
    {
        ResourceLocation name = entity.getRegistryName();
        Path path = generator.getOutputFolder().resolve("data/" + name.getNamespace() + "/mob_spawns/" + name.getPath() + ".json");
        JsonElement json = MobSpawnManager.Data
                .LIST_CODEC
                .encodeStart(JsonOps.INSTANCE, Arrays.asList(entries))
                .getOrThrow(false, Wyrmroost.LOG::error);
        IDataProvider.save(MobSpawnManager.GSON, cache, json, path);
    }

    public void add(EntityType<?> entity, int weight, int minCount, int maxCount, Biome.Category category) throws IOException
    {
        add(entity, weight, minCount, maxCount, category, null);
    }

    public void add(EntityType<?> entity, int weight, int minCount, int maxCount, Biome.Category category, EntityClassification classification) throws IOException
    {
        add(entity, entry(weight, minCount, maxCount, category, classification));
    }

    @Override
    public void run(DirectoryCache cache) throws IOException
    {
        this.cache = cache;
        register();
    }

    @Override
    public String getName()
    {
        return "MobSpawnData: Wyrmroost";
    }

    static MobSpawnManager.Data entry(int weight, int minCount, int maxCount, Biome.Category category, EntityClassification classification)
    {
        return new MobSpawnManager.Data(Optional.of(category), Optional.empty(), classification, weight, minCount, maxCount);
    }

    static MobSpawnManager.Data entry(int weight, int minCount, int maxCount, Biome.Category category)
    {
        return entry(weight, minCount, maxCount, category, null);
    }
}
