package com.github.wolfshotz.wyrmroost.client.render.entity.dragon_egg;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.WRModelRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragonegg.DragonEggEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.HashMap;
import java.util.Map;

public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    public static final ResourceLocation DEFAULT_TEXTURE = Wyrmroost.rl("textures/entity/dragon/dragon_egg.png");
    public static final Model MODEL = new Model();

    private static final Map<EntityType<?>, ResourceLocation> TEXTURE_MAP = new HashMap<>();

    public DragonEggRenderer(EntityRendererManager manager) { super(manager); }

    @Override
    public void render(DragonEggEntity entity, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn)
    {
        ms.push();
        scale(entity, ms);
        ms.translate(0, -1.5, 0);
        MODEL.animate(entity, partialTicks);
        IVertexBuilder builder = buffer.getBuffer(MODEL.getLayer(getTexture(entity)));
        MODEL.render(ms, builder, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        ms.pop();

        super.render(entity, entityYaw, partialTicks, ms, buffer, packedLightIn);
    }

    @Override
    protected boolean hasLabel(DragonEggEntity p_177070_1_)
    {
        return false;
    }

    @Override
    protected void renderLabelIfPresent(DragonEggEntity p_225629_1_, ITextComponent p_225629_2_, MatrixStack p_225629_3_, IRenderTypeBuffer p_225629_4_, int p_225629_5_)
    {
        super.renderLabelIfPresent(p_225629_1_, p_225629_2_, p_225629_3_, p_225629_4_, p_225629_5_);
    }

    @Override
    public ResourceLocation getTexture(DragonEggEntity entity) { return getDragonEggTexture(entity.containedDragon); }

    public static ResourceLocation getDragonEggTexture(EntityType<?> type)
    {
        return TEXTURE_MAP.computeIfAbsent(type, t ->
        {
            ResourceLocation textureLoc = Wyrmroost.rl(String.format("textures/entity/dragon/%s/egg.png", type.getRegistryName().getPath()));
            if (Minecraft.getInstance().getResourceManager().containsResource(textureLoc)) return textureLoc;
            return DEFAULT_TEXTURE;
        });
    }

    /**
     * Render Custom egg sizes / shapes. <P>
     * If none is defined, then calculate the model size according to egg size
     */
    private void scale(DragonEggEntity entity, MatrixStack ms)
    {
        EntitySize size = entity.getSize();
        if (size != null) ms.scale(size.width * 2.95f, -(size.height * 2), -(size.width * 2.95f));
    }

    /**
     * WREggTemplate - Ukan
     * Created using Tabula 7.0.1
     */
    public static class Model extends EntityModel<DragonEggEntity>
    {
        public WRModelRenderer base;
        public ModelRenderer two;
        public ModelRenderer three;
        public ModelRenderer four;

        public Model()
        {
            super(RenderType::getEntitySolid);
            textureWidth = 64;
            textureHeight = 32;
            four = new ModelRenderer(this, 0, 19);
            four.setPivot(0.0F, -1.3F, 0.0F);
            four.addCuboid(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
            two = new ModelRenderer(this, 17, 0);
            two.setPivot(0.0F, -1.5F, 0.0F);
            two.addCuboid(-2.5F, -3.0F, -2.5F, 5, 6, 5, 0.0F);
            three = new ModelRenderer(this, 0, 9);
            three.setPivot(0.0F, -2.0F, 0.0F);
            three.addCuboid(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
            base = new WRModelRenderer(this, 0, 0);
            base.setPivot(0.0F, 22.0F, 0.0F);
            base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
            three.addChild(four);
            base.addChild(two);
            two.addChild(three);

            base.setDefaultPose();
        }

        @Override
        public void setAngles(DragonEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

        public void animate(DragonEggEntity entity, float partialTicks)
        {
            float time = entity.wiggleTime.get(partialTicks);
            base.pitch = time * entity.wiggleDirection.getOffsetX();
            base.roll = time * entity.wiggleDirection.getOffsetZ();
        }

        @Override
        public void render(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            base.render(ms, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            base.resetToDefaultPose();
        }
    }
}
