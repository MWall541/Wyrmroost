package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public enum WRSounds
{
    WING_FLAP,

    ENTITY_LDWYRM_IDLE,

    ENTITY_SILVERGLIDER_IDLE,
    ENTITY_SILVERGLIDER_HURT,
    ENTITY_SILVERGLIDER_DEATH,

    ENTITY_OWDRAKE_IDLE,
    ENTITY_OWDRAKE_ROAR,
    ENTITY_OWDRAKE_HURT,
    ENTITY_OWDRAKE_DEATH,

    ENTITY_STALKER_IDLE,
    ENTITY_STALKER_HURT,
    ENTITY_STALKER_DEATH,

    ENTITY_BFLY_IDLE,
    ENTITY_BFLY_ROAR,
    ENTITY_BFLY_HURT,
    ENTITY_BFLY_DEATH,

    ENTITY_CANARI_IDLE,
    ENTITY_CANARI_HURT,
    ENTITY_CANARI_DEATH,

    ENTITY_ROYALRED_IDLE,
    ENTITY_ROYALRED_HURT,
    ENTITY_ROYALRED_ROAR,
    ENTITY_ROYALRED_DEATH;

    private final RegistryObject<SoundEvent> delegate;

    WRSounds()
    {
        String name = toString();
        this.delegate = deferred().register(name, () -> new SoundEvent(Wyrmroost.rl(name)));
    }

    public SoundEvent get() { return delegate.get(); }

    @Override
    public String toString() { return name().toLowerCase().replaceAll("_", "."); }

    public static DeferredRegister<SoundEvent> deferred() { return Holder.SOUNDS; }

    private static final class Holder
    {
        public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, Wyrmroost.MOD_ID);
    }
}
