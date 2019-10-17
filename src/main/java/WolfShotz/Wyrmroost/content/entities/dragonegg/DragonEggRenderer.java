package WolfShotz.Wyrmroost.content.entities.dragonegg;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntitySize;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    private final DragonEggModel EGG_MODEL = new DragonEggModel();
    
    public DragonEggRenderer(EntityRendererManager manager) { super(manager); }
    
    @Override
    public void doRender(DragonEggEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translated(x, y, z);
        renderShapeByType(entity);
        GlStateManager.translated(0, -1.5, 0);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
    
        bindEntityTexture(entity);
        EGG_MODEL.render(entity);
        
        GlStateManager.popMatrix();
        
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DragonEggEntity entity) { return entity.getProperties().TEXTURE; }
    
    /**
     * Render Custom egg sizes / shapes. <P>
     * If none is defined, then calculate the model size according to egg size
     */
    private void renderShapeByType(DragonEggEntity entity) {
        EntitySize size = entity.getSize(entity.getPose());
        if (size == null) return;
        GlStateManager.scalef(size.width * 2.95f, -(size.height * 2), -(size.width * 2.95f));
    }
}
