package WolfShotz.Wyrmroost.content.entities;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.Calendar;

import static org.lwjgl.opengl.GL11.GL_ONE;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity> extends MobRenderer<T, EntityModel<T>>
{
    protected static final String DEF_LOC = "textures/entity/dragon/";
    protected boolean isChristmas = false;

    public AbstractDragonRenderer(EntityRendererManager manager, EntityModel<T> model, float shadowSize) {
        super(manager, model, shadowSize);
    
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (day > 14 && day < 26)) isChristmas = true;
    }
    
    // =================
    //   Render Layers
    // =================
    
    /**
     * Abstract layer renderer to handle the generalizing
     */
    public abstract class AbstractLayerRenderer extends LayerRenderer<T, EntityModel<T>>
    {
        public AbstractLayerRenderer(IEntityRenderer<T, EntityModel<T>> entityIn) { super(entityIn); }
    
        @Override // Override to deobfuscate params
        public abstract void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
    
        @Override
        public boolean shouldCombineTextures() { return false; }
    }
    
    /**
     * Class Responsible for glow layer of dragons
     * Used mainly for eyes, but can be used for other things aswell.
     */
    public class GlowLayer extends AbstractLayerRenderer
    {
        private ResourceLocation glowLoc;
    
        public GlowLayer(IEntityRenderer entityIn, ResourceLocation glowLocation) {
            super(entityIn);
            this.glowLoc = glowLocation;
        }
    
        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            
            int i = entity.getBrightnessForRender();
            int j = i % 65536;
            int k = i / 65536;
            
            bindTexture(glowLoc);
    
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_ONE, GL_ONE);
            GlStateManager.depthMask(!entity.isInvisible());
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240, 240);
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            
            gamerenderer.setupFogColor(true);
            getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
            
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
            // setLightMap(Entity)
            func_215334_a(entity);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
        }
    }
    
    
    /**
     * Class Responsible for the sleep layer. normally consists of closed eyes
     */
    public class SleepLayer extends AbstractLayerRenderer
    {
        private ResourceLocation loc;
        
        public SleepLayer(IEntityRenderer entityIn, ResourceLocation locIn) {
            super(entityIn);
            this.loc = locIn;
        }
        
        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entity.isSleeping()) {
                bindTexture(loc);
                getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
        
        @Override
        public boolean shouldCombineTextures() { return false; }
    }
    
    
    /**
     * Class Responsible for Rendering the saddle layer
     */
    public class SaddleLayer extends AbstractLayerRenderer
    {
        private ResourceLocation saddleloc;

        public SaddleLayer(IEntityRenderer entityIn, ResourceLocation location) {
            super(entityIn);
            this.saddleloc = location;
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entity.isSaddled()) {
                bindTexture(saddleloc);
                getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }
}
