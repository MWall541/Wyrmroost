package com.github.wolfshotz.wyrmroost.client.render.entity;

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

import java.util.HashMap;
import java.util.Map;

public class DragonEggRenderer extends EntityRenderer<DragonEggEntity>
{
    public static final ResourceLocation DEFAULT_TEXTURE = Wyrmroost.id("textures/entity/dragon/dragon_egg.png");
    public static final Model MODEL = new Model();

    private static final Map<EntityType<?>, ResourceLocation> TEXTURE_MAP = new HashMap<>();

    public DragonEggRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(DragonEggEntity entity, float entityYaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int packedLightIn)
    {
        ms.pushPose();
        scale(entity, ms);
        ms.translate(0, -1.5, 0);
        MODEL.animate(entity, partialTicks);
        IVertexBuilder builder = buffer.getBuffer(MODEL.renderType(getTextureLocation(entity)));
        MODEL.renderToBuffer(ms, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        ms.popPose();

        super.render(entity, entityYaw, partialTicks, ms, buffer, packedLightIn);
    }

    @Override
    protected boolean shouldShowName(DragonEggEntity entity)
    {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(DragonEggEntity entity)
    {
        return getDragonEggTexture(entity.containedDragon);
    }

    public static ResourceLocation getDragonEggTexture(EntityType<?> type)
    {
        return TEXTURE_MAP.computeIfAbsent(type, t ->
        {
            ResourceLocation textureLoc = Wyrmroost.id(String.format("textures/entity/dragon/%s/egg.png", type.getRegistryName().getPath()));
            if (Minecraft.getInstance().getResourceManager().hasResource(textureLoc)) return textureLoc;
            return DEFAULT_TEXTURE;
        });
    }

    /**
     * Render Custom egg sizes / shapes. <P>
     * If none is defined, then calculate the model size according to egg size
     */
    private void scale(DragonEggEntity entity, MatrixStack ms)
    {
        EntitySize size = entity.getDimensions();
        if (size != null) ms.scale(size.width * 3, -(size.height * 2), -(size.width * 3));
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
            super(RenderType::entitySolid);
            texWidth = 64;
            texHeight = 32;
            four = new ModelRenderer(this, 0, 19);
            four.setPos(0.0F, -1.3F, 0.0F);
            four.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
            two = new ModelRenderer(this, 17, 0);
            two.setPos(0.0F, -1.5F, 0.0F);
            two.addBox(-2.5F, -3.0F, -2.5F, 5, 6, 5, 0.0F);
            three = new ModelRenderer(this, 0, 9);
            three.setPos(0.0F, -2.0F, 0.0F);
            three.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
            base = new WRModelRenderer(this, 0, 0);
            base.setPos(0.0F, 22.0F, 0.0F);
            base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
            three.addChild(four);
            base.addChild(two);
            two.addChild(three);

            base.setDefaultPose();
        }

        @Override
        public void setupAnim(DragonEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
        {
        }

        public void animate(DragonEggEntity entity, float partialTicks)
        {
            float time = entity.wiggleTime.get(partialTicks);
            base.xRot = time * entity.wiggleDirection.getStepX();
            base.zRot = time * entity.wiggleDirection.getStepZ();
        }

        @Override
        public void renderToBuffer(MatrixStack ms, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            base.render(ms, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            base.reset();
        }
    }
}
