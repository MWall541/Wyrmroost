package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.OutlineLayerBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;

public class StaffRenderer
{
    private static ShaderGroup outlineShader;

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

        renderEntityOutline(dragon, ms, 0xffffff);
        renderHomePos(dragon, stack, ms);
    }

    public static void renderEntityOutline(Entity entity, MatrixStack ms, int color)
    {
        if (outlineShader == null) makeOutlineShader();

        Minecraft mc = Minecraft.getInstance();
        Vec3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
        OutlineLayerBuffer outlineBuffer = mc.getRenderTypeBuffers().getOutlineBufferSource();
        EntityRendererManager manager = mc.getRenderManager();
        float partialTicks = mc.getRenderPartialTicks();
        double x = entity.getPosX() - view.x;
        double y = entity.getPosY() - view.y;
        double z = entity.getPosZ() - view.z;

        outlineShader.createBindFramebuffers(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight());
        outlineShader.getFramebufferRaw("final").framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
        outlineBuffer.setColor(255, 0, 0, 255);
        manager.renderEntityStatic(entity, x, y, z, entity.rotationYaw, partialTicks, ms, outlineBuffer, manager.getPackedLight(entity, partialTicks));
        outlineShader.render(partialTicks);
        mc.getFramebuffer().bindFramebuffer(false);
    }

    private static void renderHomePos(AbstractDragonEntity dragon, ItemStack stack, MatrixStack ms)
    {
        Vec3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        Minecraft mc = Minecraft.getInstance();

        BlockPos pos = dragon.getHomePos().orElse((DragonStaffItem.getAction(stack) == StaffAction.HOME_POS && mc.objectMouseOver instanceof BlockRayTraceResult)? ((BlockRayTraceResult) mc.objectMouseOver).getPos() : null);
        if (pos != null)
        {
            double x = pos.getX() - view.x;
            double y = pos.getY() - view.y;
            double z = pos.getZ() - view.z;

            IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            RenderEvents.drawShape(ms,
                    impl.getBuffer(RenderType.getLines()),
                    dragon.world.getBlockState(pos).getShape(dragon.world, pos),
                    x, y, z,
                    0, 0, 1, 0.85f);

            impl.finish();
        }
    }

    private static void makeOutlineShader()
    {
        if (outlineShader != null) outlineShader.close();

        Minecraft mc = Minecraft.getInstance();
        ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");

        try
        {
            outlineShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourcelocation);
            outlineShader.createBindFramebuffers(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight());
        }
        catch (IOException | JsonSyntaxException ioexception)
        {
            Wyrmroost.LOG.warn("Failed to load shader: {}", resourcelocation, ioexception);
            outlineShader = null;
        }

    }
}
