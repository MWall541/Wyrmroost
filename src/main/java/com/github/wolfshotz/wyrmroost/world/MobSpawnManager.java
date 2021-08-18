package com.github.wolfshotz.wyrmroost.world;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.biome.Biome.Category.*;

public class MobSpawnManager
{
    public static final Path CONFIG_FILE_PATH = FMLPaths.GAMEDIR.get().resolve("config/" + Wyrmroost.MOD_ID + "-mob-spawns.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Multimap<Biome.Category, Record> BY_CATEGORY = ArrayListMultimap.create();
    private static final Multimap<ResourceLocation, Record> BY_BIOME = ArrayListMultimap.create();
    private static boolean initalized;

    public static Collection<Record> getSpawnList(Biome.Category category, ResourceLocation biome)
    {
        Collection<Record> records;
        if ((records = BY_CATEGORY.get(category)) != null)
        {
            records.removeIf(r -> r.biomeBlacklist.isPresent() && r.biomeBlacklist.get().contains(biome));
            return records;
        }
        if ((records = BY_BIOME.get(biome)) != null)
            return records;
        return Collections.emptyList();
    }

    public static void load()
    {
        if (initalized) return;

        initalized = true;
        Wyrmroost.LOG.info("Loading Biome mob spawn entries...");
        try
        {
            Path p = CONFIG_FILE_PATH;
            JsonElement json;
            if (!Files.exists(p))
            {
                Files.createFile(p);
                json = Record.CODEC
                        .encodeStart(JsonOps.INSTANCE, defaultList())
                        .getOrThrow(false, Wyrmroost.LOG::error);
                JsonWriter writer = new JsonWriter(Files.newBufferedWriter(p));
                writer.jsonValue(GSON.toJson(json));
                writer.close();
            }
            else
            {
                BufferedReader reader = Files.newBufferedReader(p);
                json = JSONUtils.fromJson(GSON, reader, JsonElement.class);
                reader.close();
            }

            if (json == null)
            {
                Wyrmroost.LOG.error("Could not load Wyrmroost mob spawn data as it is null or empty.");
                return;
            }

            parse(json);
            Wyrmroost.LOG.info("Biome mob spawn entries successfully deserialized.");
        }
        catch (Exception e)
        {
            Wyrmroost.LOG.error("Could not load Wyrmroost mob spawn data. Something went horrifically wrong...", e);
        }
    }

    public static void close()
    {
        initalized = false;
        BY_CATEGORY.clear();
        BY_BIOME.clear();
    }

    private static void parse(JsonElement json)
    {
        List<Record> result = Record.CODEC
                .decode(JsonOps.INSTANCE, json)
                .map(Pair::getFirst)
                .getOrThrow(false, Wyrmroost.LOG::error);

        for (Record record : result)
        {
            record.category.ifPresent(category -> BY_CATEGORY.put(category, record));
            record.biomes.ifPresent(l -> l.forEach(id -> BY_BIOME.put(id, record)));
        }
    }

    private static List<Record> defaultList()
    {
        return ImmutableList.of(
                rec(WREntities.LESSER_DESERTWYRM.get(), 3, 1, 3, DESERT, EntityClassification.AMBIENT),
                rec(WREntities.ROOSTSTALKER.get(), 6, 2, 5, PLAINS),
                rec(WREntities.ROOSTSTALKER.get(), 7, 1, 4, FOREST),
                rec(WREntities.OVERWORLD_DRAKE.get(), 5, 1, 3, PLAINS),
                rec(WREntities.OVERWORLD_DRAKE.get(), 4, 1, 5, SAVANNA),
                rec(WREntities.SILVER_GLIDER.get(), 20, 2, 6, OCEAN),
                rec(WREntities.SILVER_GLIDER.get(), 10, 1, 4, BEACH),
                rec(WREntities.DRAGON_FRUIT_DRAKE.get(), 25, 3, 6, JUNGLE)
        );
    }

    private static Record rec(EntityType<?> entity, int weight, int minCount, int maxCount, Biome.Category category)
    {
        return rec(entity, weight, minCount, maxCount, category, EntityClassification.CREATURE);
    }

    private static Record rec(EntityType<?> entity, int weight, int minCount, int maxCount, Biome.Category category, EntityClassification classification)
    {
        return new Record(entity, Optional.of(category), Optional.empty(), Optional.empty(), classification, weight, minCount, maxCount);
    }

    public static class Record
    {
        public static final Codec<List<Record>> CODEC = Codec.list(RecordCodecBuilder.create(o -> o.group(
                Registry.ENTITY_TYPE.fieldOf("entity_type").forGetter(e -> e.entity),
                Biome.Category.CODEC.optionalFieldOf("biome_category").forGetter(e -> e.category),
                ResourceLocation.CODEC.listOf().optionalFieldOf("biomes").forGetter(e -> e.biomes),
                ResourceLocation.CODEC.listOf().optionalFieldOf("biome_blacklist").xmap(e -> e.map(ImmutableSet::copyOf), e -> e.map(ImmutableList::copyOf)).forGetter(e -> e.biomeBlacklist),
                EntityClassification.CODEC.optionalFieldOf("classification").xmap(p -> p.orElse(EntityClassification.CREATURE), Optional::ofNullable).forGetter(e -> e.classification),
                Codec.INT.fieldOf("weight").forGetter(e -> e.weight),
                Codec.INT.fieldOf("min_count").forGetter(e -> e.minCount),
                Codec.INT.fieldOf("max_count").forGetter(e -> e.maxCount)
        ).apply(o, Record::new)));

        public final EntityType<?> entity;
        public final Optional<Biome.Category> category;
        public final Optional<List<ResourceLocation>> biomes;
        public final Optional<ImmutableSet<ResourceLocation>> biomeBlacklist; // for boosted performance during "contains" spam
        public final EntityClassification classification;
        public final int weight;
        public final int minCount;
        public final int maxCount;

        public Record(EntityType<?> entity, Optional<Biome.Category> category, Optional<List<ResourceLocation>> biomes, Optional<ImmutableSet<ResourceLocation>> biomeBlacklist, EntityClassification classification, int weight, int minCount, int maxCount)
        {
            this.entity = entity;
            this.category = category;
            this.biomes = biomes;
            this.biomeBlacklist = biomeBlacklist;
            this.classification = classification;
            this.weight = weight;
            this.minCount = minCount;
            this.maxCount = maxCount;

            if (!category.isPresent() && !biomes.isPresent())
                throw new IllegalArgumentException("Must have defined a biome category OR a list of biomes. Both is empty...");
        }
    }
}
