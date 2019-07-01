package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.objects.blocks.BlockList;
import WolfShotz.Wyrmroost.objects.items.ItemList;
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
        BlockList.BLOCKS.forEach(block -> event.getRegistry().register(block));

        Wyrmroost.L.info("Block Setup Complete");
    }

    @SubscribeEvent
    public static void itemSetup(final RegistryEvent.Register<Item> event) {
        ItemList.ITEMS.forEach(item -> event.getRegistry().register(item));

        Wyrmroost.L.info("Item Setup Complete");
    }

}
