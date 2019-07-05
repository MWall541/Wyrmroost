package WolfShotz.Wyrmroost.setup;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.blocks.BlockList;
import WolfShotz.Wyrmroost.content.entities.EntitySetup;
import WolfShotz.Wyrmroost.content.items.ItemList;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents
{
    @SubscribeEvent
    public static void entitySetup(final RegistryEvent.Register<EntityType<?>> event) {
        EntitySetup.collectEntities();
        EntitySetup.ENTITIES.forEach(entity -> event.getRegistry().register(entity));
        EntitySetup.registerEntityWorldSpawns();

        Wyrmroost.L.info("Entity Setup Complete");
    }

    @SubscribeEvent
    public static void itemSetup(final RegistryEvent.Register<Item> event) {
        ItemList.ITEMS.forEach(item -> event.getRegistry().register(item));

        Wyrmroost.L.info("Item Setup Complete");
    }

    @SubscribeEvent
    public static void blockSetup(final RegistryEvent.Register<Block> event) {
        BlockList.BLOCKS.forEach(block -> event.getRegistry().register(block));

        Wyrmroost.L.info("Block Setup Complete");
    }


}
