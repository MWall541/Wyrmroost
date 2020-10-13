package WolfShotz.Wyrmroost.client.model;

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
        addBox(offX, offY, offZ, width, height, depth);
    }

    public void setDefaultPose()
    {
        defaultRotationX = rotateAngleX;
        defaultRotationY = rotateAngleY;
        defaultRotationZ = rotateAngleZ;
        defaultPositionX = rotationPointX;
        defaultPositionY = rotationPointY;
        defaultPositionZ = rotationPointZ;
    }
    
    public void resetToDefaultPose()
    {
        rotateAngleX = defaultRotationX;
        rotateAngleY = defaultRotationY;
        rotateAngleZ = defaultRotationZ;
        rotationPointX = defaultPositionX;
        rotationPointY = defaultPositionY;
        rotationPointZ = defaultPositionZ;
    }

    public void walk(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        rotateAngleX += invert? -rotation : rotation;
    }

    public void swing(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        rotateAngleY += invert? -rotation : rotation;
    }

    public void flap(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount)
    {
        float rotation = MathHelper.cos(limbSwing * speed + offset) * degree * limbSwingAmount + weight * limbSwingAmount;
        rotateAngleZ += invert? -rotation : rotation;
    }

    public void bob(float speed, float degree, boolean bounce, float limbSwing, float limbSwingAmount)
    {
        rotationPointY += bounce?
                -Math.abs(MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree) :
                MathHelper.sin(limbSwing * speed) * limbSwingAmount * degree - limbSwingAmount * degree;
    }

    public void copyRotationsTo(ModelRenderer box)
    {
        box.rotateAngleX = rotateAngleX;
        box.rotateAngleY = -rotateAngleY;
        box.rotateAngleZ = -rotateAngleZ;
    }
}
