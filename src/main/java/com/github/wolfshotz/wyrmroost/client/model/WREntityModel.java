package com.github.wolfshotz.wyrmroost.client.model;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.render.RenderHelper;
import com.github.wolfshotz.wyrmroost.client.screen.AnimateScreen;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class WREntityModel<T extends Entity> extends EntityModel<T>
{
    public T entity;
    public float bob, partialTicks;
    public float globalSpeed = 0.5f;
    public final List<ModelRenderer> boxList = new ArrayList<>();
    public float time;

    public WREntityModel()
    {
    }

    public WREntityModel(Function<ResourceLocation, RenderType> type)
    {
        super(type);
    }

    public abstract ResourceLocation getTexture(T entity);

    public abstract float getShadowRadius(T entity);

    // first
    @Override
    @Deprecated // do not override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks)
    {
        this.entity = entity;
        this.partialTicks = partialTicks;
    }

    public void scale(T entity, MatrixStack ms, float partialTicks)
    {
    }

    public void postProcess(T entity, MatrixStack ms, IRenderTypeBuffer buffer, int light, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float partialTicks)
    {
    }

    public void setDefaultPose()
    {
        for (ModelRenderer box : boxList) if (box instanceof WRModelRenderer) ((WRModelRenderer) box).setDefaultPose();
    }

    public void reset()
    {
        globalSpeed = 0.5f;
        for (ModelRenderer box : boxList) if (box instanceof WRModelRenderer) ((WRModelRenderer) box).reset();

        if (WRConfig.DEBUG_MODE.get())
        {
            AnimateScreen screen = AnimateScreen.last;
            if (screen != null && screen.dragon == entity)
                screen.positionModel();
        }
    }

    public void setRotateAngle(ModelRenderer model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public void faceTarget(float yaw, float pitch, float rotationDivisor, ModelRenderer... boxes)
    {
        rotationDivisor *= boxes.length;
        yaw = (float) Math.toRadians(MathHelper.wrapDegrees(yaw)) / rotationDivisor;
        pitch = (float) Math.toRadians(pitch) / rotationDivisor;

        for (ModelRenderer box : boxes)
        {
            box.xRot += pitch;
            box.yRot += yaw;
        }
    }

    /**
     * Rotate Angle X
     */
    @Deprecated
    public void walk(ModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount)
    {
        float i = limbSwing(speed, degree, offset, weight, walk, walkAmount);
        box.xRot += invert? -i : i;
    }

    /**
     * Rotate Angle Z
     */
    @Deprecated
    public void flap(ModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount)
    {
        float i = limbSwing(speed, degree, offset, weight, flap, flapAmount);
        box.zRot += invert? -i : i;
    }

    /**
     * Rotate Angle Y
     */
    @Deprecated
    public void swing(ModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount)
    {
        float i = limbSwing(speed, degree, offset, weight, swing, swingAmount);
        box.yRot += invert? -i : i;
    }

    /**
     * Chain Wave (rotateAngleX)
     */
    public void chainWave(ModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount)
    {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; ++index)
            boxes[index].xRot += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
    }

    /**
     * Chain Swing (rotateAngleY)
     */
    public void chainSwing(ModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount)
    {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; ++index)
            boxes[index].yRot += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
    }

    /**
     * Chain Flap (rotateAngleZ)
     */
    public void chainFlap(ModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount)
    {
        float offset = calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; ++index)
            boxes[index].zRot += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
    }

    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex)
    {
        return MathHelper.cos(swing * speed + offset * boxIndex) * swingAmount * degree;
    }

    private float calculateChainOffset(double rootOffset, ModelRenderer... boxes)
    {
        return (float) rootOffset * Mafs.PI / (2f * boxes.length);
    }

    public void setTime(float x)
    {
        this.time = x;
    }

    public void move(ModelRenderer box, float x, float y, float z)
    {
        box.x += time * x;
        box.y += time * y;
        box.z += time * z;
    }

    public void rotate(ModelRenderer box, float x, float y, float z)
    {
        box.xRot += time * x;
        box.yRot += time * y;
        box.zRot += time * z;
    }

    public void rotateFrom0(WRModelRenderer box, float x, float y, float z)
    {
        box.xRot += (-box.defaultRotationX + x) * time;
        box.yRot += (-box.defaultRotationY + y) * time;
        box.zRot += (-box.defaultRotationZ + z) * time;
    }

    public void renderTexturedOverlay(ResourceLocation texture, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay, float red, float green, float blue, float alpha)
    {
        IVertexBuilder builder = buffer.getBuffer(renderType(texture));
        renderToBuffer(ms, builder, light, overlay, red, green, blue, alpha);
    }

    public void renderGlowOverlay(ResourceLocation texture, MatrixStack ms, IRenderTypeBuffer buffer)
    {
        IVertexBuilder builder = buffer.getBuffer(RenderHelper.getAdditiveGlow(texture));
        renderToBuffer(ms, builder, 15728640, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
    }

    public static ModelAnimator animator()
    {
        return ModelAnimator.INSTANCE;
    }

    public static float limbSwing(float speed, float degree, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        return MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
    }

    public static float bob(float speed, float degree, boolean bounce, float limbSwing, float limbSwingAmount)
    {
        float sin = MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree;
        return bounce? -Math.abs(sin) : sin - limbSwingAmount * degree;
    }

    // note: maybe add a way to add callbacks to WRModelRenderer's instead of this... (performance concerns...)
    public static void relocateTo(MatrixStack stack, ModelRenderer... boxes)
    {
        for (ModelRenderer box : boxes)
        {
            stack.translate(box.x / 16, box.y / 16, box.z / 16);
            Quaternion rotation = null;
            if (box.zRot != 0) rotation = Vector3f.ZP.rotation(box.zRot);
            if (box.yRot != 0) rotation = mul(rotation, Vector3f.YP.rotation(box.yRot));
            if (box.xRot != 0) rotation = mul(rotation, Vector3f.XP.rotation(box.xRot));
            if (rotation != null) stack.mulPose(rotation);
        }
    }

    private static Quaternion mul(@Nullable Quaternion first, Quaternion multiple)
    {
        if (first != null)
        {
            first.mul(multiple);
            return first;
        }
        else return multiple;
    }
}
