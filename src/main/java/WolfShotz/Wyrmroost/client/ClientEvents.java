package WolfShotz.Wyrmroost.client;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.butterfly.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.client.render.entity.canari.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_fruit.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.ldwyrm.LDWyrmRenderer;
import WolfShotz.Wyrmroost.client.render.entity.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.client.render.entity.silverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRKeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler;

/**
 * EventBus listeners on CLIENT distribution
 * Also a client helper class because yes.
 */
@SuppressWarnings("unused")
public class ClientEvents
{
    /**
     * Handles custom keybind pressing
     */
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event)
    {
        if (Minecraft.getInstance().world == null) return; // Dont do anything on the main menu screen
        PlayerEntity player = Minecraft.getInstance().player;

        // Generic Attack
        if (WRKeyBinds.genericAttack.isPressed())
        {
            if (!(player.getRidingEntity() instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();

            if (dragon.noActiveAnimation())
            {
                dragon.performGenericAttack();
                Wyrmroost.NETWORK.sendToServer(new KeybindPacket(dragon, KeybindPacket.PERFORM_GENERIC_ATTACK));
            }
        }
        if (WRKeyBinds.specialAttack.isPressed())
        {
            if (!(player.getRidingEntity() instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();

            if (dragon.noActiveAnimation())
            {
                dragon.performAltAttack(true);
                Wyrmroost.NETWORK.sendToServer(new KeybindPacket(dragon, KeybindPacket.PERFORM_SPECIAL_ATTACK));
            }
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
    public static void registerEntityRenders()
    {
        registerEntityRenderingHandler(WREntities.OVERWORLD_DRAKE.get(), OWDrakeRenderer::new);
        registerEntityRenderingHandler(WREntities.LESSER_DESERTWYRM.get(), LDWyrmRenderer::new);
        registerEntityRenderingHandler(WREntities.SILVER_GLIDER.get(), SilverGliderRenderer::new);
        registerEntityRenderingHandler(WREntities.ROOSTSTALKER.get(), RoostStalkerRenderer::new);
        registerEntityRenderingHandler(WREntities.BUTTERFLY_LEVIATHAN.get(), ButterflyLeviathanRenderer::new);
        registerEntityRenderingHandler(WREntities.DRAGON_FRUIT_DRAKE.get(), DragonFruitDrakeRenderer::new);
        registerEntityRenderingHandler(WREntities.CANARI_WYVERN.get(), CanariWyvernRenderer::new);

        registerEntityRenderingHandler(WREntities.DRAGON_EGG.get(), DragonEggRenderer::new);

        registerEntityRenderingHandler(WREntities.MULTIPART.get(), mgr -> new EntityRenderer<MultiPartEntity>(mgr)
        {
            public ResourceLocation getEntityTexture(MultiPartEntity entity) { return null; }
        });
    }

    // for class loading issues
    public static Minecraft getMinecraft() { return Minecraft.getInstance(); }

    // for class loading issues
    public static PlayerEntity getPlayer() { return Minecraft.getInstance().player; }
}
