package WolfShotz.Wyrmroost.event;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.network.SendKeyPressMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.lwjgl.opengl.GL11;

/**
 * Class Used to hook into forge events to perform particular tasks when the event is called
 * (e.g. player uses an anvil, cancel the normal behaviour and do something else instead)
 */
@SuppressWarnings("unused")
public class ForgeEvents
{
    @OnlyIn(Dist.CLIENT)
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
            if (!SetupKeyBinds.specialAttack.isKeyDown() && dragon.isSpecialAttacking) {
                System.out.println("sending stop message");
                Wyrmroost.network.sendToServer(new SendKeyPressMessage(dragon, 2)); // Release
            }
        }
    }

     /**
      * Handles the camera setup for what the player is looking at
      * @deprecated Currently not functional. See:
      * <a href="https://github.com/MinecraftForge/MinecraftForge/issues/5911">Issue #5911</a>
      */
     @OnlyIn(Dist.CLIENT)
     public static void ridingPerspective(EntityViewRenderEvent.CameraSetup event) {
         Minecraft mc = Minecraft.getInstance();

         if (mc.player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance))
             if (mc.gameSettings.thirdPersonView == 1) GL11.glTranslatef(0, -0.5f, -0.5f);
     }
     
     public static void debugStick(PlayerInteractEvent.EntityInteract evt) {
         Entity entity = evt.getTarget();
         if (!(entity instanceof AbstractDragonEntity)) return;
         AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
         PlayerEntity player = evt.getPlayer();
         ItemStack stack = player.getHeldItem(evt.getHand());
         
         if (stack.getItem() == Items.STICK && stack.getDisplayName().getUnformattedComponentText().equals("Debug Stick")) {
             evt.setCanceled(true);
         }
     }
}
