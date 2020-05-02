package WolfShotz.Wyrmroost;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render.ButterflyLeviathanRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.CanariWyvernEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.CanariWyvernRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderRenderer;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggRenderer;
import WolfShotz.Wyrmroost.content.entities.multipart.MultiPartEntity;
import WolfShotz.Wyrmroost.content.io.screen.DebugScreen;
import WolfShotz.Wyrmroost.content.items.CustomSpawnEggItem;
import WolfShotz.Wyrmroost.content.items.DragonStaffItem;
import WolfShotz.Wyrmroost.registry.WRIO;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRKeyBinds;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.network.messages.DragonKeyBindMessage;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.opengl.GL11;

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
        renderDragonStaff();
        renderDebugBox();
    }

    public static void renderDragonStaff()
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());

        if (stack.getItem() instanceof DragonStaffItem)
        {
            AbstractDragonEntity dragon = DragonStaffItem.getDragon(stack, ModUtils.getServerWorld(player));
            ActiveRenderInfo renderInfo = mc.gameRenderer.getActiveRenderInfo();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            double d0 = renderInfo.getProjectedView().x;
            double d1 = renderInfo.getProjectedView().y;
            double d2 = renderInfo.getProjectedView().z;

            if (dragon != null)
            {
                if (dragon.getHomePos().isPresent())
                {
                    BlockPos homePos = dragon.getHomePos().get();
                    ResourceLocation loc = Wyrmroost.rl("textures/io/homepos.png");
                    double x = homePos.getX() - d0;
                    double y = homePos.getY() - d1;
                    double z = homePos.getZ() - d2;

                    // save states
                    GlStateManager.pushMatrix();
                    GlStateManager.depthFunc(519);

                    // draw
                    GlStateManager.translated(x + 0.5, (y + (Math.sin(dragon.ticksExisted * 0.06) * 0.5)) + 3, z + 0.5);
                    GlStateManager.rotatef(-renderInfo.getYaw(), 0, 1f, 0);
                    GlStateManager.rotatef(renderInfo.getPitch(), 1f, 0, 0);
                    GlStateManager.rotatef(180, 0, 1, 0);
                    mc.textureManager.bindTexture(loc);
                    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(0.5, 0, 0).tex(1, 1).endVertex();
                    buffer.pos(0.5, 1, 0).tex(1, 0).endVertex();
                    buffer.pos(-0.5, 1, 0).tex(0, 0).endVertex();
                    buffer.pos(-0.5, 0, 0).tex(0, 1).endVertex();
                    Tessellator.getInstance().draw();

                    // reset states
                    buffer.setTranslation(0, 0, 0);
                    GlStateManager.depthFunc(515);
                    GlStateManager.popMatrix();
                }
                else
                {
                    World world = mc.world;

                    if (mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK)
                    {
                        BlockPos blockpos = ((BlockRayTraceResult) mc.objectMouseOver).getPos();
                        BlockState blockstate = world.getBlockState(blockpos);
                        if (blockstate.isSolid() && world.getWorldBorder().contains(blockpos))
                        {
                            GlStateManager.enableBlend();
                            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                            GlStateManager.lineWidth(1000);
                            GlStateManager.disableTexture();
                            GlStateManager.depthMask(false);
                            GlStateManager.matrixMode(5889);
                            GlStateManager.pushMatrix();
                            GlStateManager.scalef(1.0F, 1.0F, 0.999F);

                            WorldRenderer.drawShape(blockstate.getShape(world, blockpos, ISelectionContext.forEntity(renderInfo.getRenderViewEntity())), blockpos.getX() - d0, blockpos.getY() - d1, blockpos.getZ() - d2, 0, 0, 1f, 0.2f);

                            GlStateManager.popMatrix();
                            GlStateManager.matrixMode(5888);
                            GlStateManager.depthMask(true);
                            GlStateManager.enableTexture();
                            GlStateManager.disableBlend();
                        }
                    }
                }
            }
        }
    }

    public static void renderDebugBox()
    {
        if (debugBox == null) return;

        Vec3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double x = view.x;
        double y = view.y;
        double z = view.z;

        GlStateManager.depthMask(false);
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        WorldRenderer.drawBoundingBox(
                debugBox.minX - x,
                debugBox.minY - y,
                debugBox.minZ - z,
                debugBox.maxX - x,
                debugBox.maxY - y,
                debugBox.maxZ - z,
                1, 0, 0, 1);

        GlStateManager.enableTexture();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);

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
        registerEntityRenderingHandler(OWDrakeEntity.class, OWDrakeRenderer::new);
        registerEntityRenderingHandler(MinutusEntity.class, MinutusRenderer::new);
        registerEntityRenderingHandler(SilverGliderEntity.class, SilverGliderRenderer::new);
        registerEntityRenderingHandler(RoostStalkerEntity.class, RoostStalkerRenderer::new);
        registerEntityRenderingHandler(ButterflyLeviathanEntity.class, ButterflyLeviathanRenderer::new);
        registerEntityRenderingHandler(DragonFruitDrakeEntity.class, DragonFruitDrakeRenderer::new);
        registerEntityRenderingHandler(CanariWyvernEntity.class, CanariWyvernRenderer::new);

        registerEntityRenderingHandler(DragonEggEntity.class, DragonEggRenderer::new);

        registerEntityRenderingHandler(MultiPartEntity.class, mgr -> new EntityRenderer<MultiPartEntity>(mgr)
        {
            protected ResourceLocation getEntityTexture(MultiPartEntity entity) { return null; }
        });
    }
}
