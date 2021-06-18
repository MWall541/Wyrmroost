package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WRSounds
{
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Wyrmroost.MOD_ID);

    public static final RegistryObject<SoundEvent> WING_FLAP = entity("wings.flap");
    public static final RegistryObject<SoundEvent> FIRE_BREATH = entity("breath.fire");

    public static final RegistryObject<SoundEvent> ENTITY_LDWYRM_IDLE = entity("lesser_desertwyrm.idle");

    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_IDLE = entity("silver_glider.idle");
    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_HURT = entity("silver_glider.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_DEATH = entity("silver_glider.death");

    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_IDLE = entity("overworld_drake.idle");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_ROAR = entity("overworld_drake.roar");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_HURT = entity("overworld_drake.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_DEATH = entity("overworld_drake.death");

    public static final RegistryObject<SoundEvent> ENTITY_STALKER_IDLE = entity("roost_stalker.idle");
    public static final RegistryObject<SoundEvent> ENTITY_STALKER_HURT = entity("roost_stalker.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_STALKER_DEATH = entity("roost_stalker.death");

    public static final RegistryObject<SoundEvent> ENTITY_BFLY_IDLE = entity("butterfly_leviathan.idle");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_ROAR = entity("butterfly_leviathan.roar");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_HURT = entity("butterfly_leviathan.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_DEATH = entity("butterfly_leviathan.death");

    public static final RegistryObject<SoundEvent> ENTITY_CANARI_IDLE = entity("canari_wyvern.idle");
    public static final RegistryObject<SoundEvent> ENTITY_CANARI_HURT = entity("canari_wyvern.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_CANARI_DEATH = entity("canari_wyvern.death");

    public static final RegistryObject<SoundEvent> ENTITY_DFD_IDLE = entity("dragonfruit_drake.idle");
    public static final RegistryObject<SoundEvent> ENTITY_DFD_HURT = entity("dragonfruit_drake.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_DFD_DEATH = entity("dragonfruit_drake.death");

    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_IDLE = entity("royal_red.idle");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_HURT = entity("royal_red.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_ROAR = entity("royal_red.roar");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_DEATH = entity("royal_red.death");

    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_IDLE = entity("alpine.idle");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_HURT = entity("alpine.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_ROAR = entity("alpine.roar");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_DEATH = entity("alpine.death");

    public static final RegistryObject<SoundEvent> ENTITY_COINDRAGON_IDLE = entity("coin_dragon.idle");

    public static final RegistryObject<SoundEvent> MULCH_SOFT = register("block.mulch.soft");
    public static final RegistryObject<SoundEvent> MULCH_HARD = register("block.mulch.hard");
    public static final RegistryObject<SoundEvent> FROSTED_GRASS_SOFT = register("block.frosted_grass.soft");
    public static final RegistryObject<SoundEvent> FROSTED_GRASS_HARD = register("block.frosted_grass.hard");

    public static final RegistryObject<SoundEvent> WEATHER_SANDSTORM = register("weather.sandstorm");

    public static final RegistryObject<SoundEvent> MUSIC_ASHEN_DESERT = register("music.wyrmroost.ashen_desert");
    public static final RegistryObject<SoundEvent> MUSIC_TINCTURE_WEALD = register("music.wyrmroost.tincture_weald");

    public static RegistryObject<SoundEvent> register(String name)
    {
        return REGISTRY.register(name, () -> new SoundEvent(Wyrmroost.id(name)));
    }

    public static RegistryObject<SoundEvent> entity(String name)
    {
        return register("entity." + name);
    }

    public static class Types
    {
        public static final SoundType MULCH = blockSound(1, 1, MULCH_SOFT, MULCH_HARD);
        public static final SoundType FROSTED_GRASS = blockSound(1, 1, FROSTED_GRASS_SOFT, FROSTED_GRASS_HARD);

        private static SoundType blockSound(float volume, float pitch, Supplier<SoundEvent> soft, Supplier<SoundEvent> hard)
        {
            return new ForgeSoundType(volume, pitch, soft, hard, soft, hard, hard);
        }
    }
}
