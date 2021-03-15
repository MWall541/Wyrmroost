package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.screen.DragonInvScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
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
        ScreenManager.register(DRAGON_INVENTORY.get(), DragonInvScreen::new);
    }

    private static ContainerType<DragonInvContainer> getDragonInvContainer()
    {
        return IForgeContainerType.create(((windowId, inv, data) ->
        {
            AbstractDragonEntity dragon = (AbstractDragonEntity) ClientEvents.getWorld().getEntityById(data.readInt());
            return new DragonInvContainer(dragon.getInvHandler(), inv, windowId);
        }));
    }
}
