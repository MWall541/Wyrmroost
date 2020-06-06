package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.client.screen.DebugScreen;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.multipart.IMultiPartEntity;
import WolfShotz.Wyrmroost.items.DrakeArmorItem;
import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("unused")
public class CommonEvents
{
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
     * Nuff' said
     */
    @SubscribeEvent
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
        if (!(entity instanceof AbstractDragonEntity)) return;
        AbstractDragonEntity dragon = (AbstractDragonEntity) entity;

        dragon.recalculateSize();
        if (player.isSneaking()) dragon.tame(true, player);
        else if (evt.getWorld().isRemote) DebugScreen.open(dragon);
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
