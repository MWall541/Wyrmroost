package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.items.staff.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class TarragonTomeRenderer extends ItemStackTileEntityRenderer
{
    private static final BookModel MODEL = new BookModel();
    private static final ResourceLocation MODEL_TEXTURE = Wyrmroost.id("textures/item/tarragon_tome_model.png");
    public static final ModelResourceLocation SPRITE_MODEL_LOCATION = new ModelResourceLocation(Wyrmroost.id("book_sprite"), "inventory");
    public static final Animation<TarragonTomeItem.AnimationCap, BookModel> OPEN_ANIMATION = Animation.create(100, null, () -> b -> {});

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transforms, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay)
    {
        switch(transforms)
        {
            case GROUND:
            case FIXED:
            case GUI:
                renderSprite(stack, ms, buffer, transforms, light, overlay);
                break;
            case FIRST_PERSON_LEFT_HAND:
            case THIRD_PERSON_LEFT_HAND:
            default:
                renderModel(stack, ms, buffer, transforms, light, overlay);
                break;
        }
    }

    public void renderSprite(ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffer, ItemCameraTransforms.TransformType transforms, int light, int overlay)
    {
        ms.pushPose();
        ms.translate(0.5, 0.5, 0.5);
        IBakedModel model = ClientEvents.getClient().getModelManager().getModel(SPRITE_MODEL_LOCATION);
        ClientEvents.getClient().getItemRenderer().render(stack, transforms, false, ms, buffer, light, overlay, model);
        ms.popPose();
    }

    public void renderModel(ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffer, ItemCameraTransforms.TransformType transforms, int light, int overlay)
    {
        IVertexBuilder builder = buffer.getBuffer(MODEL.renderType(MODEL_TEXTURE));
        if (stack.hasFoil()) builder = VertexBuilderUtils.create(builder, buffer.getBuffer(RenderType.glint()));
        TarragonTomeItem.AnimationCap animations = ModUtils.getCapabilityInstance(IAnimatable.CapImpl.CAPABILITY, stack);
        float delta = ClientEvents.getClient().getFrameTime();
        float bob = ClientEvents.getPlayer().tickCount + delta;
        float flipTime = animations.flipTime.get(delta);
        float page1 = MathHelper.clamp(MathHelper.frac(flipTime + 0.25f) * 1.6f - 0.3f, 0, 1);
        float page2 = MathHelper.clamp(MathHelper.frac(flipTime + 0.75f) * 1.6f - 0.3f, 0, 1);
        boolean i = transforms == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;

        ms.pushPose();
        ms.translate(i? 0.25 : 0.75, 0.5 + MathHelper.sin(bob * 0.02f - 1f) * 0.02f, 0);
        ms.mulPose(Vector3f.YN.rotationDegrees(i? 70 : 115));
        ms.mulPose(Vector3f.ZP.rotationDegrees(25f));

        MODEL.setupAnim(bob, page1, page2, 1);
        MODEL.render(ms, builder, light, overlay, 1f, 1f, 1f, 1f);
        ms.popPose();
    }
}
