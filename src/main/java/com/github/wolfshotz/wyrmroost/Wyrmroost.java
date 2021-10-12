package com.github.wolfshotz.wyrmroost;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.network.packets.*;
import com.github.wolfshotz.wyrmroost.registry.*;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
    public static final SimpleChannel NETWORK;

    public Wyrmroost()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonEvents.init();
        if (ModUtils.isClient()) ClientEvents.init();

        WREntities.REGISTRY.register(bus);
        WREntities.Attributes.REGISTRY.register(bus);
        WRBlocks.REGISTRY.register(bus);
        WRBlockEntities.REGISTRY.register(bus);
        WRItems.REGISTRY.register(bus);
        WRIO.REGISTRY.register(bus);
        WRSounds.REGISTRY.register(bus);
        WRWorld.Features.REGISTRY.register(bus);
        WRParticles.REGISTRY.register(bus);
        WREffects.REGISTRY.register(bus);
        WRFluids.REGISTRY.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WRConfig.COMMON);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WRConfig.CLIENT);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WRConfig.SERVER);
    }

    public static ResourceLocation id(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    static
    {
        final String PROTOCOL_VERSION = "1.0";
        final SimpleChannel network = NetworkRegistry.ChannelBuilder
                .named(id("network"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        int index = 0;
        network.messageBuilder(AnimationPacket.class, index, NetworkDirection.PLAY_TO_CLIENT).encoder(AnimationPacket::encode).decoder(AnimationPacket::new).consumer(AnimationPacket::handle).add();
        network.messageBuilder(KeybindHandler.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(KeybindHandler::encode).decoder(KeybindHandler::new).consumer(KeybindHandler::handle).add();
        network.messageBuilder(RenameEntityPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(RenameEntityPacket::encode).decoder(RenameEntityPacket::new).consumer(RenameEntityPacket::handle).add();
        network.messageBuilder(BookActionPacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(BookActionPacket::encode).decoder(BookActionPacket::new).consumer(BookActionPacket::handle).add();
        network.messageBuilder(SGGlidePacket.class, ++index, NetworkDirection.PLAY_TO_SERVER).encoder(SGGlidePacket::encode).decoder(SGGlidePacket::new).consumer(SGGlidePacket::handle).add();
        network.messageBuilder(AddPassengerPacket.class, ++index, NetworkDirection.PLAY_TO_CLIENT).encoder(AddPassengerPacket::encode).decoder(AddPassengerPacket::new).consumer(AddPassengerPacket::handle).add();

        NETWORK = network;
    }
}
