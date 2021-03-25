package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.DebugRendering;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.OptionalDouble;

public class RenderHelper extends RenderType
{
    // == [Render Types] ==

    @SuppressWarnings("ConstantConditions")
    private RenderHelper()
    {
        super(null, null, 0, 0, false, false, null, null); // dummy
    }

    public static RenderType getAdditiveGlow(ResourceLocation locationIn)
    {
        return create("glow_additive", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true, State.builder()
                .setTextureState(new TextureState(locationIn, false, false))
                .setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setAlphaState(DEFAULT_ALPHA)
                .createCompositeState(false));
    }

    public static RenderType getTranslucentGlow(ResourceLocation texture)
    {
        return create("glow_transluscent", DefaultVertexFormats.NEW_ENTITY, 7, 256, false, true, State.builder()
                .setTextureState(new TextureState(texture, false, false))
                .setCullState(NO_CULL)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setAlphaState(DEFAULT_ALPHA)
                .createCompositeState(false));
    }

    public static RenderType getThiccLines(double thickness)
    {
        return create("thickened_lines", DefaultVertexFormats.POSITION_COLOR, 1, 256, State.builder()
                .setLineState(new LineState(OptionalDouble.of(thickness)))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false));
    }

    // == [Rendering] ==

    public static void renderWorld(RenderWorldLastEvent evt)
    {
        MatrixStack ms = evt.getMatrixStack();
        float partialTicks = evt.getPartialTicks();

        renderDragonStaff(ms, partialTicks);
        if (WRConfig.debugMode) DebugRendering.render(ms, partialTicks);
    }

    private static final Object2IntMap<Entity> ENTITY_OUTLINE_MAP = new Object2IntOpenHashMap<>(1);

    public static void renderEntityOutline(Entity entity, int red, int green, int blue, int alpha)
    {
        ENTITY_OUTLINE_MAP.put(entity, ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF)));
    }

    // todo: find a better, shaders friendly way to do this
    public static void renderEntities(RenderLivingEvent.Pre<? super LivingEntity, ?> event)
    {
        LivingEntity entity = event.getEntity();
        int color = ENTITY_OUTLINE_MAP.removeInt(entity);
        if (color != 0)
        {
            event.setCanceled(true);

            Minecraft mc = ClientEvents.getClient();
            OutlineLayerBuffer buffer = mc.renderBuffers().outlineBufferSource();
            MatrixStack ms = event.getMatrixStack();
            LivingRenderer<? super LivingEntity, ?> renderer = event.getRenderer();
            float partialTicks = event.getPartialRenderTick();
            float yaw = MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot);

            buffer.setColor((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
            renderer.render(entity, yaw, partialTicks, ms, buffer, 15728640);
            buffer.endOutlineBatch();
        }
    }

    private static void renderDragonStaff(MatrixStack ms, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack == null) return;
        TameableDragonEntity dragon = DragonStaffItem.getBoundDragon(mc.level, stack);
        if (dragon == null) return;

        DragonStaffItem.getAction(stack).render(dragon, ms, partialTicks);
        if (WRConfig.renderEntityOutlines)
        {
            renderEntityOutline(dragon, 0, 255, 255, (int) (MathHelper.cos((dragon.tickCount + partialTicks) * 0.2f) * 35 + 45));
            LivingEntity target = dragon.getTarget();
            if (target != null) renderEntityOutline(target, 255, 0, 0, 100);
        }
        dragon.getHomePos().ifPresent(pos -> RenderHelper.drawBoxAt(ms, pos, dragon.level, 4, 0xff0000ff));
    }

    public static void drawShape(MatrixStack ms, IVertexBuilder buffer, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha)
    {
        Matrix4f matrix4f = ms.last().pose();
        shapeIn.forAllEdges((x1, y1, z1, x2, y2, z2) ->
        {
            buffer.vertex(matrix4f, (float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).color(red, green, blue, alpha).endVertex();
            buffer.vertex(matrix4f, (float) (x2 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).color(red, green, blue, alpha).endVertex();
        });
    }

    public static void drawBoxAt(MatrixStack ms, BlockPos pos, World world, double lineThickness, int argb)
    {
        Vector3d view = ClientEvents.getProjectedView();
        double x = pos.getX() - view.x;
        double y = pos.getY() - view.y;
        double z = pos.getZ() - view.z;

        IRenderTypeBuffer.Impl impl = Minecraft.getInstance().renderBuffers().bufferSource();
        drawShape(ms,
                impl.getBuffer(getThiccLines(lineThickness)),
                world.getBlockState(pos).getShape(world, pos),
                x, y, z,
                ((argb >> 16) & 0xFF) / 255f, ((argb >> 8) & 0xFF) / 255f, (argb & 0xFF) / 255f, ((argb >> 24) & 0xFF) / 255f);

        impl.endBatch();
    }

    public static void drawAABB(MatrixStack ms, AxisAlignedBB aabb, int argb)
    {
        WorldRenderer.renderLineBox(ms,
                Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(lines()),
                aabb,
                ((argb >> 16) & 0xFF) / 255f,
                ((argb >> 8) & 0xFF) / 255f,
                (argb & 0xFF) / 255f,
                ((argb >> 24) & 0xFF) / 255f);
    }
}
