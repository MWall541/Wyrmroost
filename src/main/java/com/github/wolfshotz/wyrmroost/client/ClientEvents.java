package com.github.wolfshotz.wyrmroost.client;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.registry.*;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.block.WoodType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.github.wolfshotz.wyrmroost.util.ModUtils.cast;

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

    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::stitchTextures);
        bus.addListener(ClientEvents::itemColors);
        bus.addListener(ClientEvents::bakeParticles);

        forgeBus.addListener(RenderHelper::renderWorld);
        forgeBus.addListener(RenderHelper::renderEntities);
        forgeBus.addListener(ClientEvents::cameraPerspective);
    }

    // ====================
    //       Mod Bus
    // ====================

    public static void clientSetup(final FMLClientSetupEvent event)
    {
        WRKeybind.registerKeys();

        for (EntityType<?> entry : ModUtils.getRegistryEntries(WREntities.REGISTRY))
        {
            if (entry instanceof WREntities.Type)
            {
                WREntities.Type<? super Entity> custom = cast(entry);
                RenderingRegistry.registerEntityRenderingHandler(custom, custom.renderer.get());
            }
        }

        event.enqueueWork(() ->
        {
            WRIO.screenSetup();

            WoodType.values().filter(w -> w.name().contains(Wyrmroost.MOD_ID)).forEach(Atlases::addWoodType);

            WRBlockEntities.RENDERERS.forEach((del, render) -> ClientRegistry.bindTileEntityRenderer(del.get(), render.get()));
            WRBlockEntities.RENDERERS.clear();
        });
    }

    public static void bakeParticles(ParticleFactoryRegisterEvent event)
    {
        for (ParticleType<?> entry : ModUtils.getRegistryEntries(WRParticles.REGISTRY))
        {
            if (entry instanceof WRParticles.Type<?>)
            {
                WRParticles.Type<?> type = (WRParticles.Type<?>) entry;
                getClient().particleEngine.register(type, sprite -> ((d, w, x, y, z, xS, yS, zS) -> type.getFactory().create(cast(d), w, sprite, x, y, z, xS, yS, zS)));
            }
        }
    }

    public static void stitchTextures(TextureStitchEvent.Pre evt)
    {
        if (evt.getMap().location() == AtlasTexture.LOCATION_BLOCKS)
            evt.addSprite(BreathWeaponRenderer.BLUE_FIRE);
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
