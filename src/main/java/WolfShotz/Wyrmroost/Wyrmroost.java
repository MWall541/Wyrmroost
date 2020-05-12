package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.render.RenderEvents;
import WolfShotz.Wyrmroost.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.network.NetworkUtils;
import WolfShotz.Wyrmroost.registry.*;
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
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Wyrmroost.MOD_ID)
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final ItemGroup ITEM_GROUP = new WRItemGroup();
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(rl("network"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public Wyrmroost()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        bus.addListener(this::commonSetup);
        bus.addListener(this::configLoad);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            MinecraftForge.EVENT_BUS.register(ClientEvents.class);
            MinecraftForge.EVENT_BUS.addListener(RenderEvents::renderWorld);
            bus.addListener(this::clientSetup);
            bus.addListener(this::registerItemColors);
        });

        WREntities.ENTITIES.register(bus);
        WRBlocks.BLOCKS.register(bus);
        WRItems.ITEMS.register(bus);
//        WRFluids.FLUIDS.register(bus);
        WRIO.CONTAINERS.register(bus);
        WRSounds.SOUNDS.register(bus);
//        WRBiomes.BIOMES.register(bus);
//        WRWorld.FEATURES.register(bus);
//        bus.addGenericListener(ModDimension.class, (RegistryEvent.Register<ModDimension> e) -> e.getRegistry().register(ModDimension.withFactory(WyrmroostDimension::new).setRegistryName("wyrmroost")));

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WRConfig.CommonConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WRConfig.ClientConfig.CLIENT_SPEC);
    }

    public void commonSetup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(WRWorld::setupWorldGen);
        DeferredWorkQueue.runLater(WREntities::registerEntityWorldSpawns);
        NetworkUtils.registerMessages();
    }

    public void clientSetup(final FMLClientSetupEvent event)
    {
        ClientEvents.registerEntityRenders();
        WRKeyBinds.registerKeys();
        WRIO.screenSetup();
    }

    public void configLoad(ModConfig.ModConfigEvent evt)
    {
        if (evt.getConfig().getSpec() == WRConfig.CommonConfig.COMMON_SPEC)
            WRConfig.CommonConfig.reload();
        if (evt.getConfig().getSpec() == WRConfig.ClientConfig.CLIENT_SPEC)
            WRConfig.ClientConfig.reload();
    }

    public void registerItemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();
        IItemColor eggColor = (stack, tintIndex) -> ((CustomSpawnEggItem) stack.getItem()).getColors(tintIndex);
        CustomSpawnEggItem.EGG_TYPES.forEach(e -> handler.register(eggColor, e));
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
     * Its still <b>Creative Tab</b>. Idc what anyone says.
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
