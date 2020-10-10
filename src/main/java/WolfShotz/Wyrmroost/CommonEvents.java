package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.client.screen.DebugScreen;
import WolfShotz.Wyrmroost.data.DataGatherer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.dragon.SilverGliderEntity;
import WolfShotz.Wyrmroost.entities.util.VillagerHelper;
import WolfShotz.Wyrmroost.items.base.ArmorBase;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Reflection is shit and we shouldn't use it
 * - Some communist coding wyrmroost 2020
 * <p>
 * Manually add listeners
 */
public class CommonEvents
{
    public static final List<Runnable> CALLBACKS = new ArrayList<>();

    public static void load()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(CommonEvents::commonSetup);
        bus.addListener(WRConfig::configLoad);
        bus.addListener(DataGatherer::gather);

        forgeBus.addListener(CommonEvents::debugStick);
        forgeBus.addListener(CommonEvents::onChangeEquipment);
        forgeBus.addListener(CommonEvents::onFall);
        forgeBus.addListener(VillagerHelper::addWandererTrades);
    }

    // ====================
    //       Mod Bus
    // ====================

    // @formatter:off
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(() -> { CALLBACKS.forEach(Runnable::run); CALLBACKS.clear(); });
        DeferredWorkQueue.runLater(WRWorld::setupWorld);
    }
    // @formatter: on

    // =====================
    //      Forge Bus
    // =====================

    public static void debugStick(PlayerInteractEvent.EntityInteract evt)
    {
        if (!WRConfig.debugMode) return;
        PlayerEntity player = evt.getPlayer();
        ItemStack stack = player.getHeldItem(evt.getHand());
        if (stack.getItem() != Items.STICK || !stack.getDisplayName().getUnformattedComponentText().equals("Debug Stick"))
            return;

        evt.setCanceled(true);
        evt.setCancellationResult(ActionResultType.SUCCESS);

        Entity entity = evt.getTarget();
        entity.recalculateSize();

        if (!(entity instanceof AbstractDragonEntity)) return;
        AbstractDragonEntity dragon = (AbstractDragonEntity) entity;

//        dragon.setFlying(true);
        if (player.isSneaking()) dragon.tame(true, player);
        else
        {
            if (dragon.world.isRemote) DebugScreen.open(dragon);
            else Wyrmroost.LOG.info(dragon.getNavigator().getPath() == null? "null" : dragon.getNavigator().getPath().getTarget().toString());
        }
    }

    @SubscribeEvent
    public static void onChangeEquipment(LivingEquipmentChangeEvent evt)
    {
        ArmorBase initial;
        if (evt.getTo().getItem() instanceof ArmorBase) initial = (ArmorBase) evt.getTo().getItem();
        else if (evt.getFrom().getItem() instanceof ArmorBase) initial = (ArmorBase) evt.getFrom().getItem();
        else return;

        LivingEntity entity = evt.getEntityLiving();
        initial.applyFullSetBonus(entity, ArmorBase.hasFullSet(entity));
    }

    public static void onFall(LivingFallEvent evt)
    {
        Entity entity = evt.getEntity();
        if (entity.getType() == EntityType.PLAYER && !entity.getPassengers().isEmpty())
        {
            Entity passenger = entity.getPassengers().get(0);
            if (passenger.getType() == WREntities.SILVER_GLIDER.get())
                evt.setCanceled(((SilverGliderEntity) passenger).isGliding());
        }
    }
}
