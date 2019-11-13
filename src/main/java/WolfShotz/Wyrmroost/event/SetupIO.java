package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.io.container.DragonFruitDrakeContainer;
import WolfShotz.Wyrmroost.content.io.container.StalkerInvContainer;
import WolfShotz.Wyrmroost.content.io.container.ButterflyInvContainer;
import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.io.screen.ButterflyInvScreen;
import WolfShotz.Wyrmroost.content.io.screen.OWDrakeInvScreen;
import WolfShotz.Wyrmroost.content.io.screen.base.BasicDragonScreen;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import javafx.stage.Screen;
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
    public static final RegistryObject<ContainerType<StalkerInvContainer>> STALKER_CONTAINER = register("stalker_inv", createEntityContainer(StalkerInvContainer::new));
    public static final RegistryObject<ContainerType<ButterflyInvContainer>> BUTTERFLY_CONTAINER = register("butterfly_inv", createEntityContainer(ButterflyInvContainer::new));
    public static final RegistryObject<ContainerType<DragonFruitDrakeContainer>> FRUIT_DRAKE_CONTAINER = register("fruit_drake_inv", createEntityContainer(DragonFruitDrakeContainer::new));
    
    @OnlyIn(Dist.CLIENT)
    public static void screenSetup() {
        ScreenManager.registerFactory(OWDRAKE_CONTAINER.get(), OWDrakeInvScreen::new);
        ScreenManager.registerFactory(BUTTERFLY_CONTAINER.get(), ButterflyInvScreen::new);
        ScreenManager.registerFactory(FRUIT_DRAKE_CONTAINER.get(), BasicDragonScreen::singleSlotScreen);
        ScreenManager.registerFactory(STALKER_CONTAINER.get(), BasicDragonScreen::singleSlotScreen);
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
