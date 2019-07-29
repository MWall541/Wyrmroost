package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundSetup
{
    public static SoundEvent MINUTUS_SCREECH;

    @SubscribeEvent
    public static void soundSetup(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(MINUTUS_SCREECH = registerSound("entity.minutus.screech"));
    }

    private static SoundEvent registerSound(String name) {
        return new SoundEvent(ModUtils.location(name)).setRegistryName(name);
    }
}
