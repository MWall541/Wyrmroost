package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    private final ResourceLocation TEXTURE = ModUtils.location("textures/block/dragon_egg.png");
    private final DragonEggModel EGG_MODEL = new DragonEggModel();
    
    public DragonEggRenderer(EntityRendererManager manager) {
        super(manager);
    }
    
    @Override
    public void doRender(DragonEggEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translated(x, y, z);
//        GlStateManager.rotated(180, 1, 0, 0);
        renderShapeByType(entity);
        GlStateManager.translated(0, -1.5, 0);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
        
        bindEntityTexture(entity);
        EGG_MODEL.base.render(0.0625f);
        
        GlStateManager.popMatrix();
        
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DragonEggEntity entity) { return TEXTURE; }
    
    private void renderShapeByType(DragonEggEntity entity) {
        DragonEggEntity.DragonTypes type = entity.getDragonTypeEnum();
        
        // Drake
        switch(type) {
            case DRAKE: GlStateManager.scalef(2.0F, -2.0F, -2.0F); break;
            case SILVER_GLIDER: GlStateManager.scalef(1.2F, -1.2F, -1.2F); break;
            case ROOST_STALKER: GlStateManager.scalef(0.7f, -0.7f, -0.7f); break;
        }
    }
}
