package com.github.wolfshotz.wyrmroost.client.render.entity.rooststalker;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.render.entity.AbstractDragonRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.RoostStalkerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class RoostStalkerRenderer extends AbstractDragonRenderer<RoostStalkerEntity, RoostStalkerModel>
{
    public static final ResourceLocation BODY = resource("body.png");
    public static final ResourceLocation SHINY = resource("body_spe.png");
    public static final ResourceLocation CHRISTMAS = resource("christmas.png");

    public static final ResourceLocation EYES = resource("body_glow.png");
    public static final ResourceLocation SHINY_EYES = resource("body_spe_glow.png");
    public static final ResourceLocation CHRISTMAS_EYES = resource("christmas_layer.png");

    public RoostStalkerRenderer(EntityRendererManager manager)
    {
        super(manager, new RoostStalkerModel(), 0.5f);
        addLayer(new MouthItemLayer());
        addLayer(new GlowLayer(this::getGlowTexture).addCondition(r -> !r.isSleeping()));
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(RoostStalkerEntity entity)
    {
        if (entity.getVariant() == -1) return SHINY;
        return WRConfig.DECK_THE_HALLS.get()? CHRISTMAS : BODY;
    }

    public ResourceLocation getGlowTexture(RoostStalkerEntity entity)
    {
        if (entity.getVariant() == -1) return SHINY_EYES;
        return WRConfig.DECK_THE_HALLS.get()? CHRISTMAS_EYES : EYES;
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.id(BASE_PATH + "roost_stalker/" + png);
    }

    class MouthItemLayer extends LayerRenderer<RoostStalkerEntity, RoostStalkerModel>
    {
        public MouthItemLayer()
        {
            super(RoostStalkerRenderer.this);
        }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer bufferIn, int packedLightIn, RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            ItemStack stack = stalker.getItem();

            if (!stack.isEmpty())
            {
                ms.pushPose();

                if (stalker.isSleeping())
                {
                    // just set the item on the ground
                    ms.translate(-0.4, 1.47, 0.1);
                    ms.mulPose(Vector3f.YP.rotationDegrees(135));
                }
                else
                {
                    ModelRenderer head = getModel().head;
                    ms.translate(head.x / 8, -(head.y * 2.4), head.z / 8); // translate to heads rotation point (rough estimate) to allow for the same rotations while rotating; fixes connection issues
                    ms.mulPose(Vector3f.YP.rotationDegrees(netHeadYaw)); // rotate to match head rotations
                    ms.mulPose(Vector3f.XP.rotationDegrees(headPitch));
                    ms.translate(0, stalker.isInSittingPose()? 0.11 : 0.03, -0.4); // offset
                    if (stack.getItem() instanceof TieredItem) // offsets for tools, looks way fucking better
                    {
                        ms.translate(0.1, 0, 0);
                        ms.mulPose(Vector3f.YP.rotationDegrees(45));
                    }
                }

                ms.mulPose(Vector3f.XP.rotationDegrees(90)); // flip the item

                Minecraft.getInstance().getItemInHandRenderer().renderItem(stalker, stack, ItemCameraTransforms.TransformType.GROUND, false, ms, bufferIn, packedLightIn);
                ms.popPose();
            }
        }
    }
}
