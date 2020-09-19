package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
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
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.io.IOException;
import java.util.Set;
import java.util.function.Function;

public class SoundData implements IDataProvider
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Set<SoundEvent> registered = Sets.newHashSet();

    private final DataGenerator generator;
    private final ExistingFileHelper existingFileHelper;

    public SoundData(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException
    {
        JsonObject json = new JsonObject();
        registerSounds(json);
        for (SoundEvent value : ModUtils.getRegistryEntries(WRSounds.REGISTRY))
            if (!registered.contains(value))
                throw new IllegalArgumentException("Unregistered Sound event: " + value.toString());
        IDataProvider.save(GSON, cache, json, generator.getOutputFolder().resolve("assets/" + Wyrmroost.MOD_ID + "/sounds.json"));
    }

    public void registerSounds(JsonObject json)
    {
        new Builder(WRSounds.WING_FLAP.get()).subtitle("Dragon Wing Flap").sounds(s -> Wyrmroost.rl("entity/other/wings" + s), "flap1", "flap2", "flap3").build(json);

        new Builder(WRSounds.ENTITY_LDWYRM_IDLE.get()).subtitle("Desertwyrm Click").sounds(s -> Wyrmroost.rl("entity/lesser_desertwyrm" + s), "idle1", "idle2").build(json);

        new Builder(WRSounds.ENTITY_SILVERGLIDER_IDLE.get()).subtitle("Silver Glider Whistles").sounds(s -> Wyrmroost.rl("entity/silver_glider" + s), "idle1", "idle2", "idle3", "idle4").build(json);
        new Builder(WRSounds.ENTITY_SILVERGLIDER_HURT.get()).subtitle("Silver Glider Screech").sound(Wyrmroost.rl("entity/silver_glider/hurt")).build(json);
        new Builder(WRSounds.ENTITY_SILVERGLIDER_DEATH.get()).subtitle("Silver Glider Moan").sound(Wyrmroost.rl("entity/silver_glider/death")).build(json);

        new Builder(WRSounds.ENTITY_OWDRAKE_IDLE.get()).subtitle("Overworld Drake Snorts").sounds(s -> Wyrmroost.rl("entity/overworld_drake" + s), "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_OWDRAKE_HURT.get()).subtitle("Overworld Drake Outcry").sounds(s -> Wyrmroost.rl("entity/overworld_drake" + s), "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_OWDRAKE_DEATH.get()).subtitle("Overworld Drake Moan").sound(Wyrmroost.rl("entity/overworld_drake/death")).build(json);
        new Builder(WRSounds.ENTITY_OWDRAKE_ROAR.get()).subtitle("Overworld Drake Roar").category(SoundCategory.HOSTILE).sound(Wyrmroost.rl("entity/overworld_drake/roar")).build(json);

        new Builder(WRSounds.ENTITY_STALKER_IDLE.get()).subtitle("Rooststalker Clicks").sounds(s -> Wyrmroost.rl("entity/roost_stalker" + s), "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_STALKER_HURT.get()).subtitle("Rooststalker Screech").sound(Wyrmroost.rl("entity/roost_stalker/hurt")).build(json);
        new Builder(WRSounds.ENTITY_STALKER_DEATH.get()).subtitle("Rooststalker Moan").sound(Wyrmroost.rl("entity/roost_stalker/death")).build(json);

        new Builder(WRSounds.ENTITY_BFLY_IDLE.get()).subtitle("Butterfly Leviathan Trumpets").sounds(s -> Wyrmroost.rl("entity/butterfly_leviathan" + s), "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_BFLY_HURT.get()).subtitle("Butterfly Leviathan Outcry").sounds(s -> Wyrmroost.rl("entity/butterfly_leviathan" + s), "hurt1", "hurt2").build(json);
        new Builder(WRSounds.ENTITY_BFLY_ROAR.get()).subtitle("Butterfly Leviathan Ascension").sound(Wyrmroost.rl("entity/butterfly_leviathan/roar")).build(json);
        new Builder(WRSounds.ENTITY_BFLY_DEATH.get()).subtitle("Butterfly Leviathan Death").sound(Wyrmroost.rl("entity/butterfly_leviathan/death")).build(json);

        new Builder(WRSounds.ENTITY_CANARI_IDLE.get()).subtitle("Canari Wyvern Chirps").sounds(s -> Wyrmroost.rl("entity/canari_wyvern" + s), "idle1", "idle2", "idle3", "idle4").build(json);
        new Builder(WRSounds.ENTITY_CANARI_HURT.get()).subtitle("Canari Wyvern Screech").sounds(s -> Wyrmroost.rl("entity/canari_wyvern" + s), "hurt1", "hurt2", "hurt3").build(json);
        new Builder(WRSounds.ENTITY_CANARI_DEATH.get()).subtitle("Canari Wyvern Death").sound(Wyrmroost.rl("entity/canari_wyvern/death")).build(json);

        new Builder(WRSounds.ENTITY_ROYALRED_IDLE.get()).subtitle("Royal Red Grunts").sounds(s -> Wyrmroost.rl("entity/royal_red" + s), "idle1", "idle2").build(json);
        new Builder(WRSounds.ENTITY_ROYALRED_HURT.get()).subtitle("Royal Red Scream").sounds(s -> Wyrmroost.rl("entity/royal_red" + s), "hurt1", "hurt2").build(json);
        new Builder(WRSounds.ENTITY_ROYALRED_DEATH.get()).subtitle("Royal Red Outcry").sound(Wyrmroost.rl("entity/royal_red/death")).build(json);
        new Builder(WRSounds.ENTITY_ROYALRED_ROAR.get()).subtitle("Royal Red Roar").sound(Wyrmroost.rl("entity/royal_red/roar")).build(json);
        new Builder(WRSounds.ENTITY_ROYALRED_BREATH.get()).subtitle("Royal Red Breathes Fire").sound(Wyrmroost.rl("entity/royal_red/scree")).build(json);

        new Builder(WRSounds.ENTITY_ALPINE_IDLE.get()).subtitle("Alpine Growls").sounds(s -> Wyrmroost.rl("entity/alpine" + s), "idle1", "idle2").build(json);
        new Builder(WRSounds.ENTITY_ALPINE_HURT.get()).subtitle("Alpine Whine").sounds(s -> Wyrmroost.rl("entity/alpine" + s), "hurt1", "hurt2", "hurt3").build(json);
        new Builder(WRSounds.ENTITY_ALPINE_ROAR.get()).subtitle("Alpine Roar").sounds(s -> Wyrmroost.rl("entity/alpine" + s), "roar", "roar1", "roar2").build(json);
    }

    @Override
    public String getName() { return "WR Sounds"; }

    private class Builder
    {
        private final SoundEvent sound;
        private final JsonObject json;

        private Builder(SoundEvent sound)
        {
            this.sound = sound;
            this.json = new JsonObject();
        }

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

        public Builder sound(ResourceLocation path) { return sound(path, 1, 1); }

        public Builder sounds(Function<String, ResourceLocation> initialPath, String... sounds)
        {
            for (String s : sounds) sound(initialPath.apply("/" + s), 1, 1);
            return this;
        }

        public void build(JsonObject soundsFile)
        {
            soundsFile.add(sound.getName().getPath().replace("_", "."), json);
            registered.add(sound);
        }
    }
}
