package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.io.container.base.BasicSlotInvContainer;
import WolfShotz.Wyrmroost.content.io.container.ButterflyInvContainer;
import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.io.screen.ButterflyInvScreen;
import WolfShotz.Wyrmroost.content.io.screen.OWDrakeInvScreen;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class SetupIO
{
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Wyrmroost.MOD_ID);
    
    public static final RegistryObject<ContainerType<OWDrakeInvContainer>> OWDRAKE_CONTAINER = register("owdrake_inv", createEntityContainer(OWDrakeInvContainer::new));
    public static final RegistryObject<ContainerType<BasicSlotInvContainer>> STALKER_CONTAINER = register("stalker_inv", SetupIO.<BasicSlotInvContainer, RoostStalkerEntity>createEntityContainer(BasicSlotInvContainer::stalkerContainer));
    public static final RegistryObject<ContainerType<BasicSlotInvContainer>> BUTTERFLY_CONTAINER = register("butterfly_inv", SetupIO.<BasicSlotInvContainer, ButterflyLeviathanEntity>createEntityContainer(BasicSlotInvContainer::butterflyContainer));
    public static final RegistryObject<ContainerType<BasicSlotInvContainer>> FRUIT_DRAKE_CONTAINER = register("fruit_drake_inv", SetupIO.<BasicSlotInvContainer, DragonFruitDrakeEntity>createEntityContainer(BasicSlotInvContainer::dragonFruitContainer));
    
    @OnlyIn(Dist.CLIENT)
    public static void screenSetup() {
        ScreenManager.registerFactory(OWDRAKE_CONTAINER.get(), OWDrakeInvScreen::new);
        ScreenManager.registerFactory(BUTTERFLY_CONTAINER.get(), ButterflyInvScreen::new);
    }
    
    
    private static <T extends Container, E extends Entity> ContainerType<T> createEntityContainer(IEntityContainerFactory<T, E> creation) {
        return IForgeContainerType.create((windowId, inv, buf) -> {
            E entity = (E) ModUtils.getClientWorld().getEntityByID(buf.readInt());
            return creation.get(entity, inv, windowId);
        });
    }
    
    public static <T extends Container> RegistryObject<ContainerType<T>> register(String name, ContainerType<T> type) {
        return CONTAINERS.register(name, () -> type);
    }
    
    private interface IEntityContainerFactory<T extends Container, E extends Entity>
    {
        T get(E entity, PlayerInventory inv, int windowID);
    }
}
