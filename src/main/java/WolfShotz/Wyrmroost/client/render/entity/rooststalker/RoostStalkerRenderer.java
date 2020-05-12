package WolfShotz.Wyrmroost.client.render.entity.rooststalker;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.RoostStalkerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RoostStalkerRenderer extends AbstractDragonRenderer<RoostStalkerEntity, RoostStalkerModel>
{
    public static final ResourceLocation BODY = resource("body.png");
    public static final ResourceLocation BODY_SPE = resource("body_spe.png");
    public static final ResourceLocation BODY_XMAS = resource("body_christmas.png");
    public static final ResourceLocation BODY_GLOW = resource("body_glow.png");
    public static final ResourceLocation BODY_SPE_GLOW = resource("body_spe_glow.png");
    public static final ResourceLocation SLEEP = resource("sleep.png");

    public RoostStalkerRenderer(EntityRendererManager manager)
    {
        super(manager, new RoostStalkerModel(), 0.5f);
        addLayer(new MouthItemLayer());
        addLayer(new GlowLayer(stalker -> stalker.isSpecial()? BODY_SPE_GLOW : BODY_GLOW));
        addLayer(new ConditionalLayer(AbstractDragonEntity::isSleeping, d -> RenderType.getEntityCutoutNoCull(SLEEP)));
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(RoostStalkerEntity entity)
    {
        if (isChristmas) return BODY_XMAS;
        return entity.isSpecial()? BODY_SPE : BODY;
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(BASE_PATH + "rooststalker/" + png);
    }

    class MouthItemLayer extends LayerRenderer<RoostStalkerEntity, RoostStalkerModel>
    {
        public MouthItemLayer() { super(RoostStalkerRenderer.this); }

        @Override
        public void render(MatrixStack ms, IRenderTypeBuffer bufferIn, int packedLightIn, RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
        {
            ItemStack stack = stalker.getItem();

            if (!stack.isEmpty())
            {
                float i = stalker.isChild()? 1f : 0;

                ms.push();

                ms.rotate(Vector3f.YP.rotationDegrees(netHeadYaw / 3f));
                ms.rotate(Vector3f.XP.rotationDegrees(90));

                if (stack.getItem() instanceof BlockItem)
                {
                    ms.scale(0.5f, 0.5f, 0.5f);
                    ms.translate(0, -0.8, -1.3);
                }

                if (stalker.isSleeping() && stalker.getAnimation() != RoostStalkerEntity.WAKE_ANIMATION)
                {
                    ms.translate(-0.5f - (i * 2.8f), -0.6f - (i * 1.8f), -1.49f);
                    ms.rotate(Vector3f.ZP.rotationDegrees(240));
                }
                else
                {
                    ms.translate(0, -0.5f - (i * -0.4f), (stalker.isSitting()? -1.3f : -1.2f) - (i * 0.135f));
                    ms.rotate(Vector3f.XP.rotationDegrees(headPitch / (1.7f - (i * -1f))));
                    ms.translate(0, -0.3f, 0);
                }
                if (stalker.isChild()) ms.scale(0.45f, 0.45f, 0.45f);

                Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(stalker, stack, ItemCameraTransforms.TransformType.GROUND, false, ms, bufferIn, packedLightIn);

                ms.pop();
            }
        }
    }
}
