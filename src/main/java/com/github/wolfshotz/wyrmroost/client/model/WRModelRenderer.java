package com.github.wolfshotz.wyrmroost.client.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

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
    
    public void reset()
    {
        xRot = defaultRotationX;
        yRot = defaultRotationY;
        zRot = defaultRotationZ;
        x = defaultPositionX;
        y = defaultPositionY;
        z = defaultPositionZ;
    }

    public void mirrorRotationsTo(ModelRenderer box)
    {
        box.xRot = xRot;
        box.yRot = -yRot;
        box.zRot = -zRot;
    }
}
