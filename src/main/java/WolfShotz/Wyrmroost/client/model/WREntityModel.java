package WolfShotz.Wyrmroost.client.model;

import WolfShotz.Wyrmroost.client.animation.ModelAnimator;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Function;

public abstract class WREntityModel<T extends Entity> extends EntityModel<T>
{
    public T entity;
    public float globalSpeed = 0.5f;
    private float movementScale = 1.0F;
    public final List<ModelRenderer> boxList = Lists.newArrayList();
    public float time;

    public WREntityModel() {}

    public WREntityModel(Function<ResourceLocation, RenderType> type) { super(type); }

    public void updateDefaultPose()
    {
        boxList.stream()
                .filter(WRModelRenderer.class::isInstance)
                .forEach((model) -> ((WRModelRenderer) model).updateDefaultPose());
    }
    
    public void resetToDefaultPose()
    {
        boxList.stream()
                .filter(WRModelRenderer.class::isInstance)
                .forEach((model) -> ((WRModelRenderer) model).resetToDefaultPose());
    }

    public void faceTarget(float yaw, float pitch, float rotationDivisor, WRModelRenderer... boxes)
    {
        float actualRotationDivisor = rotationDivisor * (float) boxes.length;
        float yawAmount = yaw / 57.295776F / actualRotationDivisor;
        float pitchAmount = pitch / 57.295776F / actualRotationDivisor;
        WRModelRenderer[] var8 = boxes;
        int var9 = boxes.length;

        for (int var10 = 0; var10 < var9; ++var10)
        {
            WRModelRenderer box = var8[var10];
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }

    public void chainSwing(WRModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount)
    {
        float offset = this.calculateChainOffset(rootOffset, boxes);

        for (int index = 0; index < boxes.length; ++index)
            boxes[index].rotateAngleY += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
    }

    public void chainWave(WRModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount)
    {
        float offset = this.calculateChainOffset(rootOffset, boxes);

        for (int index = 0; index < boxes.length; ++index)
            boxes[index].rotateAngleX += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);

    }

    public void chainFlap(WRModelRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount)
    {
        float offset = this.calculateChainOffset(rootOffset, boxes);

        for (int index = 0; index < boxes.length; ++index)
        {
            boxes[index].rotateAngleZ += this.calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }

    }
    
    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex)
    {
        return MathHelper.cos(swing * speed * this.movementScale + offset * (float) boxIndex) * swingAmount * degree * this.movementScale;
    }

    private float calculateChainOffset(double rootOffset, WRModelRenderer... boxes)
    {
        return (float) (rootOffset * 3.141592653589793D / (double) (2 * boxes.length));
    }

    public float getMovementScale()
    {
        return this.movementScale;
    }

    public void setMovementScale(float movementScale)
    {
        this.movementScale = movementScale;
    }

    public <M extends WREntityModel<T>> M setScale(float scale)
    {
        boxList.stream().filter(WRModelRenderer.class::isInstance).forEach(b -> ((WRModelRenderer) b).setScale(scale, scale, scale));
        return (M) this;
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

    public void bob(WRModelRenderer box, float speed, float degree, boolean bounce, float f, float f1)
    {
        box.bob(speed, degree, bounce, f, f1);
    }
    
    public float moveBox(float speed, float degree, boolean bounce, float f, float f1)
    {
        return bounce? -MathHelper.abs(MathHelper.sin(f * speed) * f1 * degree) : MathHelper.sin(f * speed) * f1 * degree - f1 * degree;
    }

    public void setRotateAngle(WRModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void rotate(ModelAnimator animator, WRModelRenderer model, float x, float y, float z)
    {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public void rotateMinus(ModelAnimator animator, WRModelRenderer model, float x, float y, float z)
    {
        animator.rotate(model, (float) Math.toRadians(x) - model.defaultRotationX, (float) Math.toRadians(y) - model.defaultRotationY, (float) Math.toRadians(z) - model.defaultRotationZ);
    }

    public void startTime(float x) { this.time = x; }

    public void move(ModelRenderer box, float x, float y, float z)
    {
        box.rotationPointX += time * x;
        box.rotationPointY += time * y;
        box.rotationPointZ += time * z;
    }

    public void rotate(ModelRenderer box, float x, float y, float z)
    {
        box.rotateAngleX += time * x;
        box.rotateAngleY += time * y;
        box.rotateAngleZ += time * z;
    }

    public void idle(float frame)
    {
    }
}
