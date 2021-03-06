package com.github.wolfshotz.wyrmroost.client.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class WRModelRenderer extends ModelRenderer
{
    public float defaultRotationX;
    public float defaultRotationY;
    public float defaultRotationZ;
    public float defaultPositionX;
    public float defaultPositionY;
    public float defaultPositionZ;

    public WRModelRenderer(WREntityModel<?> model)
    {
        super(model);
        model.boxList.add(this);
    }

    public WRModelRenderer(WREntityModel<?> model, int textureOffsetX, int textureOffsetY)
    {
        this(model);
        setTextureOffset(textureOffsetX, textureOffsetY);
    }

    public WRModelRenderer(Model model, int textureOffsetX, int textureOffsetY)
    {
        super(model);
        setTextureOffset(textureOffsetX, textureOffsetY);
    }

    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
    {
        addCuboid(offX, offY, offZ, width, height, depth);
    }

    public void setDefaultPose()
    {
        defaultRotationX = pitch;
        defaultRotationY = yaw;
        defaultRotationZ = roll;
        defaultPositionX = pivotX;
        defaultPositionY = pivotY;
        defaultPositionZ = pivotZ;
    }
    
    public void resetToDefaultPose()
    {
        pitch = defaultRotationX;
        yaw = defaultRotationY;
        roll = defaultRotationZ;
        pivotX = defaultPositionX;
        pivotY = defaultPositionY;
        pivotZ = defaultPositionZ;
    }

    public void walk(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        pitch += invert? -rotation : rotation;
    }

    public void swing(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        yaw += invert? -rotation : rotation;
    }

    public void flap(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        roll += invert? -rotation : rotation;
    }

    public void bob(float speed, float degree, boolean bounce, float limbSwing, float limbSwingAmount)
    {
        pivotY += bounce?
                -Math.abs(MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree) :
                MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree - limbSwingAmount * degree;
    }

    public void copyRotationsTo(ModelRenderer box)
    {
        box.pitch = pitch;
        box.yaw = -yaw;
        box.roll = -roll;
    }
}
