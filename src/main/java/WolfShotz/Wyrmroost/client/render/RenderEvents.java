package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.WRConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class RenderEvents extends RenderType
{
    // == [Render Types] ==

    private RenderEvents() { super(null, null, 0, 0, false, false, null, null); } // dummy

    public static RenderType getGlow(ResourceLocation locationIn)
    {
        RenderState.TextureState textureState = new RenderState.TextureState(locationIn, false, false);
        return RenderType.makeType("glow", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder()
                .texture(textureState)
                .transparency(RenderState.ADDITIVE_TRANSPARENCY)
                .alpha(RenderState.DEFAULT_ALPHA)
                .build(false));
    }

    public static RenderType getOutline(ResourceLocation texture, float u, float v)
    {
        return RenderType.makeType("staff_outline", DefaultVertexFormats.ENTITY, 7, 256, RenderType.State.getBuilder()
                .texture(new RenderState.TextureState(texture, false, false))
                .texturing(new RenderState.OffsetTexturingState(u, v))
                .fog(RenderState.BLACK_FOG)
                .transparency(RenderState.ADDITIVE_TRANSPARENCY)
                .diffuseLighting(RenderState.DIFFUSE_LIGHTING_ENABLED)
                .alpha(RenderState.DEFAULT_ALPHA)
                .cull(RenderState.CULL_DISABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .overlay(RenderState.OVERLAY_ENABLED)
                .depthTest(new RenderState.DepthTestState(GL11.GL_GEQUAL))
                .build(false));
    }


    // == [Rendering] ==

    public static AxisAlignedBB debugBox;
    public static int boxTime;

    public static void renderWorld(RenderWorldLastEvent evt)
    {
        MatrixStack ms = evt.getMatrixStack();
        StaffRenderer.render(ms);
        renderDebugBox(ms);
    }

    public static void renderDebugBox(MatrixStack ms)
    {
        if (!WRConfig.debugMode) return;
        if (debugBox == null) return;

        Vec3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double x = view.x;
        double y = view.y;
        double z = view.z;

        IRenderTypeBuffer.Impl type = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
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
}
