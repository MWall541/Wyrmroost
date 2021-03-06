package com.github.wolfshotz.wyrmroost;

import com.github.wolfshotz.wyrmroost.client.screen.DebugScreen;
import com.github.wolfshotz.wyrmroost.data.DataGatherer;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.util.VillagerHelper;
import com.github.wolfshotz.wyrmroost.items.CoinDragonItem;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.items.base.ArmorBase;
import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
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
        forgeBus.addListener(CommonEvents::loadLoot);
        forgeBus.addListener(VillagerHelper::addWandererTrades);
        forgeBus.addListener(EventPriority.HIGH, WRWorld::onBiomeLoad);
    }

    // ====================
    //       Mod Bus
    // ====================

    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            CALLBACKS.forEach(Runnable::run);
            CALLBACKS.clear();
            LazySpawnEggItem.addEggsToMap();
        });
        IAnimatable.registerCapability();
        WRWorld.Features.init();
    }

    // =====================
    //      Forge Bus
    // =====================

    public static void debugStick(PlayerInteractEvent.EntityInteract evt)
    {
        if (!WRConfig.debugMode) return;
        PlayerEntity player = evt.getPlayer();
        ItemStack stack = player.getStackInHand(evt.getHand());
        if (stack.getItem() != Items.STICK || !stack.getName().getString().equals("Debug Stick"))
            return;

        evt.setCanceled(true);
        evt.setCancellationResult(ActionResultType.SUCCESS);

        Entity entity = evt.getTarget();
        entity.calculateDimensions();

        if (!(entity instanceof AbstractDragonEntity)) return;
        AbstractDragonEntity dragon = (AbstractDragonEntity) entity;

        if (player.isSneaking()) dragon.tame(true, player);
        else
        {
            if (dragon.world.isClient) DebugScreen.open(dragon);
            else Wyrmroost.LOG.info(dragon.getNavigation().getCurrentPath() == null? "null" : dragon.getNavigation().getCurrentPath().getTarget().toString());
        }
    }

    public static void onChangeEquipment(LivingEquipmentChangeEvent evt)
    {
        ArmorBase initial;
        if (evt.getTo().getItem() instanceof ArmorBase) initial = (ArmorBase) evt.getTo().getItem();
        else if (evt.getFrom().getItem() instanceof ArmorBase) initial = (ArmorBase) evt.getFrom().getItem();
        else return;

        LivingEntity entity = evt.getEntityLiving();
        initial.applyFullSetBonus(entity, ArmorBase.hasFullSet(entity));
    }

    public static void loadLoot(LootTableLoadEvent evt)
    {
        if (evt.getName().equals(LootTables.ABANDONED_MINESHAFT_CHEST))
            evt.getTable().addPool(LootPool.builder()
                    .name("coin_dragon_inject")
                    .with(CoinDragonItem.getLootEntry())
                    .build());
    }
}
