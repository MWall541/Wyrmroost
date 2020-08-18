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
import net.minecraft.client.renderer.entity.EntityRenderer;
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
import net.minecraftforge.client.event.RenderWorldLastEvent;

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

    private static void renderDragonStaff(MatrixStack ms, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack == null) return;
        AbstractDragonEntity dragon = DragonStaffItem.getBoundDragon(mc.world, stack);
        if (dragon == null) return;

        DragonStaffItem.getAction(stack).render(dragon, ms, partialTicks);
        renderEntityOutline(dragon, ms, partialTicks, 0, 255, 255, (int) (MathHelper.cos((dragon.ticksExisted + partialTicks) * 0.2f) * 35 + 45));
        LivingEntity target = dragon.getAttackTarget();
        if (target != null) renderEntityOutline(target, ms, partialTicks, 255, 0, 0, 100);
        dragon.getHomePos().ifPresent(pos -> RenderHelper.drawBlockPos(ms, pos, dragon.world, 4, 0xff0000ff));
    }

    public static void renderEntityOutline(Entity entity, MatrixStack ms, float partialTicks, int red, int green, int blue, int alpha)
    {
        Minecraft mc = ClientEvents.getClient();
        OutlineLayerBuffer buffer = mc.getRenderTypeBuffers().getOutlineBufferSource();
        EntityRenderer<Entity> renderer = (EntityRenderer<Entity>) mc.getRenderManager().getRenderer(entity);
        Vec3d cam = ClientEvents.getProjectedView();
        Vec3d offset = renderer.getRenderOffset(entity, partialTicks);
        double x = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX()) - cam.x + offset.x;
        double y = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY()) - cam.y + offset.y;
        double z = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ()) - cam.z + offset.z;
        float yaw = MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw);

        buffer.setColor(red, green, blue, alpha);
        ms.push();
        ms.translate(x, y, z);
        renderer.render(entity, yaw, partialTicks, ms, buffer, mc.getRenderManager().getPackedLight(entity, partialTicks));
        buffer.finish();
        ms.pop();
    }

    public static void queueDebugBoxRendering(AxisAlignedBB aabb) { DebugBox.INSTANCE.queue(aabb); }

    public static void drawShape(MatrixStack ms, IVertexBuilder buffer, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha)
    {
        Matrix4f matrix4f = ms.getLast().getMatrix();
        shapeIn.forEachEdge((p_230013_12_, p_230013_14_, p_230013_16_, p_230013_18_, p_230013_20_, p_230013_22_) ->
        {
            buffer.pos(matrix4f, (float) (p_230013_12_ + xIn), (float) (p_230013_14_ + yIn), (float) (p_230013_16_ + zIn)).color(red, green, blue, alpha).endVertex();
            buffer.pos(matrix4f, (float) (p_230013_18_ + xIn), (float) (p_230013_20_ + yIn), (float) (p_230013_22_ + zIn)).color(red, green, blue, alpha).endVertex();
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
                    ((color >> 16) & 0xff) / 255f,
                    ((color >> 8) & 0xff) / 255f,
                    (color & 0xff) / 255f,
                    ((color >> 24) & 0xff) / 255f);
            type.finish();

            if (--time <= 0) aabb = null;
        }
    }
}
