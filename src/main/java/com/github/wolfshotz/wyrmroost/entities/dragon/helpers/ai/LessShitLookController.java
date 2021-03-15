package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.util.math.MathHelper;

public class LessShitLookController extends LookController
{
    private boolean frozen;
    private boolean restore;

    public LessShitLookController(MobEntity entity)
    {
        super(entity);
    }

    public void tick()
    {
        if (restore)
        {
            this.restore = false;
            entity.headYaw = changeAngle(entity.headYaw, entity.bodyYaw, entity.getLookPitchSpeed());
            entity.pitch = changeAngle(entity.pitch, 0, entity.getLookYawSpeed());
            return;
        }

        if (frozen)
        {
            frozen = false;
            return;
        }

        entity.pitch = 0;
        if (active)
        {
            this.active = false;
            entity.headYaw = changeAngle(entity.headYaw, getTargetYaw(), yawSpeed);
            entity.pitch = changeAngle(entity.pitch, getTargetPitch(), pitchSpeed);
        }
        else entity.headYaw = changeAngle(entity.headYaw, entity.bodyYaw, yawSpeed);

        if (!entity.getNavigation().isIdle())
            entity.headYaw = MathHelper.stepAngleTowards(entity.headYaw, entity.bodyYaw, yawSpeed);
    }

    protected boolean func_220680_b() { return !frozen; }

    public void freeze()
    {
        this.frozen = true;
        this.active = false;
    }

    public void restore()
    {
        this.restore = true;
        this.frozen = true;
        this.active = false;
    }
}
