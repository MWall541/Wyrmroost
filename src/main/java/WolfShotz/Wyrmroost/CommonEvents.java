package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.entities.util.VillagerHelper;
import WolfShotz.Wyrmroost.items.DrakeArmorItem;
import WolfShotz.Wyrmroost.registry.WRWorld;
import WolfShotz.Wyrmroost.util.CallbackHandler;
import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

/**
 * Reflection is shit and we shouldn't use it
 * - Some communist coding wyrmroost 2020
 */
public class CommonEvents
{
    public static final CallbackHandler<?> CALLBACK = new CallbackHandler<>();

    public static void register()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ModBus::commonSetup);
        bus.addListener(ModBus::configLoad);
        bus.addListener(VillagerHelper::registerVillagersAndTrades);

        MinecraftForge.EVENT_BUS.addListener(ForgeBus::debugStick);
        MinecraftForge.EVENT_BUS.addListener(ForgeBus::onChangeEquipment);
    }

    public static <T extends IForgeRegistryEntry<T>> RegistryObject<T> registerObject(Class<T> type, String name, Supplier<T> sup)
    {
        ResourceLocation rl = Wyrmroost.rl(name);
        FMLJavaModLoadingContext.get().getModEventBus().<RegistryEvent.Register<T>, T>addGenericListener(type,
                EventPriority.NORMAL,
                false,
                e -> e.getRegistry().register(sup.get().setRegistryName(rl)));

        return RegistryObject.of(rl, () -> type);
    }

    private static class ModBus
    {
        public static void commonSetup(final FMLCommonSetupEvent event)
        {
            DeferredWorkQueue.runLater(() -> CALLBACK.acceptAndClear(null));
            DeferredWorkQueue.runLater(WRWorld::setupWorld);
        }

        public static void configLoad(ModConfig.ModConfigEvent evt)
        {
            ForgeConfigSpec spec = evt.getConfig().getSpec();
            if (spec == WRConfig.Common.SPEC) WRConfig.Common.reload();
            else if (spec == WRConfig.Client.SPEC) WRConfig.Client.reload();
            else if (spec == WRConfig.Server.SPEC) WRConfig.Server.reload();
        }
    }

    private static class ForgeBus
    {
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

            ((VillagerEntity) entity)
                    .setVillagerData(((VillagerEntity) entity)
                            .getVillagerData()
                            .withLevel(2)
                            .withProfession(ForgeRegistries.PROFESSIONS.getValue(Wyrmroost.rl("coin_dragon_trader"))));

//            if (!(entity instanceof AbstractDragonEntity)) return;
//            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
//
//            if (player.isSneaking()) dragon.tame(true, player);
//            else DebugScreen.open(dragon);
        }

//        @SubscribeEvent
//        @SuppressWarnings("ConstantConditions")
//        public static void registerDimension(RegisterDimensionsEvent evt)
//        {
//            if (ModUtils.getDimensionInstance() == null)
//                DimensionManager.registerDimension(Wyrmroost.rl("wyrmroost"), WyrmroostDimension.WYRMROOST_DIM, null, true);
//        }

        @SubscribeEvent
        public static void onChangeEquipment(LivingEquipmentChangeEvent evt)
        {
            if (!(evt.getEntity() instanceof PlayerEntity)) return;
            Iterable<ItemStack> armor = evt.getEntity().getArmorInventoryList();
            boolean full = Streams.stream(armor).allMatch(k -> k.getItem() instanceof DrakeArmorItem);
            Streams.stream(armor)
                    .filter(i -> i.getItem() instanceof DrakeArmorItem)
                    .map(i -> (DrakeArmorItem) i.getItem())
                    .forEach(i -> i.setFullSet(full));
        }
    }
}
