package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.content.blocks.BlockList;
import WolfShotz.Wyrmroost.content.entities.EntitySetup;
import WolfShotz.Wyrmroost.content.items.ItemList;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class SetupRegistryEvents
{
    @SubscribeEvent
    public static void entitySetup(final RegistryEvent.Register<EntityType<?>> event) {
        EntitySetup.ENTITIES.forEach(entity -> event.getRegistry().register(entity));
        EntitySetup.registerEntityWorldSpawns();

        ModUtils.L.info("Entity Setup Complete");
    }

    // Start Items AFTER entities to register spawn egg items.
    @SubscribeEvent
    public static void itemSetup(final RegistryEvent.Register<Item> event) {
        ItemList.ITEMS.forEach(item -> event.getRegistry().register(item));

        ModUtils.L.info("Item Setup Complete");
    }

    @SubscribeEvent
    public static void blockSetup(final RegistryEvent.Register<Block> event) {
        BlockList.BLOCKS.forEach(block -> event.getRegistry().register(block));

        ModUtils.L.info("Block Setup Complete");
    }


}
