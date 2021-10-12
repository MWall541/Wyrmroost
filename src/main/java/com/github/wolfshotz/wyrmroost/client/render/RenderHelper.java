package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.book.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class RenderHelper extends RenderType
{
    // == [Render Types] ==

    private static final RenderType TRANSPARENT = create("transparent_color", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, false, true, RenderType.State
            .builder()
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .createCompositeState(false));

    public static final RenderType CUTOUT_TRANSLUSCENT = create("cutout_transluscent", DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 131072, true, false, RenderType.State
            .builder()
            .setShadeModelState(SMOOTH_SHADE)
            .setLightmapState(LIGHTMAP)
            .setTextureState(BLOCK_SHEET)
            .setTransparencyState(RenderState.TRANSLUCENT_TRANSPARENCY)
            .setOutputState(RenderState.TRANSLUCENT_TARGET)
            .createCompositeState(true));

    @SuppressWarnings("ConstantConditions")
    private RenderHelper()
    {
        super(null, null, 0, 0, false, false, null, null); // dummy
    }

    public static RenderType getAdditiveGlow(ResourceLocation locationIn)
    {
        return create("glow_additive", DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, false, true, State.builder()
                .setTextureState(new TextureState(locationIn, false, false))
                .setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setAlphaState(DEFAULT_ALPHA)
                .createCompositeState(false));
    }

    public static RenderType getTranslucentGlow(ResourceLocation texture)
    {
        return create("glow_transluscent", DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, false, true, State.builder()
                .setTextureState(new TextureState(texture, false, false))
                .setCullState(NO_CULL)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setAlphaState(DEFAULT_ALPHA)
                .createCompositeState(false));
    }

    public static RenderType getThiccLines(double thickness)
    {
        return create("thickened_lines", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, State.builder()
                .setLineState(new LineState(OptionalDouble.of(thickness)))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false));
    }

    // == [Rendering] ==

    private static final ResourceLocation GUI_ICONS = Wyrmroost.id("textures/gui/overlay/icons.png");

    public static void renderWorld(RenderWorldLastEvent evt)
    {
        MatrixStack ms = evt.getMatrixStack();
        float partialTicks = evt.getPartialTicks();

        ms.pushPose();

        if (WRConfig.DEBUG_MODE.get()) DebugRendering.render(ms, partialTicks);
        renderBook(ms, partialTicks);

        ms.popPose();
    }

    public static void renderOverlay(RenderGameOverlayEvent evt)
    {
        if (evt.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            Entity vehicle = ClientEvents.getPlayer().getVehicle();
            if (vehicle instanceof TameableDragonEntity && ((TameableDragonEntity) vehicle).isFlying())
            {
                ClientEvents.getClient().textureManager.bind(GUI_ICONS);
                int y = ClientEvents.getClient().getWindow().getScreenHeight() / 2 - 24;
                int yOff = ClientEvents.keybindFlight? 24 : 0;
                AbstractGui.blit(evt.getMatrixStack(), 0, y, 0, yOff, 24, 24, 64, 64);
            }
        }
    }

    public static void drawShape(MatrixStack ms, IVertexBuilder buffer, VoxelShape shape, double x, double y, double z, int argb)
    {
        Matrix4f matrix = ms.last().pose();
        float alpha = ((argb >> 24) & 0xFF) / 255f;
        float red = ((argb >> 16) & 0xFF) / 255f;
        float green = ((argb >> 8) & 0xFF) / 255f;
        float blue = (argb & 0xFF) / 255f;

        shape.forAllEdges((x1, y1, z1, x2, y2, z2) ->
        {
            buffer.vertex(matrix, (float) (x1 + x), (float) (y1 + y), (float) (z1 + z)).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix, (float) (x2 + x), (float) (y2 + y), (float) (z2 + z)).color(red, green, blue, alpha).endVertex();
        });
    }

    public static void drawShape(MatrixStack ms, VoxelShape shape, double x, double y, double z, int argb)
    {
        IRenderTypeBuffer.Impl impl = getRenderBuffer();
        Vector3d view = ClientEvents.getProjectedView();
        float viewX = (float) (x - view.x);
        float viewY = (float) (y - view.y);
        float viewZ = (float) (z - view.z);

        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> counterClockwiseCuboid(ms.last().pose(),
                impl.getBuffer(TRANSPARENT),
                (float) (x1 + viewX),
                (float) (y1 + viewY),
                (float) (z1 + viewZ),
                (float) (x2 + viewX),
                (float) (y2 + viewY),
                (float) (z2 + viewZ),
                ((argb >> 24) & 0xFF) / 255f,
                ((argb >> 8) & 0xFF) / 255f,
                (argb & 0xFF) / 255f,
                ((argb >> 24) & 0xFF) / 255f));
        drawShape(ms, impl.getBuffer(lines()), shape, viewX, viewY, viewZ, 0xFF000000);

        impl.endBatch();
    }

    public static void drawBlockPos(MatrixStack ms, BlockPos pos, double lineThickness, int argb, boolean getShape)
    {
        IRenderTypeBuffer.Impl impl = getRenderBuffer();
        Vector3d view = ClientEvents.getProjectedView();
        ClientWorld level = ClientEvents.getLevel();
        drawShape(ms,
                impl.getBuffer(getThiccLines(lineThickness)),
                getShape? level.getBlockState(pos).getShape(level, pos) : VoxelShapes.block(),
                pos.getX() - view.x, pos.getY() - view.y, pos.getZ() - view.z,
                argb);
        impl.endBatch();
    }

    public static void counterClockwiseCuboid(Matrix4f matrix, IVertexBuilder buffer, float fromX, float fromY, float fromZ, float toX, float toY, float toZ, float red, float green, float blue, float alpha)
    {
        buffer.vertex(matrix, fromX, fromY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, toY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, toY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, fromY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, fromY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, toY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, toY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, fromY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, fromY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, toY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, toY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, fromY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, fromY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, toY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, toY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, fromY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, fromY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, fromY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, fromY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, fromY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, toY, fromZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, fromX, toY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, toY, toZ).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, toX, toY, fromZ).color(red, green, blue, alpha).endVertex();
    }

    private static final Object2IntMap<Entity> ENTITY_OUTLINE_MAP = new Object2IntOpenHashMap<>(1);

    public static void renderEntityOutline(Entity entity, int red, int green, int blue, int alpha)
    {
        ENTITY_OUTLINE_MAP.put(entity, ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF)));
    }

    public static IRenderTypeBuffer.Impl getRenderBuffer()
    {
        return Minecraft.getInstance().renderBuffers().bufferSource();
    }

    private static final Matrix4f flipX = Matrix4f.createScaleMatrix(-1, 1, 1);
    private static final Matrix3f flipXNormal = new Matrix3f(flipX);

    public static void mirrorX(MatrixStack matrixStack)
    {
        matrixStack.last().pose().multiply(flipX);
//        matrixStack.last().normal().multiplyBackward(flipXNormal);
//        matrixStack.last().normal().mul(flipXNormal);
    }

    // todo: find a better, shaders friendly way to do this
    public static void renderEntities(RenderLivingEvent.Pre<? super LivingEntity, ?> event)
    {
        LivingEntity entity = event.getEntity();
        MatrixStack ms = event.getMatrixStack();
        LivingRenderer<? super LivingEntity, ?> renderer = event.getRenderer();
        float partialTicks = event.getPartialRenderTick();

        int color = ENTITY_OUTLINE_MAP.removeInt(entity);
        if (color != 0)
        {
            event.setCanceled(true);

            Minecraft mc = ClientEvents.getClient();
            OutlineLayerBuffer buffer = mc.renderBuffers().outlineBufferSource();
            float yaw = MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot);

            buffer.setColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
            renderer.render(entity, yaw, partialTicks, ms, buffer, 15728640);
            buffer.endOutlineBatch();
        }
    }

    private static void renderBook(MatrixStack ms, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.TARRAGON_TOME.get());
        if (stack == null) return;
        TameableDragonEntity dragon = TarragonTomeItem.getBoundDragon(mc.level, stack);
        TarragonTomeItem.getAction(stack).render(dragon, ms, partialTicks);
        if (dragon == null) return;

        if (WRConfig.RENDER_OUTLINES.get())
        {
            renderEntityOutline(dragon, 0, 255, 255, (int) (MathHelper.cos((dragon.tickCount + partialTicks) * 0.2f) * 35 + 45));
            LivingEntity target = dragon.getTarget();
            if (target != null) renderEntityOutline(target, 255, 0, 0, 100);
        }
        BlockPos pos = dragon.getHomePos();
        if (pos != null)
            RenderHelper.drawBlockPos(ms, pos, 4, 0xff0000ff, false);
    }
}

