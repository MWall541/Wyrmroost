package WolfShotz.Wyrmroost.client;

import WolfShotz.Wyrmroost.client.render.RenderHelper;
import WolfShotz.Wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.LazySpawnEggItem;
import WolfShotz.Wyrmroost.registry.WRIO;
import WolfShotz.Wyrmroost.registry.WRKeybind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
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

        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::stitchTextures);
        bus.addListener(ClientEvents::itemColors);

        MinecraftForge.EVENT_BUS.addListener(RenderHelper::renderWorld);
        MinecraftForge.EVENT_BUS.addListener(RenderHelper::renderEntities);
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::cameraPerspective);
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
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.player.getRidingEntity();
        if (!(entity instanceof AbstractDragonEntity)) return;
        int i1 = mc.gameSettings.thirdPersonView;

        if (i1 != 0) ((AbstractDragonEntity) entity).setMountCameraAngles(i1 == 1, event);
    }

    // =====================

    // for class loading issues
    public static Minecraft getClient() { return Minecraft.getInstance(); }

    // for class loading issues
    public static PlayerEntity getPlayer() { return Minecraft.getInstance().player; }

    public static Vec3d getProjectedView() { return getClient().gameRenderer.getActiveRenderInfo().getProjectedView(); }

}
