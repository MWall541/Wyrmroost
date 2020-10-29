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
            mob.rotationYawHead = clampedRotate(mob.rotationYawHead, mob.renderYawOffset, mob.getHorizontalFaceSpeed());
            mob.rotationPitch = clampedRotate(mob.rotationPitch, 0, mob.getVerticalFaceSpeed());
            return;
        }

        if (frozen)
        {
            frozen = false;
            return;
        }

        mob.rotationPitch = 0;
        if (isLooking)
        {
            this.isLooking = false;
            mob.rotationYawHead = clampedRotate(mob.rotationYawHead, getTargetYaw(), deltaLookYaw);
            mob.rotationPitch = clampedRotate(mob.rotationPitch, getTargetPitch(), deltaLookPitch);
        }
        else mob.rotationYawHead = clampedRotate(mob.rotationYawHead, mob.renderYawOffset, deltaLookYaw);

        if (!mob.getNavigator().noPath())
            mob.rotationYawHead = MathHelper.func_219800_b(mob.rotationYawHead, mob.renderYawOffset, deltaLookYaw);
    }

    protected boolean func_220680_b() { return !frozen; }

    public void freeze()
    {
        this.frozen = true;
        this.isLooking = false;
    }

    public void restore()
    {
        this.restore = true;
        this.frozen = true;
        this.isLooking = false;
    }
}
