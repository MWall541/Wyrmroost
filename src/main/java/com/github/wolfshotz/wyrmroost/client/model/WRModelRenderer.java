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
        texOffs(textureOffsetX, textureOffsetY);
    }

    public WRModelRenderer(Model model, int textureOffsetX, int textureOffsetY)
    {
        super(model);
        texOffs(textureOffsetX, textureOffsetY);
    }

    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
    {
        addBox(offX, offY, offZ, width, height, depth);
    }

    public void setDefaultPose()
    {
        defaultRotationX = xRot;
        defaultRotationY = yRot;
        defaultRotationZ = zRot;
        defaultPositionX = x;
        defaultPositionY = y;
        defaultPositionZ = z;
    }
    
    public void resetToDefaultPose()
    {
        xRot = defaultRotationX;
        yRot = defaultRotationY;
        zRot = defaultRotationZ;
        x = defaultPositionX;
        y = defaultPositionY;
        z = defaultPositionZ;
    }

    public void walk(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        xRot += invert? -rotation : rotation;
    }

    public void swing(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        yRot += invert? -rotation : rotation;
    }

    public void flap(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        zRot += invert? -rotation : rotation;
    }

    public void bob(float speed, float degree, boolean bounce, float limbSwing, float limbSwingAmount)
    {
        y += bounce?
                -Math.abs(MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree) :
                MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree - limbSwingAmount * degree;
    }

    public void copyRotationsTo(ModelRenderer box)
    {
        box.xRot = xRot;
        box.yRot = -yRot;
        box.zRot = -zRot;
    }
}
