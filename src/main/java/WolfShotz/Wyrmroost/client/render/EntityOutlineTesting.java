package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;

@Deprecated
public class EntityOutlineTesting
{
    private static final ResourceLocation SHADER_LOC = new ResourceLocation("shaders/post/entity_outline.json");
    private static final OutlineLayerBuffer outlineBufferSource = new OutlineLayerBuffer(ClientEvents.getClient().getRenderTypeBuffers().getBufferSource());
    private static ShaderGroup outlineShader;

    public static void render(Entity entity, MatrixStack ms, float partialTicks)
    {
        Minecraft mc = ClientEvents.getClient();

        if (outlineShader == null) makeOutlineShader();
        outlineShader.createBindFramebuffers(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());

        EntityRenderer<? super Entity> renderer = ClientEvents.getClient().getRenderManager().getRenderer(entity);

        Vec3d offset = renderer.getRenderOffset(entity, partialTicks);
        Vec3d view = ClientEvents.getProjectedView();
        double x = MathHelper.lerp(partialTicks, entity.prevPosX, entity.getPosX()) - view.x + offset.x;
        double y = MathHelper.lerp(partialTicks, entity.prevPosY, entity.getPosY()) - view.y + offset.y;
        double z = MathHelper.lerp(partialTicks, entity.prevPosZ, entity.getPosZ()) - view.z + offset.z;
        ms.push();
        RenderSystem.setupOutline();
        ms.translate(x, y, z);
        renderer.render(entity, entity.rotationYaw, partialTicks, ms, outlineBufferSource, renderer.getPackedLight(entity, partialTicks));
        RenderSystem.teardownOutline();
        ms.pop();
        outlineBufferSource.finish();
    }

    private static void makeOutlineShader()
    {
        Minecraft mc = Minecraft.getInstance();
        if (outlineShader != null) outlineShader.close();
        try
        {
            outlineShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), SHADER_LOC);
        }
        catch (IOException | JsonSyntaxException ioexception)
        {
            Wyrmroost.LOG.warn("Failed to load shader: {}", SHADER_LOC, ioexception);
            outlineShader = null;
        }
    }

//    public static void renderEntityOutlines(MatrixStack ms)
//    {
//        Minecraft mc = Minecraft.getInstance();
//        Vec3d view = RenderEvents.getProjectedView();
//        float partialTicks = mc.getRenderPartialTicks();
//
//        if (outlineShader == null) makeOutlineShader();
//        outlineShader.createBindFramebuffers(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());
//        Framebuffer frameBuffer = outlineShader.getFramebufferRaw("final");
//        frameBuffer.bindFramebuffer(true);
//
//        EntityRendererManager manager = mc.getRenderManager();
//        for (Entity entity : outlineEntitiesQueue)
//        { // Interp prev pos to current pos for smoother movement
//            RenderSystem.setupOutline();
//            double x = MathHelper.lerp(partialTicks, entity.prevPosX, entity.getPosX()) - view.x;
//            double y = MathHelper.lerp(partialTicks, entity.prevPosY, entity.getPosY()) - view.y;
//            double z = MathHelper.lerp(partialTicks, entity.prevPosZ, entity.getPosZ()) - view.z;
//            manager.renderEntityStatic(entity, x, y, z, entity.rotationYaw, partialTicks, ms, outlineLayerBuffer, manager.getPackedLight(entity, partialTicks));
//            RenderSystem.teardownOutline();
//        }
//        frameBuffer.unbindFramebuffer();
//        outlineShader.render(partialTicks);
//        outlineLayerBuffer.finish();
//        outlineEntitiesQueue.clear();
//
//        RenderSystem.enableBlend(); // Blending
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
//        frameBuffer.framebufferRenderExt(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), false); // unsure
//        RenderSystem.disableBlend();
//        frameBuffer.bindFramebuffer(false);
//    }
}
