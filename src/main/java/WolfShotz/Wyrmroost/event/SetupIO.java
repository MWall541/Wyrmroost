package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.io.container.ButterflyInvContainer;
import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.io.container.StalkerInvContainer;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.content.io.screen.ButterflyInvScreen;
import WolfShotz.Wyrmroost.content.io.screen.OWDrakeInvScreen;
import WolfShotz.Wyrmroost.content.io.screen.base.AbstractContainerScreen;
import WolfShotz.Wyrmroost.content.io.screen.base.BasicDragonScreen;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Wyrmroost.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupIO
{
    private static final String ID = Wyrmroost.MOD_ID + ":";
    
    @ObjectHolder(ID + "base_dragon_inv")   public static ContainerType<ContainerBase>          baseContainer;
    @ObjectHolder(ID + "owdrake_inv")       public static ContainerType<OWDrakeInvContainer>    owDrakeContainer;
    @ObjectHolder(ID + "stalker_inv")       public static ContainerType<StalkerInvContainer>    stalkerContainer;
    @ObjectHolder(ID + "butterfly_inv")     public static ContainerType<ButterflyInvContainer>  butterflyContainer;
    
    @SubscribeEvent
    public static void containerSetup(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().registerAll (
                createEntityContainer(((entity, inv, windowID) -> new ContainerBase((AbstractDragonEntity) entity, windowID)), "base_dragon_inv"),
                createEntityContainer(OWDrakeInvContainer::new, "owdrake_inv"),
                createEntityContainer(ButterflyInvContainer::new, "butterfly_inv")
        );
    }
    
    private static <E extends Entity> ContainerType<? extends Container> createEntityContainer(IEntityContainerFactory<?, E> creation, String name) {
        return IForgeContainerType.create((windowId, inv, buf) -> {
            E entity = (E) ModUtils.getClientWorld().getEntityByID(buf.readInt());
            return creation.get(entity, inv, windowId);
        }).setRegistryName(name);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void screenSetup() {
        ScreenManager.registerFactory(owDrakeContainer, OWDrakeInvScreen::new);
        ScreenManager.registerFactory(baseContainer, BasicDragonScreen::new);
        ScreenManager.registerFactory(butterflyContainer, ButterflyInvScreen::new);
    }
    
    private interface IEntityContainerFactory<T extends Container, E extends Entity>
    {
        T get(E entity, PlayerInventory inv, int windowID);
    }
}
