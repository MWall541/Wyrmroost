package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.ClientEvents;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

    public static AxisAlignedBB debugBox;
    public static int boxTime;

    public static void renderWorld(RenderWorldLastEvent evt)
    {
        MatrixStack ms = evt.getMatrixStack();
        StaffRenderer.render(ms, evt.getPartialTicks());
        renderDebugBox(ms);
    }

    private static void renderDebugBox(MatrixStack ms)
    {
        if (!WRConfig.debugMode) return;
        if (debugBox == null) return;

        Vec3d view = ClientEvents.getProjectedView();
        double x = view.x;
        double y = view.y;
        double z = view.z;

        IRenderTypeBuffer.Impl type = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        WorldRenderer.drawBoundingBox(
                ms, type.getBuffer(RenderType.getLines()),
                debugBox.minX - x,
                debugBox.minY - y,
                debugBox.minZ - z,
                debugBox.maxX - x,
                debugBox.maxY - y,
                debugBox.maxZ - z,
                1, 0, 0, 1);
        type.finish();

        if (--boxTime <= 0) debugBox = null;
    }

    public static void queueRenderBox(AxisAlignedBB aabb)
    {
        debugBox = aabb;
        boxTime = 500;
    }

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
}
