package WolfShotz.Wyrmroost.util.entityhelpers.render.layer;

import WolfShotz.Wyrmroost.util.MathUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * A slighly more optimized arrow entity render
 */
public class ArrowLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M>
{
    private final EntityRendererManager renderManager;
    private ArrowEntity arrow;
    
    public ArrowLayer(LivingRenderer<T, M> rendererIn) {
        super(rendererIn);
        this.renderManager = rendererIn.getRenderManager();
    }
    
    public void render(T entityIn, float p_212842_2_, float p_212842_3_, float partialTicks, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
        int i = entityIn.getArrowCountInEntity();
        if (i <= 0) return;
    
        if (arrow == null) arrow = new ArrowEntity(entityIn.world, entityIn.posX, entityIn.posY, entityIn.posZ);
        Random random = new Random((long) entityIn.getEntityId());
        
        RenderHelper.disableStandardItemLighting();
    
        for(int j = 0; j < i; ++j) {
            GlStateManager.pushMatrix();
            
            float f = random.nextFloat();
            float f1 = random.nextFloat();
            float f2 = random.nextFloat();
            RendererModel renderermodel = getEntityModel().getRandomModelBox(random);
            ModelBox modelbox = renderermodel.cubeList.get(random.nextInt(renderermodel.cubeList.size()));
            renderermodel.postRender(0.0625F);
            float f3 = MathHelper.lerp(f, modelbox.posX1, modelbox.posX2) / 16f;
            float f4 = MathHelper.lerp(f1, modelbox.posY1, modelbox.posY2) / 16f;
            float f5 = MathHelper.lerp(f2, modelbox.posZ1, modelbox.posZ2) / 16f;
            GlStateManager.translatef(f3, f4, f5);
            f = -1f * (f * 2f - 1f);
            f1 = -1f * (f1 * 2f - 1f);
            f2 = -1f * (f2 * 2f - 1f);
            float f6 = MathHelper.sqrt(f * f + f2 * f2);
            arrow.prevRotationYaw = arrow.rotationYaw = (float)(Math.atan2((double) f, (double) f2) * (180f / MathUtils.PI));
            arrow.prevRotationPitch = arrow.rotationPitch = (float)(Math.atan2((double) f1, (double) f6) * (180f / MathUtils.PI));
            renderManager.renderEntity(arrow, 0, 0, 0, 0, partialTicks, false);
            
            GlStateManager.popMatrix();
        }
        
        RenderHelper.enableStandardItemLighting();
    }
    
    public boolean shouldCombineTextures() { return false; }
}
