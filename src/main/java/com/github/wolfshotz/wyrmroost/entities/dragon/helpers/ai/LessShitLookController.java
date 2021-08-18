package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class LessShitLookController extends LookController
{
    private final TameableDragonEntity dragon;
    private boolean stopLooking;

    public LessShitLookController(TameableDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void tick()
    {
        super.tick();
        stopLooking = false;
    }

    @Override
    public void setLookAt(double x, double y, double z, float speed, float maxAngle)
    {
        if (!stopLooking) super.setLookAt(x, y, z, speed, maxAngle);
    }

    @Override
    protected float getXRotD()
    {
        Vector3d mouthPos = dragon.getApproximateMouthPos();
        double x = wantedX - mouthPos.x();
        double y = wantedY - mob.getEyeY();
        double z = wantedZ - mouthPos.z();
        double sqrt = MathHelper.sqrt(x * x + z * z);
        return (float) (-(MathHelper.atan2(y, sqrt) * (double)(180f / Mafs.PI)));
    }

    public void stopLooking()
    {
        this.stopLooking = true;
        this.hasWanted = false;
    }
}
