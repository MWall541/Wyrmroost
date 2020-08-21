package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public class RenderHelper extends RenderType
{
    // == [Render Types] ==

    @SuppressWarnings("ConstantConditions")
    private RenderHelper() { super(null, null, 0, 0, false, false, null, null); } // dummy

    public static RenderType getGlowType(ResourceLocation locationIn)
    {
        RenderState.TextureState textureState = new RenderState.TextureState(locationIn, false, false);
        return makeType("glow", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder()
                .texture(textureState)
                .transparency(RenderState.ADDITIVE_TRANSPARENCY)
                .alpha(RenderState.DEFAULT_ALPHA)
                .build(false));
    }

    public static RenderType getThiccLines(double thickness)
    {
        return makeType("thickened_lines", DefaultVertexFormats.POSITION_COLOR, 1, 256,
                State.getBuilder()
                        .line(new LineState(OptionalDouble.of(thickness)))
                        .layer(PROJECTION_LAYERING)
                        .transparency(TRANSLUCENT_TRANSPARENCY)
                        .writeMask(COLOR_WRITE)
                        .build(false));
    }

    // == [Rendering] ==

    public static void renderWorld(RenderWorldLastEvent evt)
    {
        MatrixStack ms = evt.getMatrixStack();
        float partialTicks = evt.getPartialTicks();

        renderDragonStaff(ms, partialTicks);
        DebugBox.INSTANCE.render(ms);
    }

    private static final Map<Entity, Color> ENTITY_OUTLINE_MAP = new HashMap<>();

    public static void renderEntities(RenderLivingEvent.Pre<? super LivingEntity, ?> event)
    {
        LivingEntity entity = event.getEntity();
        Color color = ENTITY_OUTLINE_MAP.remove(entity);
        if (color != null)
        {
            event.setCanceled(true);

            Minecraft mc = ClientEvents.getClient();
            OutlineLayerBuffer buffer = mc.getRenderTypeBuffers().getOutlineBufferSource();
            MatrixStack ms = event.getMatrixStack();
            LivingRenderer<? super LivingEntity, ?> renderer = event.getRenderer();
            float partialTicks = event.getPartialRenderTick();
            float yaw = MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw);

            buffer.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            renderer.render(entity, yaw, partialTicks, ms, buffer, event.getLight());
            buffer.finish();
        }
    }

    private static void renderDragonStaff(MatrixStack ms, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack == null) return;
        AbstractDragonEntity dragon = DragonStaffItem.getBoundDragon(mc.world, stack);
        if (dragon == null) return;

        DragonStaffItem.getAction(stack).render(dragon, ms, partialTicks);
        renderEntityOutline(dragon, 0, 255, 255, (int) (MathHelper.cos((dragon.ticksExisted + partialTicks) * 0.2f) * 35 + 45));
        LivingEntity target = dragon.getAttackTarget();
        if (target != null) renderEntityOutline(target, 255, 0, 0, 100);
        dragon.getHomePos().ifPresent(pos -> RenderHelper.drawBlockPos(ms, pos, dragon.world, 4, 0xff0000ff));
    }

    public static void renderEntityOutline(Entity entity, int red, int green, int blue, int alpha)
    {
        ENTITY_OUTLINE_MAP.put(entity, new Color(red, green, blue, alpha));
    }

    public static void queueDebugBoxRendering(AxisAlignedBB aabb) { DebugBox.INSTANCE.queue(aabb); }

    public static void drawShape(MatrixStack ms, IVertexBuilder buffer, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha)
    {
        Matrix4f matrix4f = ms.getLast().getMatrix();
        shapeIn.forEachEdge((x1, y1, z1, x2, y2, z2) ->
        {
            buffer.pos(matrix4f, (float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).color(red, green, blue, alpha).endVertex();
            buffer.pos(matrix4f, (float) (x2 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).color(red, green, blue, alpha).endVertex();
        });
    }

    public static void drawBlockPos(MatrixStack ms, BlockPos pos, World world, double lineThickness, int argb)
    {
        Vec3d view = ClientEvents.getProjectedView();
        double x = pos.getX() - view.x;
        double y = pos.getY() - view.y;
        double z = pos.getZ() - view.z;

        IRenderTypeBuffer.Impl impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderHelper.drawShape(ms,
                impl.getBuffer(getThiccLines(lineThickness)),
                world.getBlockState(pos).getShape(world, pos),
                x, y, z,
                ((argb >> 16) & 0xFF) / 255f, ((argb >> 8) & 0xFF) / 255f, (argb & 0xFF) / 255f, ((argb >> 24) & 0xFF) / 255f);

        impl.finish();
    }

    public enum DebugBox
    {
        INSTANCE;

        private int time = 0;
        private AxisAlignedBB aabb = null;
        private int color = 0xff0000ff;

        public void queue(AxisAlignedBB aabb)
        {
            this.aabb = aabb;
            this.time = 550;
        }

        public void setColor(int color)
        {
            this.color = color;
        }

        public void reset()
        {
            this.aabb = null;
            this.time = 0;
            this.color = 0xff0000ff;
        }

        public void render(MatrixStack ms)
        {
            if (!WRConfig.debugMode) return;
            if (aabb == null) return;

            Vec3d view = ClientEvents.getProjectedView();
            double x = view.x;
            double y = view.y;
            double z = view.z;

            IRenderTypeBuffer.Impl type = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
            WorldRenderer.drawBoundingBox(
                    ms, type.getBuffer(RenderType.getLines()),
                    aabb.minX - x,
                    aabb.minY - y,
                    aabb.minZ - z,
                    aabb.maxX - x,
                    aabb.maxY - y,
                    aabb.maxZ - z,
                    (color & 0xff) / 255f,
                    ((color >> 8) & 0xff) / 255f,
                    ((color >> 16) & 0xff) / 255f,
                    ((color >> 24) & 0xff) / 255f);
            type.finish();

            if (--time <= 0) aabb = null;
        }
    }
}
