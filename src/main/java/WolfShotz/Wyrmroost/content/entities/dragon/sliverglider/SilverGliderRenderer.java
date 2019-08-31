package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL11.GL_ONE;

public class SilverGliderRenderer extends AbstractDragonRenderer<SilverGliderEntity>
{
    private static final String LOC = DEF_LOC + "silverglider/";
    private final ResourceLocation FEMALE = ModUtils.location(LOC + "female.png");
    private final ResourceLocation GOLD = ModUtils.location(LOC + "gold.png");
    private final ResourceLocation FEMALE_GLOW = ModUtils.location(LOC + "f_glow.png");
    private final ResourceLocation SLEEP = ModUtils.location(LOC + "closed_eyes.png");
    // Male Variants:
    // Body: "male_{the variant int}.png"
    // Glow: "{the variant int}_glow.png"

    public SilverGliderRenderer(EntityRendererManager manager) {
        super(manager, new SilverGliderModel(), 1f);
        addLayer(new GliderGlowLayer(this));
        addLayer(new SleepLayer(this, SLEEP));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SilverGliderEntity glider) {
        if (glider.isGolden()) return GOLD;
        if (!glider.getGender()) return FEMALE;
        return ModUtils.location(LOC + "male_" + glider.getVariant() + ".png");
    }
    
    
    class GliderGlowLayer extends AbstractLayerRenderer
    {
        GliderGlowLayer(IEntityRenderer entityIn) { super(entityIn); }
    
        @Override
        public void render(SilverGliderEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
    
            int i = entity.getBrightnessForRender();
            int j = i % 65536;
            int k = i / 65536;
            
            bindTexture(getGlowTexture(entity));
    
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_ONE, GL_ONE);
            GlStateManager.depthMask(!entity.isInvisible());
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240, 240);
            GlStateManager.color4f(1f, 1f, 1f, 1f);
    
            gamerenderer.setupFogColor(true);
            getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
    
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
            // setLightMap(Entity): deobf
            func_215334_a(entity);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
        }
    
        @Override
        public boolean shouldCombineTextures() { return false; }
        
        private ResourceLocation getGlowTexture(SilverGliderEntity glider) {
            if (glider.isGolden() || !glider.getGender()) return FEMALE_GLOW;
            return ModUtils.location(LOC + glider.getVariant() + "_glow.png");
        }
    }
}
