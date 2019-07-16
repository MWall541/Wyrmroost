package WolfShotz.Wyrmroost.util.animtools;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class BaseModel<T extends Entity> extends EntityModel<T>
{
    public float globalSpeed = 0.5f;
    public float f = 0.5f;

    public void setDefaultPoses() {
        boxList.stream()
                .filter(modelRenderer -> modelRenderer instanceof BaseRenderer)
                .forEach(box -> ((BaseRenderer) box).storeBoxPose());
    }

    public void restorePoses() {
        boxList.stream()
                .filter(modelRenderer -> modelRenderer instanceof BaseRenderer)
                .forEach(box -> ((BaseRenderer) box).restoreBoxPose());
    }

    /**
     * Rotates this box back and forth (rotateAngleX). Useful for arms and legs.
     */
    public void rotX(BaseRenderer box, float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount)
        { box.rotX(speed, degree, invert, offset, weight, walk, walkAmount); }

    /**
     * Rotates this box up and down (rotateAngleZ). Useful for wing and ears.
     */
    public void rotZ(BaseRenderer box, float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount)
        { box.rotZ(speed, degree, invert, offset, weight, flap, flapAmount); }

    /**
     * Rotates this box side to side (rotateAngleY).
     */
    public void rotY(BaseRenderer box, float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount)
        { box.rotY(speed, degree, invert, offset, weight, swing, swingAmount); }

    /**
     * Moves this box up and down (rotationPointY). Useful for bodies.
     */
    public void moveY(BaseRenderer box, float speed, float degree, boolean bounce, float f, float f1)
        { box.moveY(speed, degree, bounce, f, f1); }

    /**
     * Rotates (on the Y axis) the given model parts in a chain-like manner.
     */
    public void chainRotY(BaseRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleY += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * Rotates (on the X axis) the given model parts in a chain-like manner.
     */
    public void chainRotX(BaseRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleX += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    /**
     * Rotates (on the Z axis) the given model parts in a chain-like manner.
     */
    public void chainRotZ(BaseRenderer[] boxes, float speed, float degree, double rootOffset, float swing, float swingAmount) {
        float offset = this.calculateChainOffset(rootOffset, boxes);
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].rotateAngleZ += calculateChainRotation(speed, degree, swing, swingAmount, offset, index);
        }
    }

    private float calculateChainRotation(float speed, float degree, float swing, float swingAmount, float offset, int boxIndex) {
        return MathHelper.cos(swing * (speed * 1) + offset * boxIndex) * swingAmount * (degree * 1);
    }

    private float calculateChainOffset(double rootOffset, BaseRenderer... boxes) {
        return (float) ((rootOffset * Math.PI) / (2 * boxes.length));
    }

}
