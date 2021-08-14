package com.github.wolfshotz.wyrmroost.world;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.profiler.EmptyProfiler;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class MobSpawnManager extends JsonReloadListener
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final MobSpawnManager INSTANCE = new MobSpawnManager();

    private final Map<Biome.Category, List<Pair<EntityClassification, MobSpawnInfo.Spawners>>> categoryLookup = new HashMap<>();
    private final Map<ResourceLocation, List<Pair<EntityClassification, MobSpawnInfo.Spawners>>> biomeLookup = new HashMap<>();
    private boolean initialized = false;

    public MobSpawnManager()
    {
        super(GSON, "mob_spawns");
    }

    private void runStupidAndDumbHackySolutionToBypassMojangsBullShit()
    {
        FallbackResourceManager resources = new FallbackResourceManager(ResourcePackType.SERVER_DATA, Wyrmroost.MOD_ID);
        resources.add(new ModFileResourcePack(ModList.get().getModFileById(Wyrmroost.MOD_ID).getFile())); // todo: figure out a way to expose this to all datapacks
        Map<ResourceLocation, JsonElement> entries = prepare(resources, EmptyProfiler.INSTANCE);
        apply(entries, resources, EmptyProfiler.INSTANCE);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> entries, IResourceManager manager, IProfiler profiler)
    {
        for (Map.Entry<ResourceLocation, JsonElement> entry : entries.entrySet())
        {
            EntityType<?> entity = ForgeRegistries.ENTITIES.getValue(entry.getKey());
            if (entity == null)
            {
                Wyrmroost.LOG.warn("Unknown entity type: {} for MobSpawnInfo, ignoring.", entry.getKey());
                continue;
            }

            Optional<List<Data>> result = Data.LIST_CODEC
                    .decode(JsonOps.INSTANCE, entry.getValue())
                    .map(Pair::getFirst)
                    .resultOrPartial(Wyrmroost.LOG::error);
            if (!result.isPresent()) continue;

            for (Data data : result.get())
            {
                boolean flag = false;
                Pair<EntityClassification, MobSpawnInfo.Spawners> spawner = Pair.of(data.classification, new MobSpawnInfo.Spawners(entity, data.weight, data.minCount, data.maxCount));
                if (data.category.isPresent())
                {
                    categoryLookup.computeIfAbsent(data.category.get(), $ -> new ArrayList<>()).add(spawner);
                    flag = true;
                }

                if (data.biomes.isPresent())
                {
                    for (ResourceLocation id : data.biomes.get())
                    {
                        biomeLookup.computeIfAbsent(id, $ -> new ArrayList<>()).add(spawner);
                        flag = true;
                    }
                }

                if (!flag)
                    Wyrmroost.LOG.warn("Mob Spawn data defined for {} with no specified category or biomes!", entity);
            }

        }
    }

    public List<Pair<EntityClassification, MobSpawnInfo.Spawners>> getSpawnList(Biome.Category category, ResourceLocation biome)
    {
        if (!initialized)
        {
            runStupidAndDumbHackySolutionToBypassMojangsBullShit();
            initialized = true;
        }

        List<Pair<EntityClassification, MobSpawnInfo.Spawners>> spawners = categoryLookup.get(category);
        if (spawners == null)
        {
            if ((spawners = biomeLookup.get(biome)) == null) return Collections.emptyList();
        }
        else biomeLookup.remove(biome);
        return spawners;
    }

    public static class Data
    {
        private static final Codec<Data> CODEC = RecordCodecBuilder.create(o -> o.group(
                Biome.Category.CODEC.optionalFieldOf("biome_category").forGetter(e -> e.category),
                ResourceLocation.CODEC.listOf().optionalFieldOf("biomes").forGetter(e -> e.biomes),
                EntityClassification.CODEC.optionalFieldOf("classification").xmap(p -> p.orElse(EntityClassification.CREATURE), Optional::ofNullable).forGetter(e -> e.classification),
                Codec.INT.fieldOf("weight").forGetter(e -> e.weight),
                Codec.INT.fieldOf("min_count").forGetter(e -> e.minCount),
                Codec.INT.fieldOf("max_count").forGetter(e -> e.maxCount)
        ).apply(o, Data::new));
        public static final Codec<List<Data>> LIST_CODEC = CODEC.listOf();

        public final Optional<Biome.Category> category;
        private final Optional<List<ResourceLocation>> biomes;
        public final EntityClassification classification;
        public final int weight;
        public final int minCount;
        public final int maxCount;

        public Data(Optional<Biome.Category> category, Optional<List<ResourceLocation>> biomes, EntityClassification classification, int weight, int minCount, int maxCount)
        {
            this.category = category;
            this.biomes = biomes;
            this.classification = classification;
            this.weight = weight;
            this.minCount = minCount;
            this.maxCount = maxCount;
        }
    }
}
