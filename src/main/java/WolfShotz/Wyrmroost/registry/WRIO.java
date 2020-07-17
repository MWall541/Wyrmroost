package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.screen.DragonInvScreen;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WRIO
{
    public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, Wyrmroost.MOD_ID);

    public static final RegistryObject<ContainerType<DragonInvContainer>> DRAGON_INVENTORY = register("dragon_inventory", getDragonInvContainer());


    public static <T extends Container> RegistryObject<ContainerType<T>> register(String name, ContainerType<T> type)
    {
        return REGISTRY.register(name, () -> type);
    }

    public static void screenSetup()
    {
        ScreenManager.registerFactory(DRAGON_INVENTORY.get(), DragonInvScreen::new);
    }

    private static ContainerType<DragonInvContainer> getDragonInvContainer()
    {
        return IForgeContainerType.create(((windowId, inv, data) ->
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) ClientEvents.getClient().world.getEntityByID(data.readInt());
            return new DragonInvContainer(dragon.getInvHandler(), inv, windowId);
        }));
    }
}
