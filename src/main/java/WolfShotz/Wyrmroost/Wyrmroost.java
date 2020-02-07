package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.io.screen.DebugScreen;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.content.world.EndOrePlacement;
import WolfShotz.Wyrmroost.content.world.WorldCapability;
import WolfShotz.Wyrmroost.content.world.dimension.WyrmroostDimension;
import WolfShotz.Wyrmroost.registry.*;
import WolfShotz.Wyrmroost.util.ConfigData;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import WolfShotz.Wyrmroost.util.network.messages.DragonKeyBindMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Wyrmroost.MOD_ID)
@SuppressWarnings("unused")
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final ItemGroup CREATIVE_TAB = ModUtils.itemGroupFactory("wyrmroost", () -> new ItemStack(WRItems.BLUE_GEODE.get()));
    public static final SimpleChannel NETWORK = ModUtils.simplisticChannel(ModUtils.resource(MOD_ID), "1.0");
    
    public Wyrmroost()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.register(Wyrmroost.class);
        
        WREntities.ENTITIES.register(eventBus);
        WRBlocks.BLOCKS.register(eventBus);
        WRItems.ITEMS.register(eventBus);
        SetupIO.CONTAINERS.register(eventBus);
        WRSounds.SOUNDS.register(eventBus);
        ForgeRegistries.MOD_DIMENSIONS.register(ModDimension.withFactory(WyrmroostDimension::new).setRegistryName("wyrmroost"));
        ForgeRegistries.DECORATORS.register(new EndOrePlacement().setRegistryName("end_ore"));
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigData.CommonConfig.COMMON_SPEC);
    }
    
    // Mod Bus Event Listeners
    
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);

        DeferredWorkQueue.runLater(SetupWorld::setupOreGen);
        DeferredWorkQueue.runLater(WREntities::registerEntityWorldSpawns);
        NetworkUtils.registerMessages();
    }
    
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        
        WREntities.registerEntityRenders();
        KeyBinds.registerKeys();
        SetupIO.screenSetup();
    }
    
    /**
     * Fire on config change
     */
    @SubscribeEvent
    public static void configLoad(ModConfig.ModConfigEvent evt)
    {
        if (evt.getConfig().getSpec() == ConfigData.CommonConfig.COMMON_SPEC)
            ConfigData.CommonConfig.reload();
    }
    
    /**
     * Registers item colors for rendering
     */
    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();
        
        IItemColor eggColor = (stack, tintIndex) -> ((CustomSpawnEggItem) stack.getItem()).getColors(tintIndex);
        CustomSpawnEggItem.EGG_TYPES.forEach(e -> handler.register(eggColor, e));
    }
    
    
    /**
     * Forge EventBus listeners
     */
    public static class CommonEvents
    {
        /**
         * Register the Mod Dimension
         */
        @SubscribeEvent
        @SuppressWarnings("ConstantConditions")
        public static void registerDimension(RegisterDimensionsEvent evt)
        {
            if (ModUtils.getDimensionInstance() == null)
                DimensionManager.registerDimension(ModUtils.resource("wyrmroost"), WyrmroostDimension.DIM_WYRMROOST, null, true);
        }
        
        /**
         * Attatch our capabilities
         */
        @SubscribeEvent
        public static void attatchWorldCaps(AttachCapabilitiesEvent<World> evt)
        {
            if (evt.getObject().isRemote) return;
            evt.addCapability(ModUtils.resource("overworld_cap"), new WorldCapability.PropertiesDispatcher());
        }
        
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

            ModUtils.L.info(WorldCapability.isPortalTriggered(evt.getWorld()));

            Entity entity = evt.getTarget();
            if (!(entity instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) entity;

            if (player.isSneaking()) dragon.tame(true, player);
            else if (evt.getWorld().isRemote) Minecraft.getInstance().displayGuiScreen(new DebugScreen(dragon));
        }
    }
    
    /**
     * Forge EventBus listeners on CLIENT distribution
     */
    @OnlyIn(Dist.CLIENT)
    public static class ClientEvents
    {
        /**
         * Handles custom keybind pressing
         */
        @SubscribeEvent
        public static void onKeyPress(InputEvent.KeyInputEvent event)
        {
            if (Minecraft.getInstance().world == null) return; // Dont do anything on the main menu screen
            PlayerEntity player = Minecraft.getInstance().player;
            
            // Generic Attack
            if (KeyBinds.genericAttack.isPressed())
            {
                if (!(player.getRidingEntity() instanceof AbstractDragonEntity)) return;
                AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();
                
                if (dragon.noActiveAnimation())
                {
                    dragon.performGenericAttack();
                    Wyrmroost.NETWORK.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.PERFORM_GENERIC_ATTACK));
                }
            }
        }
        
        /**
         * Handles the perspective/position of the camera
         */
        @SubscribeEvent
        public static void cameraPerspective(EntityViewRenderEvent.CameraSetup event)
        {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.player.getRidingEntity();
            if (!(entity instanceof AbstractDragonEntity)) return;
            int i1 = mc.gameSettings.thirdPersonView;
            
            if (i1 != 0) ((AbstractDragonEntity) entity).setMountCameraAngles(i1 == 1);
        }
    }
}
