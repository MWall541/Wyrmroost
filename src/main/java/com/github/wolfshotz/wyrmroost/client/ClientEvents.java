package com.github.wolfshotz.wyrmroost.client;

import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.util.animation.IAnimatable;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.github.wolfshotz.wyrmroost.registry.WRIO;
import com.github.wolfshotz.wyrmroost.registry.WRKeybind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * EventBus listeners on CLIENT distribution
 * Also a client helper class because yes.
 */
@SuppressWarnings("unused")
public class ClientEvents
{
    public static final List<Runnable> CALLBACKS = new ArrayList<>();

    public static void load()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::stitchTextures);
        bus.addListener(ClientEvents::itemColors);

        forgeBus.addListener(RenderHelper::renderWorld);
        forgeBus.addListener(RenderHelper::renderEntities);
        forgeBus.addListener(ClientEvents::cameraPerspective);
    }

    // ====================
    //       Mod Bus
    // ====================

    public static void clientSetup(final FMLClientSetupEvent event)
    {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
        WRKeybind.registerKeys();
        WRIO.screenSetup();
    }

    public static void stitchTextures(TextureStitchEvent.Pre evt)
    {
        if (evt.getMap().getTextureLocation() == AtlasTexture.LOCATION_BLOCKS_TEXTURE)
        {
            evt.addSprite(BreathWeaponRenderer.RR_BREATH_0);
            evt.addSprite(BreathWeaponRenderer.RR_BREATH_1);
        }
    }

    public static void itemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();
        IItemColor eggColor = (stack, tintIndex) -> ((LazySpawnEggItem) stack.getItem()).getColors(tintIndex);
        for (LazySpawnEggItem e : LazySpawnEggItem.EGG_TYPES) handler.register(eggColor, e);
    }

    // =====================
    //      Forge Bus
    // =====================

    public static void cameraPerspective(EntityViewRenderEvent.CameraSetup event)
    {
        Minecraft mc = getClient();
        Entity entity = mc.player.getRidingEntity();
        if (!(entity instanceof AbstractDragonEntity)) return;
        PointOfView view = mc.gameSettings.func_243230_g();

        if (view != PointOfView.FIRST_PERSON)
            ((AbstractDragonEntity) entity).setMountCameraAngles(view == PointOfView.THIRD_PERSON_BACK, event);
    }

    // =====================

    // for class loading issues
    public static Minecraft getClient() { return Minecraft.getInstance(); }

    public static ClientWorld getWorld() { return getClient().world; }

    public static PlayerEntity getPlayer() { return getClient().player; }

    public static Vector3d getProjectedView() { return getClient().gameRenderer.getActiveRenderInfo().getProjectedView(); }

    public static float getPartialTicks() { return getClient().getRenderPartialTicks(); }

    public static boolean handleAnimationPacket(int entityID, int animationIndex)
    {
        World world = ClientEvents.getWorld();
        IAnimatable entity = (IAnimatable) world.getEntityByID(entityID);

        if (animationIndex < 0) entity.setAnimation(IAnimatable.NO_ANIMATION);
        else entity.setAnimation(entity.getAnimations()[animationIndex]);
        return true;
    }
}
