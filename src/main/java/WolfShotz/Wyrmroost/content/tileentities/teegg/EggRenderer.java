package WolfShotz.Wyrmroost.content.tileentities.teegg;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class EggRenderer extends TileEntityRenderer<EggTileEntity>
{
    private final ResourceLocation TEXTURE = ModUtils.location("textures/block/egg.png");
    private final EggModel eggModel = new EggModel();
    
    @Override
    public void render(EggTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
    
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
            GlStateManager.disableCull();
        }
        else this.bindTexture(TEXTURE);
    
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
    
        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        GlStateManager.translatef(0.0F, 1.0F, 0.0F);
        float f = 0.9995F;
        GlStateManager.scalef(f, f, f);
        GlStateManager.translatef(0.0F, -1.0F, 0.0F);
        
//        wiggle(tileEntityIn);
        eggModel.renderAll();
    
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    private void wiggle(EggTileEntity teEgg) {
//        if (teEgg.hatchTimer < teEgg.origTime * 0.25 && new Random().nextInt(300) == 0);
    }
}
