package WolfShotz.Wyrmroost.client.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class AdvancedRendererModel extends ModelRenderer
{
    public float textureWidth;
    public float textureHeight;
    public float defaultRotationX;
    public float defaultRotationY;
    public float defaultRotationZ;
    public float defaultOffsetX;
    public float defaultOffsetY;
    public float defaultOffsetZ;
    public float defaultPositionX;
    public float defaultPositionY;
    public float defaultPositionZ;
    public float scaleX;
    public float scaleY;
    public float scaleZ;
    public int textureOffsetX;
    public int textureOffsetY;
    public boolean scaleChildren;
    private AdvancedLivingEntityModel<?> model;
    private AdvancedRendererModel parent;

    public AdvancedRendererModel(AdvancedLivingEntityModel<?> model)
    {
        super(model);
        scaleX = 1.0F;
        scaleY = 1.0F;
        scaleZ = 1.0F;
        this.model = model;
        model.boxList.add(this);
    }

    public AdvancedRendererModel(AdvancedLivingEntityModel<?> model, int textureOffsetX, int textureOffsetY)
    {
        this(model);
        setTextureOffset(textureOffsetX, textureOffsetY);
    }
    
    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
    {
        addBox(offX, offY, offZ, width, height, depth);
    }
    
    public void setShouldScaleChildren(boolean scaleChildren)
    {
        this.scaleChildren = scaleChildren;
    }
    
    public void setScale(float scaleX, float scaleY, float scaleZ)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }
    
    public void setScaleX(float scaleX)
    {
        this.scaleX = scaleX;
    }
    
    public void setScaleY(float scaleY)
    {
        this.scaleY = scaleY;
    }
    
    public void setScaleZ(float scaleZ)
    {
        this.scaleZ = scaleZ;
    }
    
    public void updateDefaultPose()
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

    public void addChild(ModelRenderer child)
    {
        super.addChild(child);
        if (child instanceof AdvancedRendererModel) ((AdvancedRendererModel) child).setParent(this);
    }
    
    public AdvancedRendererModel getParent()
    {
        return parent;
    }
    
    public void setParent(AdvancedRendererModel parent)
    {
        this.parent = parent;
    }

    public AdvancedLivingEntityModel<?> getModel()
    {
        return model;
    }
    
    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
    {
        float movementScale = model.getMovementScale();
        float rotation = MathHelper.cos(f * speed * movementScale + offset) * degree * movementScale * f1 + weight * f1;
        return invert? -rotation : rotation;
    }
    
    public void walk(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount)
    {
        rotateAngleX += calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }
    
    public void flap(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount)
    {
        rotateAngleZ += calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
    }
    
    public void swing(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount)
    {
        rotateAngleY += calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
    }
    
    public void bob(float speed, float degree, boolean bounce, float f, float f1)
    {
        float movementScale = model.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float) (Math.sin(f * speed) * (double) f1 * (double) degree - (double) (f1 * degree));
        if (bounce)
        {
            bob = (float) (-Math.abs(Math.sin(f * speed) * (double) f1 * (double) degree));
        }

        rotationPointY += bob;
    }

    /**
     * Returns the model renderer with the new texture parameters.
     */
    public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn)
    {
        this.textureWidth = (float) textureWidthIn;
        this.textureHeight = (float) textureHeightIn;
        return super.setTextureSize(textureWidthIn, textureHeightIn);
    }

    public AdvancedRendererModel setTextureOffset(int textureOffsetX, int textureOffsetY)
    {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        return this;
    }

    public void transitionTo(AdvancedRendererModel to, float timer, float maxTime)
    {
        rotateAngleX += (to.rotateAngleX - rotateAngleX) / maxTime * timer;
        rotateAngleY += (to.rotateAngleY - rotateAngleY) / maxTime * timer;
        rotateAngleZ += (to.rotateAngleZ - rotateAngleZ) / maxTime * timer;
        rotationPointX += (to.rotationPointX - rotationPointX) / maxTime * timer;
        rotationPointY += (to.rotationPointY - rotationPointY) / maxTime * timer;
        rotationPointZ += (to.rotationPointZ - rotationPointZ) / maxTime * timer;
    }
}
