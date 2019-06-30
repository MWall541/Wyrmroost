package WolfShotz.Wyrmroost.util.handlers;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler
{
    @SubscribeEvent
    public static void setup(final RegistryEvent.Register<Block> event) {
        Wyrmroost.LOGGER.info("called RegistryHandler");
    }

}
