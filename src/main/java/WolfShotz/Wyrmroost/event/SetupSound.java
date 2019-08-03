package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupSound
{
    public static SoundEvent MINUTUS_IDLE;
    public static SoundEvent MINUTUS_SCREECH;

    @SubscribeEvent
    public static void soundSetup(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                MINUTUS_IDLE = registerSound("entity.minutus.idle"),
                MINUTUS_SCREECH = registerSound("entity.minutus.screech")
        );


    }

    private static SoundEvent registerSound(String name) { return new SoundEvent(ModUtils.location(name)).setRegistryName(name); }
}
