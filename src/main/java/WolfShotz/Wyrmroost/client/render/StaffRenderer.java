package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.List;

/**
 * @deprecated - Needs cleaning up and merged with {@link RenderEvents}
 */
public class StaffRenderer
{
    private static ShaderGroup outlineShader;
    public static final List<Entity> outlineEntitiesQueue = Lists.newArrayList();
    public static final OutlineLayerBuffer outlineLayerBuffer = new OutlineLayerBuffer(Minecraft.getInstance().getRenderTypeBuffers().getBufferSource());
    private static final ResourceLocation SHADER_LOC = new ResourceLocation("shaders/post/entity_outline.json");

    public static void render(MatrixStack ms)
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack == null) return;
        AbstractDragonEntity dragon = DragonStaffItem.getBoundDragon(mc.world, stack);
        if (dragon == null)
        {
            if (outlineShader != null)
            {
                outlineShader.close();
                outlineShader = null;
            }
            return;
        }

        outlineEntitiesQueue.add(dragon);
        DragonStaffItem.getAction(stack).render(dragon, ms);
//        renderEntityOutlines(ms);
        RenderEvents.drawBlockPos(ms, dragon.getHomePosition(), dragon.world, 4, 0xff0000ff);
    }

    public static void renderEntityOutlines(MatrixStack ms)
    {
        Minecraft mc = Minecraft.getInstance();
        Vec3d view = RenderEvents.getProjectedView();
        float partialTicks = mc.getRenderPartialTicks();

        if (outlineShader == null) makeOutlineShader();
        outlineShader.createBindFramebuffers(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());
        Framebuffer frameBuffer = outlineShader.getFramebufferRaw("final");
        frameBuffer.bindFramebuffer(true);

        EntityRendererManager manager = mc.getRenderManager();
        for (Entity entity : outlineEntitiesQueue)
        { // Interp prev pos to current pos for smoother movement
            RenderSystem.setupOutline();
            double x = MathHelper.lerp(partialTicks, entity.prevPosX, entity.getPosX()) - view.x;
            double y = MathHelper.lerp(partialTicks, entity.prevPosY, entity.getPosY()) - view.y;
            double z = MathHelper.lerp(partialTicks, entity.prevPosZ, entity.getPosZ()) - view.z;
            manager.renderEntityStatic(entity, x, y, z, entity.rotationYaw, partialTicks, ms, outlineLayerBuffer, manager.getPackedLight(entity, partialTicks));
            RenderSystem.teardownOutline();
        }
        frameBuffer.unbindFramebuffer();
        outlineShader.render(partialTicks);
        outlineLayerBuffer.finish();
        outlineEntitiesQueue.clear();

        RenderSystem.enableBlend(); // Blending
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        frameBuffer.framebufferRenderExt(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), false); // unsure
        RenderSystem.disableBlend();
        frameBuffer.bindFramebuffer(false);
    }

    private static void makeOutlineShader()
    {
        Minecraft mc = Minecraft.getInstance();
        if (outlineShader != null) outlineShader.close();
        try
        {
            outlineShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), SHADER_LOC);
            outlineShader.createBindFramebuffers(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight());
        }
        catch (IOException | JsonSyntaxException ioexception)
        {
            Wyrmroost.LOG.warn("Failed to load shader: {}", SHADER_LOC, ioexception);
            outlineShader = null;
        }
    }
}
