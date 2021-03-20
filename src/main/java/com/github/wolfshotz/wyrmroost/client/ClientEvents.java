package com.github.wolfshotz.wyrmroost.client;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import com.github.wolfshotz.wyrmroost.registry.WRIO;
import com.github.wolfshotz.wyrmroost.registry.WRKeybind;
import com.github.wolfshotz.wyrmroost.registry.WRParticles;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.block.WoodType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * EventBus listeners on CLIENT distribution
 * Also a client helper class because yes.
 */
@SuppressWarnings("unused")
public class ClientEvents
{
    static
    {
        WRDimensionRenderInfo.init();
    }

    public static final List<Runnable> CALLBACKS = new ArrayList<>();

    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::stitchTextures);
        bus.addListener(ClientEvents::itemColors);

        forgeBus.addListener(RenderHelper::renderWorld);
        forgeBus.addListener(RenderHelper::renderEntities);
        forgeBus.addListener(ClientEvents::cameraPerspective);
        forgeBus.addListener(ClientEvents::consumeParticles);
    }

    // ====================
    //       Mod Bus
    // ====================

    public static void clientSetup(final FMLClientSetupEvent event)
    {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
        WRKeybind.registerKeys();
        event.enqueueWork(() ->
        {
            for (Map.Entry<ResourceLocation, Supplier<Supplier<RenderType>>> entry : WRBlocks.RENDER_LOOKUP.entrySet())
            {
                RenderTypeLookup.setRenderLayer(ForgeRegistries.BLOCKS.getValue(entry.getKey()), entry.getValue().get().get());
            }
            WRBlocks.RENDER_LOOKUP.clear();

            WRIO.screenSetup();
            WoodType.values().filter(w -> w.name().contains(Wyrmroost.MOD_ID)).forEach(Atlases::addWoodType);
        });
    }

    public static void consumeParticles(ParticleFactoryRegisterEvent event)
    {
        boolean yarnIsClient = getWorld().isClientSide;
        MinecraftServer forgeHook = ServerLifecycleHooks.getCurrentServer();

        for (ParticleType<?> entry : ModUtils.getRegistryEntries(WRParticles.REGISTRY))
        {
            if (entry instanceof WRParticles.WRParticleType<?>)
            {
                WRParticles.WRParticleType<?> type = (WRParticles.WRParticleType<?>) entry;
                getClient().particleEngine.register(type, sprite -> ((t, w, x, y, z, xS, yS, zS) -> type.getFactory().create(ModUtils.cast(t), w, sprite, x, y, z, xS, yS, zS)));
            }
        }
    }

    public static void stitchTextures(TextureStitchEvent.Pre evt)
    {
        if (evt.getMap().location() == AtlasTexture.LOCATION_BLOCKS)
        {
            evt.addSprite(BreathWeaponRenderer.BLUE_FIRE);
        }
    }

    public static void itemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();
        IItemColor func = (stack, tintIndex) -> ((LazySpawnEggItem) stack.getItem()).getColor(tintIndex);
        for (LazySpawnEggItem e : LazySpawnEggItem.SPAWN_EGGS) handler.register(func, e);
    }

    // =====================
    //      Forge Bus
    // =====================

    public static void cameraPerspective(EntityViewRenderEvent.CameraSetup event)
    {
        Minecraft mc = getClient();
        Entity entity = mc.player.getVehicle();
        if (!(entity instanceof TameableDragonEntity)) return;
        PointOfView view = mc.options.getCameraType();

        if (view != PointOfView.FIRST_PERSON)
            ((TameableDragonEntity) entity).setMountCameraAngles(view == PointOfView.THIRD_PERSON_BACK, event);
    }

    public static void onClientWorldLoad(WorldEvent.Load event)
    {
//        event.getWorld()
    }

    // =====================

    // for class loading issues
    public static Minecraft getClient()
    {
        return Minecraft.getInstance();
    }

    public static ClientWorld getWorld()
    {
        return getClient().level;
    }

    public static PlayerEntity getPlayer()
    {
        return getClient().player;
    }

    public static Vector3d getProjectedView()
    {
        return getClient().gameRenderer.getMainCamera().getPosition();
    }

    public static float getFrameDelta()
    {
        return getClient().getDeltaFrameTime();
    }

    public static boolean handleAnimationPacket(int entityID, int animationIndex)
    {
        World world = ClientEvents.getWorld();
        IAnimatable entity = (IAnimatable) world.getEntity(entityID);

        if (animationIndex < 0) entity.setAnimation(IAnimatable.NO_ANIMATION);
        else entity.setAnimation(entity.getAnimations()[animationIndex]);
        return true;
    }
}
