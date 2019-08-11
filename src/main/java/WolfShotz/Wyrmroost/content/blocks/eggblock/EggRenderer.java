package WolfShotz.Wyrmroost.content.blocks.eggblock;

import WolfShotz.Wyrmroost.util.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EggRenderer extends TileEntityRenderer<EggTileEntity>
{
    private final ResourceLocation TEXTURE = ModUtils.location("textures/block/egg.png");
    private final EggModel eggModel = new EggModel();
    
    @Override
    public void render(EggTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        
        bindTexture(TEXTURE);
    
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        
        int index = te.getBlockState().get(EggBlock.DRAGONTYPE);
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
    
        eggModel.renderAll();
    
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
