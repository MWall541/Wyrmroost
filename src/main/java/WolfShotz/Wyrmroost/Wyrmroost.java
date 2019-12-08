package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.world.EndOrePlacement;
import WolfShotz.Wyrmroost.content.world.dimension.WyrmroostDimension;
import WolfShotz.Wyrmroost.registry.*;
import WolfShotz.Wyrmroost.util.ConfigData;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Wyrmroost.MOD_ID)
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final ItemGroup CREATIVE_TAB = new ItemGroup("wyrmroost") {
        @Override public ItemStack createIcon() { return new ItemStack(ModItems.GEODE_BLUE.get()); }
    };

    private static final String CHANNEL_VER = "1.0";
    public static SimpleChannel network = NetworkRegistry.ChannelBuilder
            .named(ModUtils.resource(MOD_ID))
            .clientAcceptedVersions(CHANNEL_VER::equals)
            .serverAcceptedVersions(CHANNEL_VER::equals)
            .networkProtocolVersion(() -> CHANNEL_VER)
            .simpleChannel();

    public Wyrmroost() {
        registerObjects();
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigData.COMMON_SPEC, "wyrmroost.toml");
    }

    /**
     * FML Common Setup Event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.Common.class);
        
        SetupWorld.setupOreGen();
        ModEntities.registerEntityWorldSpawns();
        NetworkUtils.registerMessages();
    }

    /**
     * FML Client Setup Event
     */
    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.Client.class);
        
        EventHandler.Client.registerItemColors();
        ModEntities.registerEntityRenders();
        ModKeys.registerKeys();
        SetupIO.screenSetup();
    }
    
    private void registerObjects() {
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS    .register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS      .register(FMLJavaModLoadingContext.get().getModEventBus());
        SetupIO.CONTAINERS  .register(FMLJavaModLoadingContext.get().getModEventBus());
        ModSounds.SOUNDS    .register(FMLJavaModLoadingContext.get().getModEventBus());
        ForgeRegistries.MOD_DIMENSIONS.register(ModDimension.withFactory(WyrmroostDimension::new).setRegistryName("dim_wyrmroost"));
        ForgeRegistries.DECORATORS.register(new EndOrePlacement().setRegistryName("end_ore"));
    }
}
