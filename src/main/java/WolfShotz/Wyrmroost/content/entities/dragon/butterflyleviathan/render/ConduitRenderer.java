package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.render;

import WolfShotz.Wyrmroost.util.MathUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConduitRenderer
{
    private static final ResourceLocation CAGE_TEXTURE = new ResourceLocation("textures/entity/conduit/cage.png");
    private static final ResourceLocation WIND_TEXTURE = new ResourceLocation("textures/entity/conduit/wind.png");
    private static final ResourceLocation VERTICAL_WIND_TEXTURE = new ResourceLocation("textures/entity/conduit/wind_vertical.png");
    private static final ResourceLocation OPEN_EYE_TEXTURE = new ResourceLocation("textures/entity/conduit/open_eye.png");
    private static final CageModel CAGE = new CageModel();
    private static final EyeModel EYE = new EyeModel();
    private static final WindModel WIND = new WindModel();

    public static void render(TextureManager textureManager, float ticks, double x, double y, double z, float partialTicks)
    {
        float f = ticks + partialTicks;
        float f2 = MathHelper.sin(f * 0.1F) / 2.0F + 0.5F;
        f2 += f2 * f2;

        // Render Cage Model
        textureManager.bindTexture(CAGE_TEXTURE);
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y + 0.3f + f2 * 0.2f, (float) z);
        GlStateManager.rotatef((f * -0.0375f) * (180 / MathUtils.PI), 0.5F, 1.0F, 0.5F);
        CAGE.render();
        GlStateManager.popMatrix();
        int j = (int) ticks / 3 % 22;
        WIND.setBoxIndex(j);
        int k = (int) ticks / 66 % 3;

        // Render Wind Model
        if (k == 1) textureManager.bindTexture(VERTICAL_WIND_TEXTURE);
        else textureManager.bindTexture(WIND_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.5d, z);
        if (k != 0) GlStateManager.rotatef(90f, 0, 0, 1f);
        WIND.render();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.5d, z);
        GlStateManager.scalef(0.875f, 0.875f, 0.875F);
        GlStateManager.rotatef(180f, 1f, 0, 0);
        GlStateManager.rotatef(180f, 0, 0, 1f);
        WIND.render();
        GlStateManager.popMatrix();
        
        // Render Eye Model
        ActiveRenderInfo activeRenderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

        textureManager.bindTexture(OPEN_EYE_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y + 0.3f + f2 * 0.2f, (float) z);
        GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        GlStateManager.rotatef(-activeRenderInfo.getYaw(), 0, 1f, 0);
        GlStateManager.rotatef(activeRenderInfo.getPitch(), 1f, 0, 0);
        GlStateManager.rotatef(180f, 0, 0, 1);
        EYE.render();
        GlStateManager.popMatrix();
    }
    
    @OnlyIn(Dist.CLIENT)
    static class CageModel extends Model
    {
        private final RendererModel cageBox;
        
        public CageModel()
        {
            this.textureWidth = 32;
            this.textureHeight = 16;
            this.cageBox = new RendererModel(this, 0, 0);
            this.cageBox.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        }
        
        public void render()
        {
            this.cageBox.render(0.0625f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    static class EyeModel extends Model
    {
        private final RendererModel eyeBox;
        
        public EyeModel()
        {
            this.textureWidth = 8;
            this.textureHeight = 8;
            this.eyeBox = new RendererModel(this, 0, 0);
            this.eyeBox.addBox(-4.0F, -4.0F, 0.0F, 8, 8, 0, 0.01F);
        }
        
        public void render()
        {
            this.eyeBox.render(0.083333336f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    static class WindModel extends Model
    {
        private final RendererModel[] windBoxes = new RendererModel[22];
        private int boxIndex;
        
        public WindModel()
        {
            this.textureWidth = 64;
            this.textureHeight = 1024;
            
            for (int i = 0; i < 22; ++i)
            {
                windBoxes[i] = new RendererModel(this, 0, 32 * i);
                windBoxes[i].addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
            }
            
        }
        
        public void setBoxIndex(int index)
        {
            this.boxIndex = index;
        }
        
        public void render()
        {
            windBoxes[boxIndex].render(0.0625f);
        }
    }
}
