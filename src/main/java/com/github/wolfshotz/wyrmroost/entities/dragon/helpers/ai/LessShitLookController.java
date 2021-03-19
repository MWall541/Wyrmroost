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
            mob.yHeadRot = rotateTowards(mob.yHeadRot, mob.yBodyRot, mob.getMaxHeadXRot());
            mob.xRot = rotateTowards(mob.xRot, 0, mob.getHeadRotSpeed());
            return;
        }

        if (frozen)
        {
            frozen = false;
            return;
        }

        mob.xRot = 0;
        if (hasWanted)
        {
            this.hasWanted = false;
            mob.yHeadRot = rotateTowards(mob.yHeadRot, getYRotD(), yMaxRotSpeed);
            mob.xRot = rotateTowards(mob.xRot, getXRotD(), xMaxRotAngle);
        }
        else mob.yHeadRot = rotateTowards(mob.yHeadRot, mob.yBodyRot, yMaxRotSpeed);

        if (!mob.getNavigation().isDone())
            mob.yHeadRot = MathHelper.rotateIfNecessary(mob.yHeadRot, mob.yBodyRot, yMaxRotSpeed);
    }

    protected boolean func_220680_b()
    {
        return !frozen;
    }

    public void freeze()
    {
        this.frozen = true;
        this.hasWanted = false;
    }

    public void restore()
    {
        this.restore = true;
        this.frozen = true;
        this.hasWanted = false;
    }
}
