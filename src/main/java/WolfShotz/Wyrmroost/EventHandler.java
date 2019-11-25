package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.content.io.screen.DebugScreen;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.content.world.CapabilityOverworld;
import WolfShotz.Wyrmroost.registry.ModKeys;
import WolfShotz.Wyrmroost.registry.SetupWorld;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TranslationUtils;
import WolfShotz.Wyrmroost.util.network.messages.DragonKeyBindMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * Class Used to hook into forge events to perform particular tasks when the event is called
 * (e.g. player uses an anvil, cancel the normal behaviour and do something else instead)
 */
@SuppressWarnings("unused") // Theyre used damnit
public class EventHandler
{
    /**
     * Subscribe events on common distribution
     */
    public static class Common
    {
        /**
         * Register the Mod Dimension
         */
        @SubscribeEvent
        public static void registerDimension(RegisterDimensionsEvent evt) {
            if (ModUtils.getDimensionInstance() == null)
                DimensionManager.registerDimension(ModUtils.resource("dim_wyrmroost"), SetupWorld.DIM_WYRMROOST, null, true);
        }
    
        /**
         * Attach our capabilities
         * @param evt
         */
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<World> evt) {
            if (evt.getObject() == null) return;
            if (!evt.getObject().getCapability(CapabilityOverworld.OW_CAP).isPresent())
                evt.addCapability(ModUtils.resource("overworldproperties"), new CapabilityOverworld.PropertiesDispatcher());
        }
        
        /**
         * Nuff' said
         */
        @SubscribeEvent
        public static void debugStick(PlayerInteractEvent.EntityInteract evt) {
            Entity entity = evt.getTarget();
            if (!(entity instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;
            PlayerEntity player = evt.getPlayer();
            ItemStack stack = player.getHeldItem(evt.getHand());
            
            if (stack.getItem() == Items.STICK && stack.getDisplayName().getUnformattedComponentText().equals("Debug Stick")) {
                evt.setCanceled(true);
                
                if (evt.getWorld().isRemote) Minecraft.getInstance().displayGuiScreen(new DebugScreen(dragon));
            }
        }
    
        /**
         * Focken yeet the little shits that try this crap
         */
        private static final String[] begoneWEEB = {"owo", "uwu", "owu", "uwo", "0wo", "ow0", "0w0"};
        @SubscribeEvent
        public static void begoneWEEB(PlayerInteractEvent.EntityInteract evt) {
            Entity entity = evt.getTarget();
            if (!(entity instanceof AbstractDragonEntity)) return;
            PlayerEntity player = evt.getPlayer();
            ItemStack stack = player.getHeldItem(evt.getHand());
        
            if (stack.getItem() == Items.NAME_TAG && TranslationUtils.containsArray(stack.getDisplayName().getUnformattedComponentText().toLowerCase().trim(), begoneWEEB)) {
                evt.setCanceled(true);
                
                stack.clearCustomName();
                entity.world.createExplosion(entity, player.posX, player.posY + 1, player.posZ, 6f, Explosion.Mode.NONE); // WEEB GO BOOM
            }
        }
    
        /**
         * Disable Damage when riding dragons. This makes as much sense as mojang themselves
         */
        @SubscribeEvent
        public static void onEntityFall(LivingFallEvent evt) {
            if (evt.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) evt.getEntity();
                
                if (player.getPassengers().stream().anyMatch(SilverGliderEntity.class::isInstance)) {
                    evt.setCanceled(true);
                    return;
                }
                if (player.getRidingEntity() instanceof AbstractDragonEntity) {
                    evt.setCanceled(true);
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
         * Can't be subscribed for some reason. Trust me, I tried >.>
         */
        public static void registerItemColors() {
            ItemColors event = Minecraft.getInstance().getItemColors();
            
            IItemColor eggColor = (stack, tintIndex) -> ((CustomSpawnEggItem) stack.getItem()).getColors(tintIndex);
            CustomSpawnEggItem.EGG_TYPES.forEach(e -> event.register(eggColor, e));
        }
        
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
                    Wyrmroost.network.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.PERFORM_GENERIC_ATTACK));
                }
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
