package WolfShotz.Wyrmroost.util.entityutils.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

public class AdvancedRendererModel extends RendererModel
{
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
    private int displayList;
    private boolean compiled;

    public AdvancedRendererModel(AdvancedLivingEntityModel<?> model, String name)
    {
        super(model, name);
        scaleX = 1.0F;
        scaleY = 1.0F;
        scaleZ = 1.0F;
        this.model = model;
    }

    public AdvancedRendererModel(AdvancedLivingEntityModel<?> model)
    {
        this(model, null);
    }

    public AdvancedRendererModel(AdvancedLivingEntityModel<?> model, int textureOffsetX, int textureOffsetY)
    {
        this(model);
        setTextureOffset(textureOffsetX, textureOffsetY);
    }
    
    public RendererModel addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth)
    {
        partName = boxName + "." + partName;
        Pair<Integer, Integer> textureOffset = model.getTextureOffset(partName);
        setTextureOffset(textureOffset.getLeft(), textureOffset.getRight());
        cubeList.add((new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F)).setBoxName(partName));
        return this;
    }
    
    public RendererModel addBox(float offX, float offY, float offZ, int width, int height, int depth)
    {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
        return this;
    }
    
    public RendererModel addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored)
    {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F, mirrored));
        return this;
    }
    
    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
    {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, scaleFactor));
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
        defaultOffsetX = offsetX;
        defaultOffsetY = offsetY;
        defaultOffsetZ = offsetZ;
        defaultPositionX = rotationPointX;
        defaultPositionY = rotationPointY;
        defaultPositionZ = rotationPointZ;
    }
    
    public void resetToDefaultPose()
    {
        rotateAngleX = defaultRotationX;
        rotateAngleY = defaultRotationY;
        rotateAngleZ = defaultRotationZ;
        offsetX = defaultOffsetX;
        offsetY = defaultOffsetY;
        offsetZ = defaultOffsetZ;
        rotationPointX = defaultPositionX;
        rotationPointY = defaultPositionY;
        rotationPointZ = defaultPositionZ;
    }
    
    public void addChild(RendererModel child)
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
    
    public void parentedPostRender(float scale)
    {
        if (parent != null) parent.parentedPostRender(scale);
        
        postRender(scale);
    }
    
    public void renderWithParents(float scale)
    {
        if (parent != null) parent.renderWithParents(scale);
        
        render(scale);
    }
    
    public void render(float scale)
    {
        if (!isHidden && showModel)
        {
            GlStateManager.pushMatrix();
            if (!compiled) compileDisplayList(scale);
            
            GlStateManager.translatef(offsetX, offsetY, offsetZ);
            GlStateManager.translatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
            if (rotateAngleZ != 0.0F)
                GlStateManager.rotatef((float) Math.toDegrees(rotateAngleZ), 0.0F, 0.0F, 1.0F);
            
            if (rotateAngleY != 0.0F)
                GlStateManager.rotatef((float) Math.toDegrees(rotateAngleY), 0.0F, 1.0F, 0.0F);
            
            if (rotateAngleX != 0.0F)
                GlStateManager.rotatef((float) Math.toDegrees(rotateAngleX), 1.0F, 0.0F, 0.0F);
            
            if (scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F)
                GlStateManager.scalef(scaleX, scaleY, scaleZ);
            
            GlStateManager.callList(displayList);
            if (!scaleChildren && (scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F))
            {
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.translatef(offsetX, offsetY, offsetZ);
                GlStateManager.translatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
                if (rotateAngleZ != 0.0F)
                    GlStateManager.rotatef((float) Math.toDegrees(rotateAngleZ), 0.0F, 0.0F, 1.0F);
                
                if (rotateAngleY != 0.0F)
                    GlStateManager.rotatef((float) Math.toDegrees(rotateAngleY), 0.0F, 1.0F, 0.0F);
                
                if (rotateAngleX != 0.0F)
                    GlStateManager.rotatef((float) Math.toDegrees(rotateAngleX), 1.0F, 0.0F, 0.0F);
            }
            
            if (childModels != null)
            {
                for (RendererModel childModel : childModels) childModel.render(scale);
            }
            
            GlStateManager.popMatrix();
        }
        
    }
    
    private void compileDisplayList(float scale)
    {
        displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.newList(displayList, 4864);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        
        for (ModelBox box : cubeList) box.render(buffer, scale);
        
        GlStateManager.endList();
        compiled = true;
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
        offsetX += (to.offsetX - offsetX) / maxTime * timer;
        offsetY += (to.offsetY - offsetY) / maxTime * timer;
        offsetZ += (to.offsetZ - offsetZ) / maxTime * timer;
    }
}
