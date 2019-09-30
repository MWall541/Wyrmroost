package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.network.DragonKeyBindMessage;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.opengl.GL11;

/**
 * Class Used to hook into forge events to perform particular tasks when the event is called
 * (e.g. player uses an anvil, cancel the normal behaviour and do something else instead)
 */
@SuppressWarnings("unused")
public class EventHandler
{
    /**
     * Subscribe events on common distribution
     */
    public static class Common
    {
        @SubscribeEvent
        public static void debugStick(PlayerInteractEvent.EntityInteract evt) {
            Entity entity = evt.getTarget();
            if (!(entity instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
            PlayerEntity player = evt.getPlayer();
            ItemStack stack = player.getHeldItem(evt.getHand());
            
            if (stack.getItem() == Items.STICK && stack.getDisplayName().getUnformattedComponentText().equals("Debug Stick")) {
                evt.setCanceled(true);
    
                if (player instanceof ServerPlayerEntity) NetworkHooks.openGui((ServerPlayerEntity) player, (OWDrakeEntity) dragon, buf -> buf.writeInt(dragon.getEntityId()));
            }
        }
    
        @SubscribeEvent
        public static void begoneTHOT(PlayerInteractEvent.EntityInteract evt) {
            Entity entity = evt.getTarget();
            if (!(entity instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
            PlayerEntity player = evt.getPlayer();
            ItemStack stack = player.getHeldItem(evt.getHand());
        
            if (stack.getItem() == Items.NAME_TAG && TranslationUtils.containsArray(stack.getDisplayName().getUnformattedComponentText().toLowerCase(), "owo", "uwu", "owu", "uwo")) {
                evt.setCanceled(true);
                stack.clearCustomName();
                dragon.world.createExplosion(dragon, player.posX, player.posY + 1, player.posZ, 6f, Explosion.Mode.NONE);
            }
        }
        
        @SubscribeEvent
        public static void onEntityFall(LivingFallEvent evt) {
            if (evt.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) evt.getEntity();
                
                if (player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance)) {
                    evt.setCanceled(true);
                    return;
                }
                if (player.getRidingEntity() instanceof AbstractDragonEntity) { // Disable fall damage of players when riding dragons. this doesnt make any sense.
                    evt.setCanceled(true);
//                    return;
                }
            }
        }
    }
    
    /**
     * Subscribe events playing on CLIENT only distribution
     */
    public static class Client
    {
        /**
         * Handles custom keybind pressing
         */
        @SubscribeEvent
        public static void onKeyPress(InputEvent.KeyInputEvent event) {
            if (Minecraft.getInstance().world == null) return; // Dont do anything on the main menu screen
            PlayerEntity player = Minecraft.getInstance().player;
        
            // Generic Attack
            if (SetupKeyBinds.genericAttack.isPressed()) {
                if (!(player.getRidingEntity() instanceof AbstractDragonEntity)) return;
                AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();
                
                if (dragon.noActiveAnimation()) {
                    dragon.performGenericAttack();
                    Wyrmroost.network.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.PERFORM_GENERIC_ATTACK));
                }
            } else if (SetupKeyBinds.callDragon.isPressed()) {
                RayTraceResult result = MathUtils.rayTrace(player.world, player, 50, true);
                if (result.getType() != RayTraceResult.Type.ENTITY) return;
                EntityRayTraceResult ertr = (EntityRayTraceResult) result;
                Entity entity = ertr.getEntity();
                if (!(entity instanceof AbstractDragonEntity)) return;
                AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
                if (!dragon.isOwner(player)) return;
                
                if (!dragon.callDragon(player)) return;
                Wyrmroost.network.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.CALL_DRAGON));
            }
        }
        
        /**
         * Handles the camera setup for what the player is looking at
         * @deprecated Currently not functional. See:
         * <a href="https://github.com/MinecraftForge/MinecraftForge/issues/5911">Issue #5911</a>
         */
        @SubscribeEvent
        public static void ridingPerspective(EntityViewRenderEvent.CameraSetup event) {
            Minecraft mc = Minecraft.getInstance();
        
            System.out.println("Fired");
            
            if (mc.player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance))
                if (mc.gameSettings.thirdPersonView == 1) GL11.glTranslatef(0, -0.5f, -0.5f);
        }
    }
}
