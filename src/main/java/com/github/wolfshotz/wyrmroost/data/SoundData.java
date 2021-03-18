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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class SoundData implements IDataProvider
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Set<SoundEvent> REGISTERED = new HashSet<>();

    private final Builder soundBuilder = new Builder();
    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;

    public SoundData(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void run(DirectoryCache cache) throws IOException
    {
        JsonObject json = new JsonObject();
        registerSounds(json);
        for (SoundEvent value : ModUtils.getRegistryEntries(WRSounds.REGISTRY))
            if (!REGISTERED.contains(value))
                throw new IllegalArgumentException("Unregistered Sound event: " + value.getLocation());
        IDataProvider.save(GSON, cache, json, generator.getOutputFolder().resolve("assets/" + Wyrmroost.MOD_ID + "/sounds.json"));
    }

    public void registerSounds(JsonObject json)
    {
        getBuilder(WRSounds.WING_FLAP.get()).subtitle("Dragon wing flaps").sounds(Wyrmroost::id, "entity/other/wings/%s", "flap1", "flap2", "flap3").build(json);
        getBuilder(WRSounds.FIRE_BREATH.get()).subtitle("Dragon breathes fire").sound(Wyrmroost.id("entity/other/breath/fire_breath")).build(json);

        getBuilder(WRSounds.ENTITY_LDWYRM_IDLE.get()).subtitle("Desertwyrm clicks").sounds(Wyrmroost::id, "entity/lesser_desertwyrm/%s", "idle1", "idle2").build(json);

        getBuilder(WRSounds.ENTITY_SILVERGLIDER_IDLE.get()).subtitle("Silver Glider whistles").sounds(Wyrmroost::id, "entity/silver_glider/%s", "idle1", "idle2", "idle3", "idle4").build(json);
        getBuilder(WRSounds.ENTITY_SILVERGLIDER_HURT.get()).subtitle("Silver Glider screeches").sound(Wyrmroost.id("entity/silver_glider/hurt")).build(json);
        getBuilder(WRSounds.ENTITY_SILVERGLIDER_DEATH.get()).subtitle("Silver Glider moans").sound(Wyrmroost.id("entity/silver_glider/death")).build(json);

        getBuilder(WRSounds.ENTITY_OWDRAKE_IDLE.get()).subtitle("Overworld Drake snorts").sounds(Wyrmroost::id, "entity/overworld_drake/%s", "idle1", "idle2", "idle3").build(json);
        getBuilder(WRSounds.ENTITY_OWDRAKE_HURT.get()).subtitle("Overworld Drake outcries").sounds(Wyrmroost::id, "entity/overworld_drake/%s", "idle1", "idle2", "idle3").build(json);
        getBuilder(WRSounds.ENTITY_OWDRAKE_DEATH.get()).subtitle("Overworld Drake moans").sound(Wyrmroost.id("entity/overworld_drake/death")).build(json);
        getBuilder(WRSounds.ENTITY_OWDRAKE_ROAR.get()).subtitle("Overworld Drake roars").sound(Wyrmroost.id("entity/overworld_drake/roar")).build(json);

        getBuilder(WRSounds.ENTITY_STALKER_IDLE.get()).subtitle("Rooststalker clicks").sounds(Wyrmroost::id, "entity/roost_stalker/%s", "idle1", "idle2", "idle3").build(json);
        getBuilder(WRSounds.ENTITY_STALKER_HURT.get()).subtitle("Rooststalker screeches").sound(Wyrmroost.id("entity/roost_stalker/hurt")).build(json);
        getBuilder(WRSounds.ENTITY_STALKER_DEATH.get()).subtitle("Rooststalker moans").sound(Wyrmroost.id("entity/roost_stalker/death")).build(json);

        getBuilder(WRSounds.ENTITY_BFLY_IDLE.get()).subtitle("Butterfly Leviathan trumpets").sounds(Wyrmroost::id, "entity/butterfly_leviathan/%s", "idle1", "idle2", "idle3").build(json);
        getBuilder(WRSounds.ENTITY_BFLY_HURT.get()).subtitle("Butterfly Leviathan outcries").sounds(Wyrmroost::id, "entity/butterfly_leviathan/%s", "hurt1", "hurt2").build(json);
        getBuilder(WRSounds.ENTITY_BFLY_ROAR.get()).subtitle("Butterfly Leviathan ascends").sound(Wyrmroost.id("entity/butterfly_leviathan/roar")).build(json);
        getBuilder(WRSounds.ENTITY_BFLY_DEATH.get()).subtitle("Butterfly Leviathan dies").sound(Wyrmroost.id("entity/butterfly_leviathan/death")).build(json);

        getBuilder(WRSounds.ENTITY_CANARI_IDLE.get()).subtitle("Canari Wyvern chirps").sounds(Wyrmroost::id, "entity/canari_wyvern/%s", "idle1", "idle2", "idle3", "idle4").build(json);
        getBuilder(WRSounds.ENTITY_CANARI_HURT.get()).subtitle("Canari Wyvern screeches").sounds(Wyrmroost::id, "entity/canari_wyvern/%s", "hurt1", "hurt2", "hurt3").build(json);
        getBuilder(WRSounds.ENTITY_CANARI_DEATH.get()).subtitle("Canari Wyvern dies").sound(Wyrmroost.id("entity/canari_wyvern/death")).build(json);

        getBuilder(WRSounds.ENTITY_DFD_IDLE.get()).subtitle("Dragon Fruit mumbles").sounds(Wyrmroost::id, "entity/dragonfruit_drake/%s", "idle1", "idle2", "idle3", "idle4").build(json);
        getBuilder(WRSounds.ENTITY_DFD_HURT.get()).subtitle("Dragon Fruit screeches").sounds(Wyrmroost::id, "entity/dragonfruit_drake/%s", "hurt", "hurt1", "hurt2", "hurt3").build(json);
        getBuilder(WRSounds.ENTITY_DFD_DEATH.get()).subtitle("Dragon Fruit moans").sound(Wyrmroost.id("entity/dragonfruit_drake/death")).build(json);

        getBuilder(WRSounds.ENTITY_ROYALRED_IDLE.get()).subtitle("Royal Red grunts").sounds(Wyrmroost::id, "entity/royal_red/%s", "idle1", "idle2").build(json);
        getBuilder(WRSounds.ENTITY_ROYALRED_HURT.get()).subtitle("Royal Red screams").sounds(Wyrmroost::id, "entity/royal_red/%s", "hurt1", "hurt2").build(json);
        getBuilder(WRSounds.ENTITY_ROYALRED_DEATH.get()).subtitle("Royal Red outcries").sound(Wyrmroost.id("entity/royal_red/death")).build(json);
        getBuilder(WRSounds.ENTITY_ROYALRED_ROAR.get()).subtitle("Royal Red roars").sound(Wyrmroost.id("entity/royal_red/roar")).build(json);

        getBuilder(WRSounds.ENTITY_ALPINE_IDLE.get()).subtitle("Alpine growls").sounds(Wyrmroost::id, "entity/alpine/%s", "idle1", "idle2").build(json);
        getBuilder(WRSounds.ENTITY_ALPINE_HURT.get()).subtitle("Alpine whinse").sounds(Wyrmroost::id, "entity/alpine/%s", "hurt1", "hurt2", "hurt3").build(json);
        getBuilder(WRSounds.ENTITY_ALPINE_ROAR.get()).subtitle("Alpine roars").sounds(Wyrmroost::id, "entity/alpine/%s", "roar", "roar1", "roar2").build(json);
        getBuilder(WRSounds.ENTITY_ALPINE_DEATH.get()).subtitle("Alpine screeches").sound(Wyrmroost.id("entity/alpine/death")).build(json);

        getBuilder(WRSounds.ENTITY_COINDRAGON_IDLE.get()).subtitle("Coin Dragon chirps").sounds(Wyrmroost::id, "entity/coin_dragon/%s", "idle", "idle1", "idle2").build(json);

        getBuilder(WRSounds.WEATHER_SANDSTORM.get()).sounds(Wyrmroost::id, "weather/sandstorm/%s", "sandstorm1", "sandstorm2", "sandstorm3", "sandstorm4", "sandstorm5", "sandstorm6").build(json);

        getBuilder(WRSounds.MUSIC_ASHEN_DESERT.get()).sound(Wyrmroost.id("music/game/wyrmroost/ashen_desert")).build(json);
        getBuilder(WRSounds.MUSIC_TINCTURE_WEALD.get()).sound(Wyrmroost.id("music/game/wyrmroost/tincture_weald")).build(json);
    }

    private Builder getBuilder(SoundEvent sound)
    {
        soundBuilder.sound = sound;
        soundBuilder.json = new JsonObject();
        return soundBuilder;
    }

    @Override
    public String getName()
    {
        return "WR Sounds";
    }

    private class Builder
    {
        private SoundEvent sound;
        private JsonObject json;

        public Builder category(SoundCategory category)
        {
            json.addProperty("category", category.getName());
            return this;
        }

        public Builder subtitle(String subtitle)
        {
            json.addProperty("subtitle", subtitle);
            return this;
        }

        public Builder sound(ResourceLocation sound, double volume, double pitch)
        {
            Preconditions.checkArgument(existingFileHelper.exists(sound, ResourcePackType.CLIENT_RESOURCES, ".ogg", "sounds"),
                    "Sound does not exist in any known Resourcepack: %s", sound);

            JsonArray array;
            boolean flag = false;
            if (json.has("sounds")) array = json.getAsJsonArray("sounds");
            else
            {
                array = new JsonArray();
                flag = true;
            }

            JsonObject object = new JsonObject();
            object.addProperty("name", sound.toString());
            if (volume != 1) object.addProperty("volume", volume);
            if (pitch != 1) object.addProperty("pitch", pitch);
            array.add(object);

            if (flag) json.add("sounds", array);

            return this;
        }

        public Builder sound(ResourceLocation path)
        {
            return sound(path, 1, 1);
        }

        public Builder sounds(Function<String, ResourceLocation> rlFunction, String path, String... sounds)
        {
            for (String s : sounds) sound(rlFunction.apply(path.replace("%s", s)), 1, 1);
            return this;
        }

        public void build(JsonObject soundsFile)
        {
            soundsFile.add(sound.getLocation().getPath(), json);
            REGISTERED.add(sound);
        }
    }
}
