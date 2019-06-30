package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents
{
    @SubscribeEvent
    public static void blockSetup(final RegistryEvent.Register<Block> event) {
//        event.getRegistry().registerAll();

        Wyrmroost.L.info("Block Setup...");
    }

    @SubscribeEvent
    public static void itemSetup(final RegistryEvent.Register<Item> event) {
//        event.getRegistry().registerAll();

        Wyrmroost.L.info("Item Setup...");
    }

}
