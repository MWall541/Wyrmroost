package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WRSounds
{
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Wyrmroost.MOD_ID);


    public static final RegistryObject<SoundEvent> WING_FLAP = register("wing_flap");

    public static final RegistryObject<SoundEvent> ENTITY_LDWYRM_IDLE = entity("ldwyrm_idle");

    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_IDLE = entity("silverglider_idle");
    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_HURT = entity("silverglider_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_SILVERGLIDER_DEATH = entity("silverglider_death");

    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_IDLE = entity("owdrake_idle");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_ROAR = entity("owdrake_roar");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_HURT = entity("owdrake_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_OWDRAKE_DEATH = entity("owdrake_death");

    public static final RegistryObject<SoundEvent> ENTITY_STALKER_IDLE = entity("stalker_idle");
    public static final RegistryObject<SoundEvent> ENTITY_STALKER_HURT = entity("stalker_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_STALKER_DEATH = entity("stalker_death");

    public static final RegistryObject<SoundEvent> ENTITY_BFLY_IDLE = entity("bfly_idle");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_ROAR = entity("bfly_roar");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_HURT = entity("bfly_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_BFLY_DEATH = entity("bfly_death");

    public static final RegistryObject<SoundEvent> ENTITY_CANARI_IDLE = entity("canari_idle");
    public static final RegistryObject<SoundEvent> ENTITY_CANARI_HURT = entity("canari_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_CANARI_DEATH = entity("canari_death");

    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_IDLE = entity("royalred_idle");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_HURT = entity("royalred_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_ROAR = entity("royalred_roar");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_DEATH = entity("royalred_death");
    public static final RegistryObject<SoundEvent> ENTITY_ROYALRED_BREATH = entity("royalred_breath");

    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_IDLE = entity("alpine_idle");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_HURT = entity("alpine_hurt");
    public static final RegistryObject<SoundEvent> ENTITY_ALPINE_ROAR = entity("alpine_roar");

    public static RegistryObject<SoundEvent> register(String name)
    {
        return REGISTRY.register(name, () -> new SoundEvent(Wyrmroost.rl(name)));
    }

    public static RegistryObject<SoundEvent> entity(String name)
    {
        return register("entity_" + name);
    }
}
