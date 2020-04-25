package WolfShotz.Wyrmroost.content.entities.dragon;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import java.util.Calendar;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.lwjgl.opengl.GL11.GL_ONE;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity> extends MobRenderer<T, EntityModel<T>>
{
    public static final String DEF_LOC = "textures/entity/dragon/";
    public boolean isChristmas = false;
    
    public AbstractDragonRenderer(EntityRendererManager manager, EntityModel<T> model, float shadowSize)
    {
        super(manager, model, shadowSize);
        
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && (day > 14 && day < 26)) isChristmas = true;
    }
    
    // =================
    //   Render Layers
    // =================
    
    /**
     * Abstract layer client to handle the generalizing
     */
    public abstract class AbstractLayerRenderer extends LayerRenderer<T, EntityModel<T>>
    {
        public AbstractLayerRenderer() { super(AbstractDragonRenderer.this); }
        
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
        private Function<T, ResourceLocation> glowLocation;
        private Predicate<T> shouldRender;
        
        public GlowLayer(Function<T, ResourceLocation> glowLocation)
        {
            this(glowLocation, e -> true);
        }
        
        public GlowLayer(Function<T, ResourceLocation> glowLocation, Predicate<T> predicate)
        {
            this.glowLocation = glowLocation;
            this.shouldRender = predicate;
        }
        
        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            if (!shouldRender.test(entity)) return;
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            int i = entity.getBrightnessForRender();
            int j = i % 65536;
            int k = i / 65536;
            
            bindTexture(glowLocation.apply(entity));
            
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
     * A client layer that can only client if certain conditions are met.
     * E.G. is the dragon sleeping, saddled, etc
     */
    public class ConditionalLayer extends AbstractLayerRenderer
    {
        public Predicate<T> conditions;
        public Function<T, ResourceLocation> func;
        
        public ConditionalLayer(ResourceLocation locIn, Predicate<T> conditions)
        {
            this.func = e -> locIn;
            this.conditions = conditions;
        }
        
        public ConditionalLayer(Function<T, ResourceLocation> funcIn, Predicate<T> conditions)
        {
            this.func = funcIn;
            this.conditions = conditions;
        }
        
        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            if (!conditions.test(entity)) return;
            
            bindTexture(func.apply(entity));
            getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    /**
     * Class Responsible for the sleep layer. normally consists of closed eyes
     */
    public class SleepLayer extends ConditionalLayer
    {
        public SleepLayer(ResourceLocation locIn)
        {
            super(locIn, AbstractDragonEntity::isSleeping);
        }
    }
}