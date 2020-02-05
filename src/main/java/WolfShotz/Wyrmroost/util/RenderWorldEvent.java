package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.items.DragonStaffItem;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.opengl.GL11;

@EventBusSubscriber(modid = Wyrmroost.MOD_ID)
public class RenderWorldEvent
{
    @SubscribeEvent
    public static void renderWorld(RenderWorldLastEvent event)
    {
        renderDragonStaff();
    }

    public static void renderDragonStaff()
    {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = ModUtils.getHeldStack(player, DragonStaffItem.class);

        if (stack.getItem() instanceof DragonStaffItem)
        {
            AbstractDragonEntity dragon = ((DragonStaffItem) stack.getItem()).getDragon(stack, ModUtils.getServerWorld(player));
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
                    ResourceLocation loc = ModUtils.resource("textures/io/homepos.png");
                    double x = homePos.getX() - d0;
                    double y = homePos.getY() - d1;
                    double z = homePos.getZ() - d2;

                    // save stats
                    GlStateManager.pushMatrix();
                    GlStateManager.depthFunc(519); // 519

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
}
