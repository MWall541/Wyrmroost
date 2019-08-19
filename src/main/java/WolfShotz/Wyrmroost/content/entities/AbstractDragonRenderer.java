package WolfShotz.Wyrmroost.content.entities;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.Calendar;

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
    
    
    /**
     * Abstract layer renderer to handle the generalizing
     */
    protected abstract class AbstractLayerRenderer<V extends AbstractDragonEntity> extends LayerRenderer<V, EntityModel<V>>
    {
        protected AbstractLayerRenderer(IEntityRenderer<V, EntityModel<V>> entityIn) { super(entityIn); }
    
        @Override // Override to deobfuscate params
        public abstract void render(V entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);
    }

    
    /**
     * Class Responsible for Rendering the saddle layer
     */
    protected class SaddleLayer extends AbstractLayerRenderer<T>
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

        @Override
        public boolean shouldCombineTextures() { return false; }
    }
}
