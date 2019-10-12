package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.event.*;
import WolfShotz.Wyrmroost.util.network.AnimationMessage;
import WolfShotz.Wyrmroost.util.network.DragonKeyBindMessage;
import WolfShotz.Wyrmroost.util.network.EntityMoveMessage;
import WolfShotz.Wyrmroost.util.network.HatchMessage;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Wyrmroost.MOD_ID)
public class Wyrmroost
{
    public static final String MOD_ID = "wyrmroost";
    public static final ItemGroup CREATIVE_TAB = new CreativeTab();

    public static final String NETWORK_CHANNEL = MOD_ID;
    private static final String CHANNEL_VER = "1.0";
    public static SimpleChannel network = NetworkRegistry.ChannelBuilder
            .named(ModUtils.location(NETWORK_CHANNEL))
            .clientAcceptedVersions(CHANNEL_VER::equals)
            .serverAcceptedVersions(CHANNEL_VER::equals)
            .networkProtocolVersion(() -> CHANNEL_VER)
            .simpleChannel();

    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    /**
     * FML Common Setup Event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.Common.class);
        
        SetupOreGen.setupOreGen();
        
        int networkIndex = 0;
        network.registerMessage(++networkIndex, AnimationMessage.class, AnimationMessage::encode, AnimationMessage::new, AnimationMessage::handle);
        network.registerMessage(++networkIndex, DragonKeyBindMessage.class, DragonKeyBindMessage::encode, DragonKeyBindMessage::new, DragonKeyBindMessage::handle);
        network.registerMessage(++networkIndex, EntityMoveMessage.class, EntityMoveMessage::encode, EntityMoveMessage::new, EntityMoveMessage::handle);
        network.registerMessage(++networkIndex, HatchMessage.class, HatchMessage::encode, HatchMessage::new, HatchMessage::handle);
        
        ModUtils.L.debug("Fired FMLCommon Setup");
    }

    /**
     * FML Client Setup Event
     */
    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.Client.class);
        
        SetupEntities.registerEntityRenders();
        SetupKeyBinds.registerKeys();
        SetupIO.screenSetup();

        ModUtils.L.info("Fired FMLClientSetup");
    }

    /**
     * Mod Creative Tab (iTeM gRoUp* rEEE)
     */
    private static class CreativeTab extends ItemGroup
    {
        private CreativeTab() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(SetupItems.geode); }
    }

}
