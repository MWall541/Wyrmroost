package WolfShotz.Wyrmroost.client.render.entity.rooststalker;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.render.entity.AbstractDragonRenderer;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.dragon.RoostStalkerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
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
        addLayer(new GlowLayer(stalker -> stalker.getVariant() == -1? BODY_SPE_GLOW : BODY_GLOW));
        addLayer(new ConditionalLayer(AbstractDragonEntity::isSleeping, d -> RenderType.getEntityCutoutNoCull(SLEEP)));
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(RoostStalkerEntity entity)
    {
        if (itsChristmasOMG) return BODY_XMAS;
        return entity.getVariant() == -1? BODY_SPE : BODY;
    }

    public static ResourceLocation resource(String png)
    {
        return Wyrmroost.rl(BASE_PATH + "roost_stalker/" + png);
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
                ms.push();

                if (stalker.isSleeping())
                {
                    // just set the item on the ground
                    ms.translate(-0.4, 1.47, 0.1);
                    ms.rotate(Vector3f.YP.rotationDegrees(135));
                }
                else
                {
                    ModelRenderer head = getEntityModel().head;
                    ms.translate(head.rotationPointX / 8, -(head.rotationPointY * 2.4), head.rotationPointZ / 8); // translate to heads rotation point (rough estimate) to allow for the same rotations while rotating; fixes connection issues
                    ms.rotate(Vector3f.YP.rotationDegrees(netHeadYaw)); // rotate to match head rotations
                    ms.rotate(Vector3f.XP.rotationDegrees(headPitch));
                    ms.translate(0, stalker.isSitting()? 0.11 : 0.03, -0.4); // offset
                    if (stack.getItem() instanceof TieredItem) // offsets for tools, looks way fucking better
                    {
                        ms.translate(0.1, 0, 0);
                        ms.rotate(Vector3f.YP.rotationDegrees(45));
                    }
                }

                ms.rotate(Vector3f.XP.rotationDegrees(90)); // flip the item

                Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(stalker, stack, ItemCameraTransforms.TransformType.GROUND, false, ms, bufferIn, packedLightIn);
                ms.pop();
            }
        }
    }
}
