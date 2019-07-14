package WolfShotz.Wyrmroost.util.animtools;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;

public class BaseRenderer extends RendererModel
{
    public float defaultPosX, defaultPosY, defaultPosZ;
    public float defaultRotX, defaultRotY, defaultRotZ;
    public float defaultOffX, defaultOffY, defaultOffZ;

    public BaseRenderer(BaseModel model, String boxname) { super(model, boxname); }

    public BaseRenderer(BaseModel model) { this(model, (String)null); }

    public BaseRenderer(BaseModel model, int texOffX, int texOffY) {
        this(model);
        this.setTextureOffset(texOffX, texOffY);
    }

    /** Store the position of a box. */
    public void storeBoxPose() {
        defaultPosX = rotationPointX;
        defaultPosY = rotationPointY;
        defaultPosZ = rotationPointZ;

        defaultRotX = rotateAngleX;
        defaultRotY = rotateAngleY;
        defaultRotZ = rotateAngleZ;

        defaultOffX = offsetX;
        defaultOffY = offsetY;
        defaultOffZ = offsetZ;
    }

    /** Restore box position */
    public void restoreBoxPose() {
        rotationPointX = defaultPosX;
        rotationPointY = defaultPosY;
        rotationPointZ = defaultPosZ;

        rotateAngleX = defaultRotX;
        rotateAngleY = defaultRotY;
        rotateAngleZ = defaultRotZ;

        offsetX = defaultOffX;
        offsetY = defaultOffY;
        offsetZ = defaultOffZ;
    }

    /** Rotates this box back and forth (rotateAngleX). */
    public void rotX(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        this.rotateAngleX += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /** Rotates this box up and down (rotateAngleZ). */
    public void rotZ(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        this.rotateAngleZ += this.calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /** Rotates this box side to side (rotateAngleY). */
    public void rotY(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        this.rotateAngleY += this.calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    /** Moves this box up and down (rotationPointY). */
    public void moveY(float speed, float degree, boolean bounce, float f, float f1) {
        float bob = (float) (Math.sin(f * speed) * f1 * degree - f1 * degree);
        if (bounce) bob = (float) -Math.abs((Math.sin(f * speed) * f1 * degree));
        this.rotationPointY += bob;
    }

    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float rotation = (MathHelper.cos(f * (speed) + offset) * (degree) * f1) + (weight * f1);
        return invert ? -rotation : rotation;
    }
}
