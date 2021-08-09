package com.github.wolfshotz.wyrmroost.client.render;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.screen.BookScreen;
import com.github.wolfshotz.wyrmroost.items.book.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class TarragonTomeRenderer extends ItemStackTileEntityRenderer
{
    private static final BookModel MODEL = new BookModel();
    private static final ResourceLocation MODEL_TEXTURE = Wyrmroost.id("textures/item/tarragon_tome_model.png");
    public static final ModelResourceLocation SPRITE_MODEL_LOCATION = new ModelResourceLocation(Wyrmroost.id("book_sprite"), "inventory");

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
        IVertexBuilder builder = ItemRenderer.getFoilBuffer(buffer, MODEL.renderType(MODEL_TEXTURE), false, stack.hasFoil());
        TarragonTomeItem.Animations animations = ModUtils.getCapability(IAnimatable.CapImpl.CAPABILITY, stack);
        float delta = ClientEvents.getClient().getFrameTime();
        float bob = ClientEvents.getPlayer().tickCount + delta;
        float flipTime = animations.flipTime.get(delta);
        boolean usingBook = ClientEvents.getClient().screen instanceof BookScreen;
        float page1 = MathHelper.clamp(MathHelper.frac(flipTime + (usingBook? 0.1f : 0.25f)) * 1.6f - 0.3f, 0, 1);
        float page2 = MathHelper.clamp(MathHelper.frac(flipTime + (usingBook? 0.9f : 0.75f)) * 1.6f - 0.3f, 0, 1);

        ms.pushPose();
        if (transforms == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transforms == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
        {
            ms.translate(1, 0, 0);
            RenderHelper.mirrorX(ms);
        }

        ms.translate(-0.0625, 0, 0);
        if (transforms.firstPerson())
        {
            if (usingBook)
            {
                ms.translate(0, 1.02, 0.6);
                ms.mulPose(Vector3f.YN.rotationDegrees(90));
            }
            else
            {
                ms.translate(0.825, 0.5, 0);
                ms.mulPose(Vector3f.YN.rotationDegrees(115));
                ms.mulPose(Vector3f.ZP.rotationDegrees(25f));
            }
        }
        else
        {
            ms.translate(0.75f, 1.25f, 1.1f + MathHelper.sin(bob * 0.1f) * 0.2f);
            ms.mulPose(Vector3f.XP.rotationDegrees(45f));
            ms.mulPose(Vector3f.YN.rotationDegrees(115));
            ms.mulPose(new Quaternion(MathHelper.sin(bob * 0.15f) * -0.05f, 0, 0, 1));
        }

        MODEL.setupAnim(bob, page1, page2, usingBook? 1.15f : 1f);
        MODEL.render(ms, builder, light, overlay, 1f, 1f, 1f, 1f);
        ms.popPose();
    }
}
