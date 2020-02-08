package WolfShotz.Wyrmroost.registry;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.content.io.screen.dragoninvscreens.ButterflyInvScreen;
import WolfShotz.Wyrmroost.content.io.screen.dragoninvscreens.OWDrakeInvScreen;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SetupIO
{
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<ContainerType<OWDrakeInvContainer>> OWDRAKE_CONTAINER = register("owdrake_inv", createEntityContainer(OWDrakeInvContainer::new));
    public static final RegistryObject<ContainerType<ContainerBase<RoostStalkerEntity>>> STALKER_CONTAINER = register("stalker_inv", createEntityContainer(ContainerBase::stalkerInv));
    public static final RegistryObject<ContainerType<ContainerBase<ButterflyLeviathanEntity>>> BUTTERFLY_CONTAINER = register("butterfly_inv", createEntityContainer(ContainerBase::butterflyInv));
    
    @OnlyIn(Dist.CLIENT)
    public static void screenSetup()
    {
        ScreenManager.registerFactory(OWDRAKE_CONTAINER.get(), OWDrakeInvScreen::new);
        ScreenManager.registerFactory(BUTTERFLY_CONTAINER.get(), ButterflyInvScreen::new);
//        ScreenManager.registerFactory(STALKER_CONTAINER.get(), ContainerScreenBase::singleSlotScreen);
    }
    
    @SuppressWarnings("unchecked")
    private static <T extends Container, E extends Entity> ContainerType<T> createEntityContainer(IEntityContainerFactory<T, E> creation)
    {
        return IForgeContainerType.create((windowId, inv, buf) -> creation.get((E) ModUtils.getClientWorld().getEntityByID(buf.readInt()), inv, windowId));
    }

    public static <T extends Container> RegistryObject<ContainerType<T>> register(String name, ContainerType<T> type)
    {
        return CONTAINERS.register(name, () -> type);
    }
    
    private interface IEntityContainerFactory<T extends Container, E extends Entity>
    {
        T get(E entity, PlayerInventory inv, int windowID);
    }
}
