package WolfShotz.Wyrmroost.client.render;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.client.model.ModelAnimator;
import WolfShotz.Wyrmroost.client.model.WRModelRenderer;
import WolfShotz.Wyrmroost.client.render.entity.fog_wraith.FogWraithModel;
import WolfShotz.Wyrmroost.client.render.entity.fog_wraith.FogWraithRenderer;
import WolfShotz.Wyrmroost.entities.dragon.FogWraithEntity;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.entities.util.animation.CapabilityAnimationHandler;
import WolfShotz.Wyrmroost.entities.util.animation.IAnimatable;
import WolfShotz.Wyrmroost.items.FogWraithTailsItem;
import WolfShotz.Wyrmroost.registry.WREntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class FogWraithTailsStackRenderer extends ItemStackTileEntityRenderer
{
    public static final Animation TAIL_SWIPE_ANIMATION = new Animation(20);
    public static final Animation GRAPPLE_ANIMATION = new Animation(50);
    public static FogWraithModel MODEL = null;

    @Override
    @SuppressWarnings("unchecked")
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transforms, MatrixStack ms, IRenderTypeBuffer typeBuffer, int light, int overlay)
    {
        if (MODEL == null)
            MODEL = ((LivingRenderer<FogWraithEntity, FogWraithModel>) ClientEvents
                    .getClient()
                    .getRenderManager()
                    .renderers
                    .get(WREntities.FOG_WRAITH.get()))
                    .getEntityModel();

        final float partialTicks = ClientEvents.getPartialTicks();
        final float tick = ClientEvents.getPlayer().ticksExisted + partialTicks;
        float timer = FogWraithTailsItem.getCapability(stack).transition.get(partialTicks);
        final int tailUseNum = transforms == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND? 1 : 0;
        final IVertexBuilder buffer = typeBuffer.getBuffer(RenderType.getEntityCutout(FogWraithRenderer.TEXTURE));
        final IAnimatable animCap = stack.getCapability(CapabilityAnimationHandler.ANIMATABLE_CAPABILITY).orElseThrow(NullPointerException::new);
        final ModelAnimator animator = MODEL.animator;
        final WRModelRenderer[] parts = MODEL.tails[tailUseNum];

        MODEL.resetToDefaultPose();
        MODEL.animator.update(animCap, partialTicks);

        if (timer > 0)
        {
            MODEL.setTime(timer);
            MODEL.chainWave(parts, 0.1f, 0.05f, -2, tick, timer * 0.5f);
            MODEL.rotate(parts[0], 0.8f, 0.15f, 0);
            for (WRModelRenderer part : parts) MODEL.rotate(part, -0.2f, 0, 0);
        }

        if (animator.setAnimation(GRAPPLE_ANIMATION))
        {
            animator.startKeyframe(3);
            animator.rotate(parts[0], -1.6f, 0, 0);
            for (WRModelRenderer part : parts)
            {
                animator.rotate(part, 0.2f, 0, 0);
            }
            animator.endKeyframe();
            animator.setStaticKeyframe(5);
            animator.resetKeyframe(3);
        }


        for (int i = 0; i < MODEL.tails.length; i++)
        {
            if (transforms != ItemCameraTransforms.TransformType.GUI) MODEL.animateTail(i, tick, i == tailUseNum? -timer + 1: 1);
            MODEL.tails[i][0].render(ms, buffer, light, overlay);
        }
    }
}
