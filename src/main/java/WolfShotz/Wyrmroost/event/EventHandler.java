package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.network.SendKeyPressMessage;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import WolfShotz.Wyrmroost.util.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Objects;
import java.util.stream.Stream;

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
                
                dragon.setGrowingAge(-24000);
            }
        }
        
        @SubscribeEvent
        public static void onEntityFall(LivingFallEvent evt) {
            if (evt.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) evt.getEntity();
                
                if (player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance) && player.getMotion().y > -0.7f)
                    evt.setCanceled(true);
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
        
            if (player.getRidingEntity() instanceof AbstractDragonEntity) {
                AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();
            
                if (SetupKeyBinds.genericAttack.isPressed() && !dragon.hasActiveAnimation()) {
                    dragon.performGenericAttack();
                    Wyrmroost.network.sendToServer(new SendKeyPressMessage(dragon, 0));
                    return;
                }
                if (SetupKeyBinds.specialAttack.isKeyDown()) {
                    System.out.println("hooold...");
                    Wyrmroost.network.sendToServer(new SendKeyPressMessage(dragon, 1)); // Hold
                }
                //todo Revaluate
/*                if (!SetupKeyBinds.specialAttack.isKeyDown() && dragon.isSpecialAttacking) {
                    System.out.println("sending stop message");
                    Wyrmroost.network.sendToServer(new SendKeyPressMessage(dragon, 2)); // Release
                }*/
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
