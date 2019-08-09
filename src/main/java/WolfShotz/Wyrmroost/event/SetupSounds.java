package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Wyrmroost.modID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupSounds
{
    @ObjectHolder(Wyrmroost.modID + ":entity.minutus.idle")
    public static SoundEvent MINUTUS_IDLE;
    
    @ObjectHolder(Wyrmroost.modID + ":entity.minutus.screech")
    public static SoundEvent MINUTUS_SCREECH;

    @SubscribeEvent
    public static void soundSetup(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                registerSound("entity.minutus.idle"),
                registerSound("entity.minutus.screech")
        );
    }

    private static SoundEvent registerSound(String name) { return new SoundEvent(ModUtils.location(name)).setRegistryName(name); }
}
