package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.blocks.eggblock.EggRenderer;
import WolfShotz.Wyrmroost.content.blocks.eggblock.EggTileEntity;
import WolfShotz.Wyrmroost.event.*;
import WolfShotz.Wyrmroost.network.AnimationMessage;
import WolfShotz.Wyrmroost.network.SendKeyPressMessage;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(Wyrmroost.modID)
public class Wyrmroost
{
    public static final String modID = "wyrmroost";
    public static final ItemGroup creativeTab = new CreativeTab();
    
    public static final String channel = modID;
    private static final String channelver = "1.0";
    public static SimpleChannel network = NetworkRegistry.ChannelBuilder
            .named(ModUtils.location(channel))
            .clientAcceptedVersions(channelver::equals)
            .serverAcceptedVersions(channelver::equals)
            .networkProtocolVersion(() -> channelver)
            .simpleChannel();

    public Wyrmroost() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::debugStick);
        
        SetupOreGen.setupOreGen();
        
        int networkIndex = 0;
        network.registerMessage(++networkIndex, AnimationMessage.class, AnimationMessage::encode, AnimationMessage::new, AnimationMessage::handle);
        network.registerMessage(++networkIndex, SendKeyPressMessage.class, SendKeyPressMessage::encode, SendKeyPressMessage::new, SendKeyPressMessage::handle);

        ModUtils.L.debug("Fired FMLCommon Setup");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
//        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::ridingPerspective);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onKeyPress);
        
        SetupEntities.registerEntityRenders();
        SetupKeyBinds.registerKeys();
        registerSpecialRenders();

        ModUtils.L.info("Fired FMLClientSetup");
    }
    
    /**
     * Needs to be in seperate <code>@OnlyIn</code> annotated method otherwise servers throw a fit...
     */
    @OnlyIn(Dist.CLIENT)
    private void registerSpecialRenders() {
        ClientRegistry.bindTileEntitySpecialRenderer(EggTileEntity.class, new EggRenderer());
    }
    
    private static class CreativeTab extends ItemGroup
    {
        private CreativeTab() { super("wyrmroost"); }

        @Override
        public ItemStack createIcon() { return new ItemStack(SetupItems.itemgeode); }
    }

}
