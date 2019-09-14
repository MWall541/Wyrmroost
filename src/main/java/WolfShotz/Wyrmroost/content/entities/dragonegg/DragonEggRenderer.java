package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.content.blocks.eggblock.EggBlock;
import WolfShotz.Wyrmroost.content.blocks.eggblock.EggTileEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    private final ResourceLocation TEXTURE = ModUtils.location("textures/block/egg.png");
    private final DragonEggModel EGG_MODEL = new DragonEggModel();
    
    public DragonEggRenderer(EntityRendererManager manager) {
        super(manager);
    }
    
    @Override
    public void doRender(DragonEggEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        
        
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DragonEggEntity entity) { return TEXTURE; }
    
    public void render(DragonEggEntity entity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        
        bindTexture(TEXTURE);
    
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        
        int index = entity.getDragonType()
        switch (index) {
            case 0: {
                GlStateManager.translatef((float) x + 0.5F, (float) y + 3f, (float) z + 0.5F);
                GlStateManager.scalef(2.0F, -2.0F, -2.0F);
                break;
            }
            case 1: {
                GlStateManager.translatef((float) x + 0.5f, (float) y + 1.8f, (float) z + 0.5f);
                GlStateManager.scalef(1.2f, -1.2f, -1.2f);
                break;
            }
            case 2: {
                GlStateManager.translatef((float) x + 0.5f, (float) y + 1.05f, (float) z + 0.5f);
                GlStateManager.scalef(0.7f, -0.7f, -0.7f);
                break;
            }
            default: {
                GlStateManager.translatef((float) x + 0.5F, (float) y + 3f, (float) z + 0.5F);
                GlStateManager.scalef(2.0F, -2.0F, -2.0F);
            }
        }
        
        EGG_MODEL.renderAll();
        
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
