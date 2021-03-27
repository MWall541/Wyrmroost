package com.github.wolfshotz.wyrmroost;

import com.github.wolfshotz.wyrmroost.client.screen.DebugScreen;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.util.VillagerHelper;
import com.github.wolfshotz.wyrmroost.items.CoinDragonItem;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.items.base.ArmorBase;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRWorld;
import com.github.wolfshotz.wyrmroost.util.DebugRendering;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.block.*;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;

import static com.github.wolfshotz.wyrmroost.util.ModUtils.cast;

/**
 * Reflection is shit and we shouldn't use it
 * - Some communist coding wyrmroost 2020
 * <p>
 * Manually add listeners
 */
public class CommonEvents
{
    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(CommonEvents::commonSetup);
        bus.addListener(WRConfig::configLoad);

        forgeBus.addListener(CommonEvents::debugStick);
        forgeBus.addListener(CommonEvents::debugStickButItsForBlocksWoah);
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
        IAnimatable.registerCapability();

        event.enqueueWork(() ->
        {
            LazySpawnEggItem.addEggsToMap();
            WoodType.values().filter(w -> w.name().contains(Wyrmroost.MOD_ID)).forEach(WoodType::register);

            for (EntityType<?> entry : ModUtils.getRegistryEntries(WREntities.REGISTRY))
            {
                if (entry instanceof WREntities.Type)
                {
                    WREntities.Type<? extends MobEntity> custom = cast(entry);
                    if (custom.attributes != null)
                        GlobalEntityTypeAttributes.put(custom, custom.attributes.build());
                    if (custom.spawnPlacement != null)
                        EntitySpawnPlacementRegistry.register(custom, custom.spawnPlacement.a, custom.spawnPlacement.b, cast(custom.spawnPlacement.c));
                }
            }

            for (Map.Entry<RegistryObject<Block>, WRBlocks.BlockExtension> entry : WRBlocks.EXTENSIONS.entrySet())
            {
                Block block = entry.getKey().get();
                WRBlocks.BlockExtension extension = entry.getValue();
                if (extension.renderType != null)
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> RenderTypeLookup.setRenderLayer(block, extension.renderType.get().get()));
                if (extension.flammability != null)
                    ((FireBlock) Blocks.FIRE).setFlammable(block, extension.flammability[0], extension.flammability[1]);
            }
        });
    }

    // =====================
    //      Forge Bus
    // =====================

    public static void debugStick(PlayerInteractEvent.EntityInteract event)
    {
        if (!WRConfig.debugMode) return;
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getItemInHand(event.getHand());
        if (stack.getItem() != Items.STICK || !stack.getHoverName().getString().equals("Debug Stick"))
            return;

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);

        Entity entity = event.getTarget();
        entity.refreshDimensions();

        if (!(entity instanceof TameableDragonEntity)) return;
        TameableDragonEntity dragon = (TameableDragonEntity) entity;

        if (player.isShiftKeyDown()) dragon.tame(true, player);
        else
        {
            if (dragon.level.isClientSide) DebugScreen.open(dragon);
            else
                Wyrmroost.LOG.info(dragon.getNavigation().getPath() == null ? "null" : dragon.getNavigation().getPath().getTarget().toString());
        }
    }

    public static void debugStickButItsForBlocksWoah(PlayerInteractEvent.RightClickBlock event)
    {
        if (!WRConfig.debugMode) return;
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getItemInHand(event.getHand());
        if (stack.getItem() != Items.STICK || !stack.getHoverName().getString().equals("Debug Stick")) return;

        World level = event.getWorld();
        BlockPos pos = event.getPos();
        Block block = level.getBlockState(pos).getBlock();

        if (!(block instanceof SaplingBlock) || !block.getRegistryName().getPath().contains("oseri")) return;

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);

        if (level.isClientSide)
            DebugRendering.conjoined(0xfffdff87, Integer.MAX_VALUE, pos, pos.north(), pos.south(), pos.above(), pos.above(2), pos.above(3), pos.above(4));
    }

    public static void onChangeEquipment(LivingEquipmentChangeEvent event)
    {
        ArmorBase initial;
        if (event.getTo().getItem() instanceof ArmorBase) initial = (ArmorBase) event.getTo().getItem();
        else if (event.getFrom().getItem() instanceof ArmorBase) initial = (ArmorBase) event.getFrom().getItem();
        else return;

        LivingEntity entity = event.getEntityLiving();
        initial.applyFullSetBonus(entity, ArmorBase.hasFullSet(entity));
    }

    public static void loadLoot(LootTableLoadEvent event)
    {
        if (event.getName().equals(LootTables.ABANDONED_MINESHAFT))
            event.getTable().addPool(LootPool.lootPool()
                    .name("coin_dragon_inject")
                    .add(CoinDragonItem.getLootEntry())
                    .build());
    }
}
