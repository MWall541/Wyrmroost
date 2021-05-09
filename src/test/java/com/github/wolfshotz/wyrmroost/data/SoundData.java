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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SoundData implements IDataProvider
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    final Set<SoundEvent> registered = new HashSet<>(ModUtils.getRegistryEntries(WRSounds.REGISTRY));
    final DataGenerator gen;
    final ExistingFileHelper fileHelper;

    public SoundData(DataGenerator gen, ExistingFileHelper fileHelper)
    {
        this.gen = gen;
        this.fileHelper = fileHelper;
    }

    @Override
    public void run(DirectoryCache cache) throws IOException
    {
        JsonObject obj = new JsonObject();
        registerSounds(obj);
        if (!registered.isEmpty()) throw new IOException("Unregistered Sounds Events: " + registered);
        IDataProvider.save(GSON, cache, obj, gen.getOutputFolder().resolve("assets/" + Wyrmroost.MOD_ID + "/sounds.json"));
    }

    private void registerSounds(JsonObject file)
    {
        add(WRSounds.WING_FLAP.get(), array("wyrmroost:entity/wings/%s", "flap1", "flap2", "flap3"), file);
        add(WRSounds.FIRE_BREATH.get(), file);

        add(WRSounds.ENTITY_LDWYRM_IDLE.get(), array("wyrmroost:entity/lesser_desertwyrm/%s", "idle1", "idle2"), file);

        add(WRSounds.ENTITY_SILVERGLIDER_IDLE.get(), array("wyrmroost:entity/silver_glider/%s", "idle1", "idle2", "idle3", "idle4"), file);
        add(WRSounds.ENTITY_SILVERGLIDER_HURT.get(), file);
        add(WRSounds.ENTITY_SILVERGLIDER_DEATH.get(), file);

        add(WRSounds.ENTITY_OWDRAKE_IDLE.get(), array("wyrmroost:entity/overworld_drake/%s", "idle1", "idle2", "idle3"), file);
        add(WRSounds.ENTITY_OWDRAKE_HURT.get(), array("wyrmroost:entity/overworld_drake/%s", "hurt1", "hurt2"), file);
        add(WRSounds.ENTITY_OWDRAKE_DEATH.get(), file);
        add(WRSounds.ENTITY_OWDRAKE_ROAR.get(), file);

        add(WRSounds.ENTITY_STALKER_IDLE.get(), array("wyrmroost:entity/roost_stalker/%s", "idle1", "idle2", "idle3"), file);
        add(WRSounds.ENTITY_STALKER_HURT.get(), file);
        add(WRSounds.ENTITY_STALKER_DEATH.get(), file);

        add(WRSounds.ENTITY_BFLY_IDLE.get(), array("wyrmroost:entity/butterfly_leviathan/%s", "idle1", "idle2", "idle3"), file);
        add(WRSounds.ENTITY_BFLY_HURT.get(), array("wyrmroost:entity/butterfly_leviathan/%s", "hurt1", "hurt2"), file);
        add(WRSounds.ENTITY_BFLY_ROAR.get(), file);
        add(WRSounds.ENTITY_BFLY_DEATH.get(), file);

        add(WRSounds.ENTITY_CANARI_IDLE.get(), array("wyrmroost:entity/canari_wyvern/%s", "idle1", "idle2", "idle3", "idle4"), file);
        add(WRSounds.ENTITY_CANARI_HURT.get(), array("wyrmroost:entity/canari_wyvern/%s", "hurt1", "hurt2", "hurt3"), file);
        add(WRSounds.ENTITY_CANARI_DEATH.get(), file);

        add(WRSounds.ENTITY_DFD_IDLE.get(), array("wyrmroost:entity/dragonfruit_drake/%s", "idle1", "idle2", "idle3", "idle4"), file);
        add(WRSounds.ENTITY_DFD_HURT.get(), array("wyrmroost:entity/dragonfruit_drake/%s", "hurt1", "hurt2", "hurt3", "hurt4"), file);
        add(WRSounds.ENTITY_DFD_DEATH.get(), file);

        add(WRSounds.ENTITY_ROYALRED_IDLE.get(), array("wyrmroost:entity/royal_red/%s", "idle1", "idle2"), file);
        add(WRSounds.ENTITY_ROYALRED_HURT.get(), array("wyrmroost:entity/royal_red/%s", "hurt1", "hurt2", "hurt3"), file);
        add(WRSounds.ENTITY_ROYALRED_DEATH.get(), file);
        add(WRSounds.ENTITY_ROYALRED_ROAR.get(), file);

        add(WRSounds.ENTITY_ALPINE_IDLE.get(), array("wyrmroost:entity/alpine/%s", "idle1", "idle2"), file);
        add(WRSounds.ENTITY_ALPINE_HURT.get(), array("wyrmroost:entity/alpine/%s", "hurt1", "hurt2", "hurt3"), file);
        add(WRSounds.ENTITY_ALPINE_ROAR.get(), array("wyrmroost:entity/alpine/%s", "roar", "roar1", "roar2"), file);
        add(WRSounds.ENTITY_ALPINE_DEATH.get(), file);

        add(WRSounds.ENTITY_COINDRAGON_IDLE.get(), array("wyrmroost:entity/coin_dragon/%s", "idle1", "idle2", "idle3"), file);

        add(WRSounds.WEATHER_SANDSTORM.get(), array("wyrmroost:weather/sandstorm/%s", "sandstorm1", "sandstorm2", "sandstorm3", "sandstorm4", "sandstorm5", "sandstorm6"), file);

        add(WRSounds.MUSIC_ASHEN_DESERT.get(), array(builder(Wyrmroost.id("music/game/wyrmroost/ashen_desert")).stream().build()), file);
        add(WRSounds.MUSIC_TINCTURE_WEALD.get(), array(builder(Wyrmroost.id("music/game/wyrmroost/tincture_weald")).stream().build()), file);
    }

    private void add(SoundEvent event, JsonArray sounds, JsonObject file)
    {
        String path = event.getLocation().getPath();
        JsonObject obj = new JsonObject();

        registered.remove(event);
        obj.add("sounds", sounds);
        obj.addProperty("subtitle", "subtitles." + path);
        file.add(path, obj);
    }

    private void add(SoundEvent event, JsonObject file)
    {
        JsonArray arr = new JsonArray();
        String path = event.getLocation().toString().replace(".", "/");
        validate(new ResourceLocation(path));
        arr.add(path);
        add(event, arr, file);
    }

    private JsonArray array(String pattern, String... paths)
    {
        JsonArray arr = new JsonArray();
        for (String path : paths) arr.add(validate(new ResourceLocation(String.format(pattern, path))).toString());
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
