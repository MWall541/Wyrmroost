package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.multipart.IMultiPartEntity;
import WolfShotz.Wyrmroost.content.items.DrakeArmorItem;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRWorld;
import WolfShotz.Wyrmroost.util.ConfigData;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@SuppressWarnings("unused")
public class CommonEvents
{
    /**
     * This is for MOD Event bus stuff.
     */
    public static void onModConstruction(IEventBus bus)
    {
        bus.addListener(CommonEvents::commonSetup);
        bus.addListener(CommonEvents::configLoad);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientEvents.onModConstruction(bus));
    }

    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);

        DeferredWorkQueue.runLater(WRWorld::setupWorldGen);
        DeferredWorkQueue.runLater(WREntities::registerEntityWorldSpawns);
        NetworkUtils.registerMessages();
    }

    /**
     * Fire on config change
     */
    public static void configLoad(ModConfig.ModConfigEvent evt)
    {
        if (evt.getConfig().getSpec() == ConfigData.CommonConfig.COMMON_SPEC)
            ConfigData.CommonConfig.reload();
        if (evt.getConfig().getSpec() == ConfigData.ClientConfig.CLIENT_SPEC)
            ConfigData.ClientConfig.reload();
    }

    // ==========================================================
    //  Forge Eventbus listeners
    //
    //  Anything below here isnt related to the mod bus,
    //  so like runtime stuff (Non-registry stuff)
    // ==========================================================

    /**
     * Register the Mod Dimension
     */
//    @SubscribeEvent
//    @SuppressWarnings("ConstantConditions")
//    public static void registerDimension(RegisterDimensionsEvent evt)
//    {
//        if (ModUtils.getDimensionInstance() == null)
//            DimensionManager.registerDimension(Wyrmroost.rl("wyrmroost"), WyrmroostDimension.WYRMROOST_DIM, null, true);
//    }

    /**
     * Attatch our capabilities
     */
//    @SubscribeEvent
//    public static void attachWorldCaps(AttachCapabilitiesEvent<World> evt)
//    {
//        if (evt.getObject().isRemote) return;
//        evt.addCapability(Wyrmroost.rl("overworld_cap"), new WorldCapability.PropertiesDispatcher());
//    }

    /**
     * Nuff' said
     */
    @SubscribeEvent
    public static void debugStick(PlayerInteractEvent.EntityInteract evt)
    {
        if (!ConfigData.debugMode) return;
        PlayerEntity player = evt.getPlayer();
        ItemStack stack = player.getHeldItem(evt.getHand());
        if (stack.getItem() != Items.STICK || !stack.getDisplayName().getUnformattedComponentText().equals("Debug Stick"))
            return;

        evt.setCanceled(true);
        evt.setCancellationResult(ActionResultType.SUCCESS);

        Entity entity = evt.getTarget();
        if (!(entity instanceof AbstractDragonEntity)) return;
        AbstractDragonEntity dragon = (AbstractDragonEntity) entity;

        if (player.isSneaking()) dragon.tame(true, player);
        else if (evt.getWorld().isRemote) ClientEvents.debugScreen(dragon);
//        dragon.setFlying(true);
    }

    @SubscribeEvent
    public static void onEntityTrack(PlayerEvent.StartTracking evt)
    {
        Entity target = evt.getTarget();
        if (target instanceof IMultiPartEntity)
        {
            IMultiPartEntity entity = (IMultiPartEntity) target;
            entity.iterateParts().forEach(target.world::addEntity);
        }
    }

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
