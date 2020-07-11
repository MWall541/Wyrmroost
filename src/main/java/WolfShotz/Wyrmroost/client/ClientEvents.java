package WolfShotz.Wyrmroost.client;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.butterfly.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.client.render.entity.canari.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_fruit.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.ldwyrm.LDWyrmRenderer;
import WolfShotz.Wyrmroost.client.render.entity.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.projectile.BreathWeaponRenderer;
import WolfShotz.Wyrmroost.client.render.entity.projectile.GeodeTippedArrowRenderer;
import WolfShotz.Wyrmroost.client.render.entity.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.client.render.entity.royal_red.RoyalRedRenderer;
import WolfShotz.Wyrmroost.client.render.entity.silverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import static net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler;

/**
 * EventBus listeners on CLIENT distribution
 * Also a client helper class because yes.
 */
@SuppressWarnings("unused")
public class ClientEvents
{
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
    public static void registerEntityRenders()
    {
        registerEntityRenderingHandler(WREntities.OVERWORLD_DRAKE.get(), OWDrakeRenderer::new);
        registerEntityRenderingHandler(WREntities.LESSER_DESERTWYRM.get(), LDWyrmRenderer::new);
        registerEntityRenderingHandler(WREntities.SILVER_GLIDER.get(), SilverGliderRenderer::new);
        registerEntityRenderingHandler(WREntities.ROOSTSTALKER.get(), RoostStalkerRenderer::new);
        registerEntityRenderingHandler(WREntities.BUTTERFLY_LEVIATHAN.get(), ButterflyLeviathanRenderer::new);
        registerEntityRenderingHandler(WREntities.DRAGON_FRUIT_DRAKE.get(), DragonFruitDrakeRenderer::new);
        registerEntityRenderingHandler(WREntities.CANARI_WYVERN.get(), CanariWyvernRenderer::new);
        registerEntityRenderingHandler(WREntities.ROYAL_RED.get(), RoyalRedRenderer::new);

        registerEntityRenderingHandler(WREntities.BLUE_GEODE_ARROW.get(), mgr -> new GeodeTippedArrowRenderer(mgr, Wyrmroost.rl("textures/entity/projectiles/blue_geode_tipped_arrow.png")));
        registerEntityRenderingHandler(WREntities.RED_GEODE_ARROW.get(), mgr -> new GeodeTippedArrowRenderer(mgr, Wyrmroost.rl("textures/entity/projectiles/red_geode_tipped_arrow.png")));
        registerEntityRenderingHandler(WREntities.PURPLE_GEODE_ARROW.get(), mgr -> new GeodeTippedArrowRenderer(mgr, Wyrmroost.rl("textures/entity/projectiles/purple_geode_tipped_arrow.png")));

        registerEntityRenderingHandler(WREntities.FIRE_BREATH.get(), BreathWeaponRenderer::new);

        registerEntityRenderingHandler(WREntities.DRAGON_EGG.get(), DragonEggRenderer::new);

        registerEntityRenderingHandler(WREntities.MULTIPART.get(), mgr -> new EntityRenderer<MultiPartEntity>(mgr)
        {
            public ResourceLocation getEntityTexture(MultiPartEntity entity) { return null; }
        });
    }

    // for class loading issues
    public static Minecraft getClient() { return Minecraft.getInstance(); }

    // for class loading issues
    public static PlayerEntity getPlayer() { return Minecraft.getInstance().player; }
}
