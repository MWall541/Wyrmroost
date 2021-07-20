package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.client.model.WREntityModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import java.util.function.Supplier;

/**
 * Purpose of this class is to remove the need for creating a different "renderers" for each and every different entity model.
 * Achieved through making similar methods within a "wrapper model."
 * For example, instead of getting the texture from the renderer, we instead do it in the model.
 */
public class ModelWrappedRenderer<T extends MobEntity, M extends WREntityModel<T>> extends MobRenderer<T, M>
{
    public ModelWrappedRenderer(EntityRendererManager manager, M model)
    {
        super(manager, model, 0f);

        addLayer(new LayerRenderer<T, M>(this) // rendering overlays such as glowing eyes, armors, etc.
        {
            @Override
            public void render(MatrixStack ms, IRenderTypeBuffer buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float yaw, float pitch)
            {
                model.postProcess(entity, ms, buffer, light, limbSwing, limbSwingAmount, age, yaw, pitch, partialTicks);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> IRenderFactory<T> factory(Supplier<Supplier<WREntityModel<T>>> model)
    {
        return m -> (EntityRenderer<? super T>) new ModelWrappedRenderer<>(m, (WREntityModel<MobEntity>) model.get().get());
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light)
    {
        this.shadowRadius = model.getShadowRadius(entity);
        super.render(entity, yaw, partialTicks, ms, buffer, light);
    }

    @Override
    protected void scale(T entity, MatrixStack ms, float partialTicks)
    {
        model.scale(entity, ms, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity)
    {
        return model.getTexture(entity);
    }
}
