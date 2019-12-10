package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.io.screen.DebugScreen;
import WolfShotz.Wyrmroost.content.world.CapabilityOverworld;
import WolfShotz.Wyrmroost.registry.ModKeys;
import WolfShotz.Wyrmroost.registry.SetupWorld;
import WolfShotz.Wyrmroost.util.ConfigData;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.network.messages.DragonKeyBindMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Events subscribed under the Forge EventBus
 */
@SuppressWarnings("unused")
public class EventHandler
{
    /**
     * Event handlers listening on COMMON Distribution
     */
    public static class Common
    {
        /**
         * Register the Mod Dimension
         */
        @SubscribeEvent
        @SuppressWarnings("ConstantConditions")
        public static void registerDimension(RegisterDimensionsEvent evt) {
            if (ModUtils.getDimensionInstance() == null)
                DimensionManager.registerDimension(ModUtils.resource("dim_wyrmroost"), SetupWorld.DIM_WYRMROOST, null, true);
        }
        
        /**
         * Attach our capabilities
         */
        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<World> evt) {
            if (evt.getObject() == null) return;
            if (!evt.getObject().getCapability(CapabilityOverworld.OW_CAP).isPresent())
                evt.addCapability(ModUtils.resource("overworldproperties"), new CapabilityOverworld.PropertiesDispatcher());
        }
        
        /**
         * Nuff' said
         */
        @SubscribeEvent
        public static void debugStick(PlayerInteractEvent.EntityInteract evt) {
            if (!ConfigData.debugMode) return;
            PlayerEntity player = evt.getPlayer();
            ItemStack stack = player.getHeldItem(evt.getHand());
            if (stack.getItem() != Items.STICK || !stack.getDisplayName().getUnformattedComponentText().equals("Debug Stick")) return;
        
            System.out.println(ModelLoaderRegistry.loaded(new ResourceLocation("item/minutus_egg")));
        
            Entity entity = evt.getTarget();
            if (!(entity instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
        
            if (player.isSneaking()) dragon.tame(true, player);
            else if (evt.getWorld().isRemote) Minecraft.getInstance().displayGuiScreen(new DebugScreen(dragon));
        
            evt.setCanceled(true);
        }
    }
    
    /**
     * Event listeners running on CLIENT distribution
     */
    @OnlyIn(Dist.CLIENT)
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
            if (ModKeys.genericAttack.isPressed()) {
                if (!(player.getRidingEntity() instanceof AbstractDragonEntity)) return;
                AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();
            
                if (dragon.noActiveAnimation()) {
                    dragon.performGenericAttack();
                    Wyrmroost.NETWORK.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.PERFORM_GENERIC_ATTACK));
                }
            }
        }
    
        /**
         * Handles the perspective/position of the camera
         */
        @SubscribeEvent
        public static void cameraPerspective(EntityViewRenderEvent.CameraSetup event) {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.player.getRidingEntity();
            if (!(entity instanceof AbstractDragonEntity)) return;
            int i1 = mc.gameSettings.thirdPersonView;
        
            if (i1 != 0) ((AbstractDragonEntity) entity).setMountCameraAngles(i1 == 1);
        }
    }
}
