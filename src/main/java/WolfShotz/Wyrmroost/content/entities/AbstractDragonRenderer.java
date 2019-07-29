package WolfShotz.Wyrmroost.content.entities;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity> extends MobRenderer<T, EntityModel<T>>
{
    protected String DEF_LOC = "textures/entity/dragon/";

    public AbstractDragonRenderer(EntityRendererManager manager, EntityModel<T> model, float shadowSize) {
        super(manager, model, shadowSize);
    }

    protected abstract class AbstractLayerRenderer<T extends AbstractDragonEntity> extends LayerRenderer<T, EntityModel<T>>
    {
        protected AbstractLayerRenderer(IEntityRenderer<T, EntityModel<T>> entityIn) { super(entityIn); }
    }

    /**
     * Class Responsible for Rendering the saddle layer
     */
    protected class SaddleLayer<T extends AbstractDragonEntity> extends AbstractLayerRenderer<T>
    {
        private ResourceLocation saddleloc;

        public SaddleLayer(IEntityRenderer entityIn, ResourceLocation location) {
            super(entityIn);
            this.saddleloc = location;
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float p_212842_4_, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entity.isSaddled()) {
                bindTexture(saddleloc);
                getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }

        @Override
        public boolean shouldCombineTextures() { return false; }
    }
}
