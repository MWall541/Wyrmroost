package com.github.wolfshotz.wyrmroost.client.model;

import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

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

    public void setDefaultPose()
    {
        for (ModelRenderer box : boxList) if (box instanceof WRModelRenderer) ((WRModelRenderer) box).setDefaultPose();
    }

    public void resetToDefaultPose()
    {
        globalSpeed = 0.5f;
        for (ModelRenderer box : boxList)
            if (box instanceof WRModelRenderer) ((WRModelRenderer) box).resetToDefaultPose();
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
        yaw = (float) Math.toRadians(yaw) / rotationDivisor;
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
    public void walk(WRModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount)
    {
        box.walk(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * Rotate Angle Z
     */
    public void flap(WRModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount)
    {
        box.flap(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /**
     * Rotate Angle Y
     */
    public void swing(WRModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount)
    {
        box.swing(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    /**
     * Bob the box up and down
     *
     * @param bounce back and forth
     */
    public void bob(WRModelRenderer box, float speed, float degree, boolean bounce, float limbSwing, float limbSwingAmount)
    {
        box.bob(speed, degree, bounce, limbSwing, limbSwingAmount);
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

    public void toDefaultPose()
    {
        for (ModelRenderer modelRenderer : boxList)
        {
            if (modelRenderer instanceof WRModelRenderer)
            {
                WRModelRenderer box = (WRModelRenderer) modelRenderer;
                box.x = Mafs.linTerp(box.x, box.defaultPositionX, time);
                box.y = Mafs.linTerp(box.y, box.defaultPositionY, time);
                box.z = Mafs.linTerp(box.z, box.defaultPositionZ, time);
                box.xRot = Mafs.linTerp(box.xRot, box.defaultRotationX, time);
                box.yRot = Mafs.linTerp(box.yRot, box.defaultRotationY, time);
                box.zRot = Mafs.linTerp(box.zRot, box.defaultRotationZ, time);
            }
        }
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

    public ModelAnimator animator()
    {
        return ModelAnimator.INSTANCE;
    }

    // first
    @Override
    @Deprecated // do not override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks)
    {
        this.entity = entity;
        this.partialTicks = partialTicks;
    }
}
