package com.github.wolfshotz.wyrmroost.client.render.entity;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WREntityModel;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractDragonRenderer<T extends AbstractDragonEntity, M extends WREntityModel<T>> extends MobRenderer<T, M>
{
    public static final String BASE_PATH = "textures/entity/dragon/";

    public AbstractDragonRenderer(EntityRendererManager manager, M model, float shadowSize)
    {
        super(manager, model, shadowSize);
    }

    @Override
    protected void preRenderCallback(T entity, MatrixStack ms, float partialTicks)
    {
        float scale = entity.getRenderScale();
        ms.scale(scale, scale, scale);
    }

    /**
     * A conditional layer that can only render if certain conditions are met.
     * E.G. is the dragon sleeping, saddled, etc
     */
    public class ConditionalLayer extends LayerRenderer<T, M>
    {
        public Predicate<T> conditions;
        public final Function<T, RenderType> type;

        public ConditionalLayer(Predicate<T> conditions, Function<T, RenderType> type)
        {
            super(AbstractDragonRenderer.this);
            this.conditions = conditions;
            this.type = type;
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            if (conditions.test(entity))
                renderLayer(ms, buffer, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }

        public void renderLayer(MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            IVertexBuilder builder = buffer.getBuffer(type.apply(entity));
            getEntityModel().render(ms, builder, packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1, 1, 1, 1);
        }

        public ConditionalLayer addCondition(Predicate<T> condition)
        {
            this.conditions = conditions.and(condition);
            return this;
        }
    }

    public class GlowLayer extends ConditionalLayer
    {
        public final Function<T, ResourceLocation> texture;

        public GlowLayer(Function<T, ResourceLocation> texture)
        {
            super(e -> true, null);
            this.texture = texture;
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            if (conditions.test(entity))
            {
                ResourceLocation rl = texture.apply(entity);
                if (rl != null)
                {
                    IVertexBuilder builder = buffer.getBuffer(RenderHelper.getAdditiveGlow(rl));
                    getEntityModel().render(ms, builder, 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                }
            }
        }
    }

    public class ArmorLayer extends LayerRenderer<T, M>
    {
        private final int slotIndex;

        public ArmorLayer(int slotIndex)
        {
            super(AbstractDragonRenderer.this);
            this.slotIndex = slotIndex;
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer type, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            if (entity.hasArmor())
            {
                IVertexBuilder builder = type.getBuffer(RenderType.getEntityCutout(getArmorTexture(entity)));
                getEntityModel().render(ms, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        }

        public ResourceLocation getArmorTexture(T entity)
        {
            String path = entity.getArmor().getItem().getRegistryName().getPath().replace("_dragon_armor", "");
            return Wyrmroost.rl(String.format("%s%s/accessories/armor_%s.png", BASE_PATH, entity.getType().getRegistryName().getPath(), path));
        }
    }
}