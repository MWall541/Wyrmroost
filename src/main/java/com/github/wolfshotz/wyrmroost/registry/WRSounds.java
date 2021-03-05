package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WRSounds
{
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Wyrmroost.MOD_ID);

    public static final RegistryObject<SoundEvent> WING_FLAP = register("wing.flap");
    public static final RegistryObject<SoundEvent> FIRE_BREATH = register("breathweapon.fire");

    public static final RegistryObject<SoundEvent> ENTITY_LDWYRM_IDLE = entity("ldwyrm.idle");

    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_IDLE = entity("silverglider.idle");
    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_HURT = entity("silverglider.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_DEATH = entity("silverglider.death");

    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_IDLE = entity("owdrake.idle");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_ROAR = entity("owdrake.roar");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_HURT = entity("owdrake.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_DEATH = entity("owdrake.death");

    public static final RegistryObject<SoundEvent> ENTITY_STALKER_IDLE = entity("stalker.idle");
    public static final RegistryObject<SoundEvent> ENTITY_STALKER_HURT = entity("stalker.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_STALKER_DEATH = entity("stalker.death");

    public static final RegistryObject<SoundEvent> ENTITY_BFLY_IDLE = entity("bfly.idle");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_ROAR = entity("bfly.roar");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_HURT = entity("bfly.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_DEATH = entity("bfly.death");

    public static final RegistryObject<SoundEvent> ENTITY_CANARI_IDLE = entity("canari.idle");
    public static final RegistryObject<SoundEvent> ENTITY_CANARI_HURT = entity("canari.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_CANARI_DEATH = entity("canari.death");

    public static final RegistryObject<SoundEvent> ENTITY_DFD_IDLE = entity("dfd.idle");
    public static final RegistryObject<SoundEvent> ENTITY_DFD_HURT = entity("dfd.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_DFD_DEATH = entity("dfd.death");

    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_IDLE = entity("royalred.idle");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_HURT = entity("royalred.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_ROAR = entity("royalred.roar");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_DEATH = entity("royalred.death");

    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_IDLE = entity("alpine.idle");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_HURT = entity("alpine.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_ROAR = entity("alpine.roar");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_DEATH = entity("alpine.death");

    public static final RegistryObject<SoundEvent> ENTITY_COINDRAGON_IDLE = entity("coindragon.idle");

    public static final RegistryObject<SoundEvent> WEATHER_SANDSTORM = register("weather.sandstorm");

    public static final RegistryObject<SoundEvent> MUSIC_ASHEN_DESERT = register("music.ashen_desert");

    public static RegistryObject<SoundEvent> register(String name)
    {
        return REGISTRY.register(name, () -> new SoundEvent(Wyrmroost.rl(name)));
    }

    public static RegistryObject<SoundEvent> entity(String name)
    {
        return register("entity." + name);
    }
}
