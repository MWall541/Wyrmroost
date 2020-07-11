package WolfShotz.Wyrmroost.client;

import WolfShotz.Wyrmroost.Wyrmroost;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.List;

/**
 * EventBus listeners on CLIENT distribution
 * Also a client helper class because yes.
 */
@SuppressWarnings("unused")
public class ClientEvents
{
    public static final List<Runnable> CALL_BACKS = Lists.newArrayList();

    public static final ResourceLocation RR_BREATH_0 = Wyrmroost.rl("entity/projectiles/rr_breath/blue_fire_0");
    public static final ResourceLocation RR_BREATH_1 = Wyrmroost.rl("entity/projectiles/rr_breath/blue_fire_1");

    public static void stitchTextures(TextureStitchEvent.Pre evt)
    {
        if (evt.getMap().getTextureLocation() == AtlasTexture.LOCATION_BLOCKS_TEXTURE)
        {
            evt.addSprite(RR_BREATH_0);
            evt.addSprite(RR_BREATH_1);
        }
    }

    /**
     * Handles the perspective/position of the camera
     */
//    @SubscribeEvent
//    public static void cameraPerspective(EntityViewRenderEvent.CameraSetup event)
//    {
//        Minecraft mc = Minecraft.getInstance();
//        Entity entity = mc.player.getRidingEntity();
//        if (!(entity instanceof AbstractDragonEntity)) return;
//        int i1 = mc.gameSettings.thirdPersonView;
//
//        if (i1 != 0) ((AbstractDragonEntity) entity).setMountCameraAngles(i1 == 1);
//    }
//

    // for class loading issues
    public static Minecraft getClient() { return Minecraft.getInstance(); }

    // for class loading issues
    public static PlayerEntity getPlayer() { return Minecraft.getInstance().player; }
}
