package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.RenderEvents;
import WolfShotz.Wyrmroost.items.LazySpawnEggItem;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.network.packets.RenameEntityPacket;
import WolfShotz.Wyrmroost.network.packets.StaffActionPacket;
import WolfShotz.Wyrmroost.registry.*;
import WolfShotz.Wyrmroost.util.CallbackHandler;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Wyrmroost.MOD_ID)
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);
    public static final ItemGroup ITEM_GROUP = new WRItemGroup();
    public static final SimpleChannel NETWORK = buildChannel();
    public static final CallbackHandler<?> CALLBACK = new CallbackHandler<>();

    public Wyrmroost()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        bus.addListener(this::commonSetup);
        bus.addListener(this::configLoad);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
        {
            MinecraftForge.EVENT_BUS.register(ClientEvents.class);
            MinecraftForge.EVENT_BUS.addListener(RenderEvents::renderWorld);
            bus.addListener(this::clientSetup);
            bus.addListener(ClientEvents::stitchTextures);
            bus.addListener(this::registerItemColors);
        });

        WREntities.REGISTRY.register(bus);
        WRBlocks.REGISTRY.register(bus);
        WRItems.REGISTRY.register(bus);
//        WRFluids.FLUIDS.register(bus);
        WRIO.CONTAINERS.register(bus);
        WRSounds.deferred().register(bus);
//        WRBiomes.BIOMES.register(bus);
//        WRWorld.FEATURES.register(bus);
//        bus.addGenericListener(ModDimension.class, (RegistryEvent.Register<ModDimension> e) -> e.getRegistry().register(ModDimension.withFactory(WyrmroostDimension::new).setRegistryName("wyrmroost")));

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WRConfig.Common.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WRConfig.Client.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WRConfig.Server.SPEC);
    }

    public void clientSetup(final FMLClientSetupEvent event)
    {
        ClientEvents.CLIENT_CALLBACK.acceptAndClear(null);
        WRKeybind.registerKeys();
        WRIO.screenSetup();
    }

    public void configLoad(ModConfig.ModConfigEvent evt)
    {
        ForgeConfigSpec spec = evt.getConfig().getSpec();
        if (spec == WRConfig.Common.SPEC) WRConfig.Common.reload();
        else if (spec == WRConfig.Client.SPEC) WRConfig.Client.reload();
        else if (spec == WRConfig.Server.SPEC) WRConfig.Server.reload();
    }

    public void registerItemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();
        IItemColor eggColor = (stack, tintIndex) -> ((LazySpawnEggItem) stack.getItem()).getColors(tintIndex);
        LazySpawnEggItem.EGG_TYPES.forEach(e -> handler.register(eggColor, e));
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(() -> CALLBACK.acceptAndClear(null));
        DeferredWorkQueue.runLater(WRWorld::setupWorld);
    }

    private static SimpleChannel buildChannel()
    {
        final String PROTOCOL_VERSION = "1.0";
        final SimpleChannel network = NetworkRegistry.ChannelBuilder
                .named(rl("network")).clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        int index = 1;
        network.messageBuilder(AnimationPacket.class, index, NetworkDirection.PLAY_TO_CLIENT).encoder(AnimationPacket::encode).decoder(AnimationPacket::new).consumer(AnimationPacket::handle).add();
        network.messageBuilder(KeybindPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(KeybindPacket::encode).decoder(KeybindPacket::new).consumer(KeybindPacket::handle).add();
        network.messageBuilder(RenameEntityPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(RenameEntityPacket::encode).decoder(RenameEntityPacket::new).consumer(RenameEntityPacket::handle).add();
        network.messageBuilder(StaffActionPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(StaffActionPacket::encode).decoder(StaffActionPacket::new).consumer(StaffActionPacket::handle).add();

        return network;
    }

    /**
     * Register a new Wyrmroost Specific Resource Location. <P>
     * Don't bash me for the method name it makes total sense ffs: <P>
     * <b><i>r</i></b>esource <P>
     * <b><i>l</i></b>ocation <P>
     *
     * @return somethin related to a resource idk
     */
    public static ResourceLocation rl(String path) { return new ResourceLocation(MOD_ID, path); }

    /**
     * C R E A T I V E  T A B. F U C K  Y O U.
     */
    static class WRItemGroup extends ItemGroup
    {
        public WRItemGroup() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(WRItems.BLUE_GEODE.get()); }

        @Override
        public void fill(NonNullList<ItemStack> items)
        {
            super.fill(items);
            if (WRConfig.debugMode)
                items.add(new ItemStack(Items.STICK).setDisplayName(new StringTextComponent("Debug Stick")));
        }
    }
}
