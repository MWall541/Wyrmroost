package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.screen.DragonStaffScreen;
import com.github.wolfshotz.wyrmroost.containers.DragonStaffContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WRIO
{
    public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, Wyrmroost.MOD_ID);

    public static final RegistryObject<ContainerType<DragonStaffContainer>> DRAGON_STAFF = register("dragon_staff", DragonStaffContainer::factory);

    public static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory)
    {
        return REGISTRY.register(name, () -> IForgeContainerType.create(factory));
    }

    public static void screenSetup()
    {
        ScreenManager.register(DRAGON_STAFF.get(), DragonStaffScreen::new);
    }
}
