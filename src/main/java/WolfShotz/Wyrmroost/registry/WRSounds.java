package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WRSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, Wyrmroost.MOD_ID);

    // Entities
    public static final RegistryObject<SoundEvent> MINUTUS_IDLE = registerEntity("minutus.idle");
    public static final RegistryObject<SoundEvent> MINUTUS_SCREECH = registerEntity("minutus.screech");

    public static final RegistryObject<SoundEvent> SILVERGLIDER_IDLE = registerEntity("silverglider.idle");
    public static final RegistryObject<SoundEvent> SILVERGLIDER_HURT = registerEntity("silverglider.hurt");
    public static final RegistryObject<SoundEvent> SILVERGLIDER_DEATH = registerEntity("silverglider.death");

    public static final RegistryObject<SoundEvent> OWDRAKE_IDLE = registerEntity("owdrake.idle");
    public static final RegistryObject<SoundEvent> OWDRAKE_ROAR = registerEntity("owdrake.roar");
    public static final RegistryObject<SoundEvent> OWDRAKE_HURT = registerEntity("owdrake.hurt");
    public static final RegistryObject<SoundEvent> OWDRAKE_DEATH = registerEntity("owdrake.death");

    public static final RegistryObject<SoundEvent> STALKER_IDLE = registerEntity("rooststalker.idle");
    public static final RegistryObject<SoundEvent> STALKER_HURT = registerEntity("rooststalker.hurt");
    public static final RegistryObject<SoundEvent> STALKER_DEATH = registerEntity("rooststalker.death");

    private static RegistryObject<SoundEvent> register(String name)
    {
        return SOUNDS.register(name, () -> new SoundEvent(Wyrmroost.rl(name)));
    }

    private static RegistryObject<SoundEvent> registerEntity(String name) { return register("entity." + name); }
}
