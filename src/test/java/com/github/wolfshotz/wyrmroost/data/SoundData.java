package com.github.wolfshotz.wyrmroost.data;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SoundData implements IDataProvider
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    final Set<SoundEvent> registered = new HashSet<>(ModUtils.getRegistryEntries(WRSounds.REGISTRY));
    final DataGenerator gen;
    final ExistingFileHelper fileHelper;
    final JsonObject file;

    public SoundData(DataGenerator gen, ExistingFileHelper fileHelper)
    {
        this.gen = gen;
        this.fileHelper = fileHelper;
        this.file = new JsonObject();
    }

    @Override
    public void run(DirectoryCache cache) throws IOException
    {
        registerSounds();
        if (!registered.isEmpty())
            throw new IOException("Unregistered SoundEvents: " + registered.stream().map(ForgeRegistryEntry::getRegistryName).collect(Collectors.toList()));
        IDataProvider.save(GSON, cache, file, gen.getOutputFolder().resolve("assets/" + Wyrmroost.MOD_ID + "/sounds.json"));
    }

    private void registerSounds()
    {
        add(WRSounds.WING_FLAP.get(), array("wyrmroost:entity/wings/%s", "flap1", "flap2", "flap3"));
        add(WRSounds.FIRE_BREATH.get());

        add(WRSounds.ENTITY_LDWYRM_IDLE.get(), array("wyrmroost:entity/lesser_desertwyrm/%s", "idle1", "idle2"));

        add(WRSounds.ENTITY_SILVERGLIDER_IDLE.get(), array("wyrmroost:entity/silver_glider/%s", "idle1", "idle2", "idle3"));
        add(WRSounds.ENTITY_SILVERGLIDER_HURT.get());
        add(WRSounds.ENTITY_SILVERGLIDER_DEATH.get());

        add(WRSounds.ENTITY_OWDRAKE_IDLE.get(), array("wyrmroost:entity/overworld_drake/%s", "idle1", "idle2", "idle3"));
        add(WRSounds.ENTITY_OWDRAKE_HURT.get(), array("wyrmroost:entity/overworld_drake/%s", "hurt1", "hurt2"));
        add(WRSounds.ENTITY_OWDRAKE_DEATH.get());
        add(WRSounds.ENTITY_OWDRAKE_ROAR.get());

        add(WRSounds.ENTITY_STALKER_IDLE.get(), array("wyrmroost:entity/roost_stalker/%s", "idle1", "idle2", "idle3"));
        add(WRSounds.ENTITY_STALKER_HURT.get());
        add(WRSounds.ENTITY_STALKER_DEATH.get());

        add(WRSounds.ENTITY_BFLY_IDLE.get(), array("wyrmroost:entity/butterfly_leviathan/%s", "idle1", "idle2", "idle3"));
        add(WRSounds.ENTITY_BFLY_HURT.get(), array("wyrmroost:entity/butterfly_leviathan/%s", "hurt1", "hurt2"));
        add(WRSounds.ENTITY_BFLY_ROAR.get());
        add(WRSounds.ENTITY_BFLY_DEATH.get());

        add(WRSounds.ENTITY_CANARI_IDLE.get(), array("wyrmroost:entity/canari_wyvern/%s", "idle1", "idle2", "idle3", "idle4"));
        add(WRSounds.ENTITY_CANARI_HURT.get(), array("wyrmroost:entity/canari_wyvern/%s", "hurt1", "hurt2", "hurt3"));
        add(WRSounds.ENTITY_CANARI_DEATH.get());

        add(WRSounds.ENTITY_DFD_IDLE.get(), array("wyrmroost:entity/dragonfruit_drake/%s", "idle1", "idle2", "idle3", "idle4"));
        add(WRSounds.ENTITY_DFD_HURT.get(), array("wyrmroost:entity/dragonfruit_drake/%s", "hurt1", "hurt2", "hurt3", "hurt4"));
        add(WRSounds.ENTITY_DFD_DEATH.get());

        add(WRSounds.ENTITY_ROYALRED_IDLE.get(), array("wyrmroost:entity/royal_red/%s", "idle1", "idle2"));
        add(WRSounds.ENTITY_ROYALRED_HURT.get(), array("wyrmroost:entity/royal_red/%s", "hurt1", "hurt2", "hurt3"));
        add(WRSounds.ENTITY_ROYALRED_DEATH.get());
        add(WRSounds.ENTITY_ROYALRED_ROAR.get());

        add(WRSounds.ENTITY_ALPINE_IDLE.get(), array("wyrmroost:entity/alpine/%s", "idle1", "idle2"));
        add(WRSounds.ENTITY_ALPINE_HURT.get(), array("wyrmroost:entity/alpine/%s", "hurt1", "hurt2", "hurt3"));
        add(WRSounds.ENTITY_ALPINE_ROAR.get(), array("wyrmroost:entity/alpine/%s", "roar", "roar1", "roar2"));
        add(WRSounds.ENTITY_ALPINE_DEATH.get());

        add(WRSounds.ENTITY_COINDRAGON_IDLE.get(), array("wyrmroost:entity/coin_dragon/%s", "idle1", "idle2", "idle3"));

        add(WRSounds.MULCH_SOFT.get(), array("wyrmroost:block/mulch/%s", "soft1", "soft2", "soft3"));
        add(WRSounds.MULCH_HARD.get(), array("wyrmroost:block/mulch/%s", "hard1", "hard2", "hard3"));
        add(WRSounds.FROSTED_GRASS_SOFT.get(), array("wyrmroost:block/frosted_grass/%s", "soft1", "soft2", "soft3"));
        add(WRSounds.FROSTED_GRASS_HARD.get(), array("wyrmroost:block/frosted_grass/%s", "hard1", "hard2", "hard3"));

        add(WRSounds.WEATHER_SANDSTORM.get(), array("wyrmroost:weather/sandstorm/%s", "sandstorm1", "sandstorm2", "sandstorm3", "sandstorm4", "sandstorm5", "sandstorm6"));

        add(WRSounds.MUSIC_ASHEN_DESERT.get(), array(builder(Wyrmroost.id("music/game/wyrmroost/ashen_desert")).stream().build()));
        add(WRSounds.MUSIC_TINCTURE_WEALD.get(), array(builder(Wyrmroost.id("music/game/wyrmroost/tincture_weald")).stream().build()));
    }

    private void add(SoundEvent event, JsonArray sounds)
    {
        String path = event.getLocation().getPath();
        JsonObject obj = new JsonObject();

        registered.remove(event);
        obj.add("sounds", sounds);
        obj.addProperty("subtitle", "subtitles." + path);
        file.add(path, obj);
    }

    private void add(SoundEvent event)
    {
        JsonArray arr = new JsonArray();
        String path = event.getLocation().toString().replace(".", "/");
        validate(new ResourceLocation(path));
        arr.add(path);
        add(event, arr);
    }

    private JsonArray array(String pattern, String... paths)
    {
        JsonArray arr = new JsonArray();
        for (String path : paths) arr.add(validate(new ResourceLocation(String.format(pattern, path))).toString());
        return arr;
    }

    private JsonArray array(String pattern, Consumer<SoundBuilder> consumer, String... paths)
    {
        JsonArray arr = new JsonArray();
        for (String path : paths)
        {
            SoundBuilder builder = builder(new ResourceLocation(String.format(pattern, path)));
            consumer.accept(builder);
            arr.add(builder.build());
        }
        return arr;
    }

    private JsonArray array(JsonObject... objects)
    {
        JsonArray arr = new JsonArray();
        for (JsonObject object : objects) arr.add(object);
        return arr;
    }

    private ResourceLocation validate(ResourceLocation resource)
    {
        Preconditions.checkArgument(fileHelper.exists(resource, ResourcePackType.CLIENT_RESOURCES, ".ogg", "sounds"),
                "Sound does not exist in any known Resourcepack: %s", resource);
        return resource;
    }

    private SoundBuilder builder(ResourceLocation rl)
    {
        return new SoundBuilder(rl);
    }

    @Override
    public String getName()
    {
        return "WR Sounds";
    }

    private class SoundBuilder
    {
        final String name;
        float volume = 1;
        float pitch = 1;
        int weight = 1;
        boolean stream = false;
        int attDist = 16;
        boolean preload = false;
        boolean event = false;

        public SoundBuilder(ResourceLocation name)
        {
            validate(name);
            this.name = name.toString();
        }

        public SoundBuilder volumePitch(float volume, float pitch)
        {
            this.volume = volume;
            this.pitch = pitch;
            return this;
        }

        public SoundBuilder volume(float volume)
        {
            this.volume = volume;
            return this;
        }

        public SoundBuilder pitch(float pitch)
        {
            this.pitch = pitch;
            return this;
        }

        public SoundBuilder weight(int weight)
        {
            this.weight = weight;
            return this;
        }

        public SoundBuilder stream()
        {
            this.stream = true;
            return this;
        }

        public SoundBuilder attenuationDist(int dist)
        {
            this.attDist = dist;
            return this;
        }

        public SoundBuilder preload()
        {
            this.preload = true;
            return this;
        }

        public SoundBuilder event()
        {
            this.event = true;
            return this;
        }

        public JsonObject build()
        {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", name);
            if (volume != 1) obj.addProperty("volume", volume);
            if (pitch != 1) obj.addProperty("pitch", pitch);
            if (weight != 1) obj.addProperty("weight", weight);
            if (stream) obj.addProperty("stream", true);
            if (attDist != 16) obj.addProperty("attenuation_distance", attDist);
            if (event) obj.addProperty("type", "event");
            return obj;
        }
    }
}
