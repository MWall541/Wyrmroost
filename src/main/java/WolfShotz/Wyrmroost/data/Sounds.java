package WolfShotz.Wyrmroost.data;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.registry.WRSounds;
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
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.io.IOException;

public class Sounds implements IDataProvider
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final DataGenerator generator;
    private final String namespace;
    private final ExistingFileHelper existingFileHelper;

    public Sounds(DataGenerator generator, String namespace, ExistingFileHelper existingFileHelper)
    {
        this.generator = generator;
        this.namespace = namespace;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException
    {
        JsonObject json = new JsonObject();
        registerSounds(json);
        IDataProvider.save(GSON, cache, json, generator.getOutputFolder().resolve("assets/" + namespace + "/sounds.json"));
    }

    public void registerSounds(JsonObject json)
    {
        new Builder(WRSounds.WING_FLAP.get()).subtitle("Dragon Wing Flap").sounds("wyrmroost:entity/wings", "flap1", "flap2", "flap3").build(json);

        new Builder(WRSounds.ENTITY_LDWYRM_IDLE.get()).subtitle("Desertwyrm Click").sounds("wyrmroost:entity/lesser_desertwyrm", "idle1", "idle2").build(json);

        new Builder(WRSounds.ENTITY_SILVERGLIDER_IDLE.get()).subtitle("Silver Glider Whistles").sounds("wyrmroost:entity/silver_glider", "idle1", "idle2", "idle3", "idle4").build(json);
        new Builder(WRSounds.ENTITY_SILVERGLIDER_HURT.get()).subtitle("Silver Glider Hurt").sound(Wyrmroost.rl("entity/silver_glider/hurt")).build(json);
        new Builder(WRSounds.ENTITY_SILVERGLIDER_DEATH.get()).subtitle("Silver Glider Death").sound(Wyrmroost.rl("entity/silver_glider/death")).build(json);

        new Builder(WRSounds.ENTITY_OWDRAKE_IDLE.get()).subtitle("Overworld Drake Snorts").sounds("wyrmroost:entity/overworld_drake", "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_OWDRAKE_HURT.get()).subtitle("Overworld Drake Hurt").sounds("wyrmroost:entity/overworld_drake", "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_OWDRAKE_DEATH.get()).subtitle("Overworld Drake Death").sound(Wyrmroost.rl("entity/overworld_drake/death")).build(json);

        new Builder(WRSounds.ENTITY_STALKER_IDLE.get()).subtitle("Rooststalker Clicks").sounds("wyrmroost:entity/roost_stalker", "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_STALKER_HURT.get()).subtitle("Rooststalker Hurt").sound(Wyrmroost.rl("entity/roost_stalker/hurt")).build(json);
        new Builder(WRSounds.ENTITY_STALKER_DEATH.get()).subtitle("Rooststalker Hurt").sound(Wyrmroost.rl("entity/roost_stalker/death")).build(json);

        new Builder(WRSounds.ENTITY_BFLY_IDLE.get()).subtitle("Butterfly Leviathan Trumpets").sounds("wyrmroost:entity/butterfly_leviathan", "idle1", "idle2", "idle3").build(json);
        new Builder(WRSounds.ENTITY_BFLY_HURT.get()).subtitle("Butterfly Leviathan Hurt").sounds("wyrmroost:entity/butterfly_leviathan", "hurt1", "hurt2").build(json);
        new Builder(WRSounds.ENTITY_BFLY_ROAR.get()).subtitle("Butterfly Leviathan Ascension").sound(Wyrmroost.rl("entity/butterfly_leviathan/roar")).build(json);
        new Builder(WRSounds.ENTITY_BFLY_ROAR.get()).subtitle("Butterfly Leviathan Death").sound(Wyrmroost.rl("entity/butterfly_leviathan/death")).build(json);

        new Builder(WRSounds.ENTITY_CANARI_IDLE.get()).subtitle("Canari Wyvern Chirps").sounds("wyrmroost:entity/canari_wyvern", "idle1", "idle2", "idle3", "idle4").build(json);
        new Builder(WRSounds.ENTITYCANARI_HURT.get()).subtitle("Canari Wyvern Hurt").sounds("wyrmroost:entity/canari_wyvern", "hurt1", "hurt2", "hurt3").build(json);
        new Builder(WRSounds.ENTITY_CANARI_DEATH.get()).subtitle("Canari Wyvern Death").sound(Wyrmroost.rl("entity/canari_wyvern/death")).build(json);
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

        public Builder sounds(String initialPath, String... sounds)
        {
            for (String s : sounds) sound(new ResourceLocation(initialPath + "/" + s), 1, 1);
            return this;
        }

        public void build(JsonObject soundsFile)
        {
            soundsFile.add(sound.getName().getPath(), json);
        }
    }
}
