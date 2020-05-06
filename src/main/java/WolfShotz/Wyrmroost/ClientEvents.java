package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.client.render.entity.butterfly.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.client.render.entity.canari.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_egg.DragonEggRenderer;
import WolfShotz.Wyrmroost.client.render.entity.dragon_fruit.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.less_dwyrm.LessDWyrmRenderer;
import WolfShotz.Wyrmroost.client.render.entity.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.client.render.entity.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.client.render.entity.silverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.content.io.screen.DebugScreen;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRIO;
import WolfShotz.Wyrmroost.registry.WRKeyBinds;
import WolfShotz.Wyrmroost.util.ConfigData;
import WolfShotz.Wyrmroost.util.network.messages.DragonKeyBindMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler;

/**
 * EventBus listeners on CLIENT distribution
 * Also a client helper class because yes.
 */
@SuppressWarnings("unused")
public class ClientEvents
{
    public static void onModConstruction(IEventBus bus)
    {
        bus.addListener(ClientEvents::clientSetup);
        bus.addListener(ClientEvents::registerItemColors);
    }

    public static void clientSetup(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);

        registerEntityRenders();
        WRKeyBinds.registerKeys();
        WRIO.screenSetup();
    }

    /**
     * Registers item colors for rendering
     */
    public static void registerItemColors(ColorHandlerEvent.Item evt)
    {
        ItemColors handler = evt.getItemColors();

        IItemColor eggColor = (stack, tintIndex) -> ((CustomSpawnEggItem) stack.getItem()).getColors(tintIndex);
        CustomSpawnEggItem.EGG_TYPES.forEach(e -> handler.register(eggColor, e));
    }

    // ==========================================================
    //  Forge Eventbus listeners
    //
    //  Anything below here isnt related to the mod bus,
    //  so like runtime stuff (Non-registry stuff)
    // ==========================================================

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
                Wyrmroost.NETWORK.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.PERFORM_GENERIC_ATTACK));
            }
        }
        if (WRKeyBinds.specialAttack.isPressed())
        {
            if (!(player.getRidingEntity() instanceof AbstractDragonEntity)) return;
            AbstractDragonEntity dragon = (AbstractDragonEntity) player.getRidingEntity();

            if (dragon.noActiveAnimation())
            {
                dragon.performAltAttack(true);
                Wyrmroost.NETWORK.sendToServer(new DragonKeyBindMessage(dragon, DragonKeyBindMessage.PERFORM_SPECIAL_ATTACK));
            }
        }
    }

    /**
     * Handles the perspective/position of the camera
     */
    @SubscribeEvent
    public static void cameraPerspective(EntityViewRenderEvent.CameraSetup event)
    {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.player.getRidingEntity();
        if (!(entity instanceof AbstractDragonEntity)) return;
        int i1 = mc.gameSettings.thirdPersonView;

        if (i1 != 0) ((AbstractDragonEntity) entity).setMountCameraAngles(i1 == 1);
    }

    public static AxisAlignedBB debugBox;

    // ====================
    public static int time;

    @SubscribeEvent
    public static void renderWorld(RenderWorldLastEvent event)
    {
//        renderDragonStaff(); todo
        renderDebugBox(event.getMatrixStack());
    }

    public static void renderDragonStaff()
    {
    }

    public static void renderDebugBox(MatrixStack ms)
    {
        if (!ConfigData.debugMode) return;
        if (debugBox == null) return;

        Vec3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double x = view.x;
        double y = view.y;
        double z = view.z;

        IRenderTypeBuffer.Impl type = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        IVertexBuilder builder = type.getBuffer(RenderType.getLines());
        WorldRenderer.drawBoundingBox(
                ms, builder,
                debugBox.minX - x,
                debugBox.minY - y,
                debugBox.minZ - z,
                debugBox.maxX - x,
                debugBox.maxY - y,
                debugBox.maxZ - z,
                1, 0, 0, 1);

        if (--time <= 0) debugBox = null;
    }

    public static void queueRenderBox(AxisAlignedBB aabb)
    {
        debugBox = aabb;
        time = 500;
    }

    public static void debugScreen(AbstractDragonEntity dragon)
    {
        Minecraft.getInstance().displayGuiScreen(new DebugScreen(dragon));
    }

    public static void bookScreen(ItemStack stack)
    {
//        if (stack.getItem() != WRItems.TARRAGON_TOME.get()) return;
//        Minecraft.getInstance().displayGuiScreen(new TarragonTomeScreen(stack));
    }

    public static void registerEntityRenders()
    {
        registerEntityRenderingHandler(WREntities.OVERWORLD_DRAKE.get(), OWDrakeRenderer::new);
        registerEntityRenderingHandler(WREntities.MINUTUS.get(), LessDWyrmRenderer::new);
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
}
