package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

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
        
        this.bindTexture(TEXTURE);
    
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
    
        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5f, (float) z + 0.5F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
    
        eggModel.renderAll();
    
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
