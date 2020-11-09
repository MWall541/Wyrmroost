package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class WRFollowOwnerGoal extends Goal
{
    private final AbstractDragonEntity dragon;
    private int newPathTicks = 0;

    public WRFollowOwnerGoal(AbstractDragonEntity tameableEntity)
    {
        setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.dragon = tameableEntity;
    }

    @Override
    public boolean shouldExecute()
    {
        final double MINIMUM_FOLLOW_DIST = dragon.getWidth() * dragon.getWidth() + 100;

        if (dragon.isSitting() || dragon.getLeashed() || dragon.detachHome()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null || owner.isSpectator()) return false;
        return dragon.getDistanceSq(owner) > MINIMUM_FOLLOW_DIST;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        final double MINIMUM_TRAVEL_DIST = dragon.getWidth() * dragon.getWidth() + 6.25;

        if (dragon.isSitting() || dragon.getLeashed()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null) return false;
        if (owner.isSpectator()) return false;
        return dragon.getDistanceSq(owner) > MINIMUM_TRAVEL_DIST;
    }

    @Override
    public void resetTask() { dragon.getNavigator().clearPath(); }

    @Override
    public void tick()
    {
        LivingEntity owner = dragon.getOwner();
        dragon.getLookController().setLookPositionWithEntity(owner, 90, 90);

        if (++newPathTicks >= 10 || dragon.getNavigator().noPath())
        {
            newPathTicks = 0;

            final double minTeleportDist = (dragon.getWidth() * 5 * dragon.getWidth() * 5 * (dragon.isFlying()? dragon.getWidth() * 5 : 1)) + 196;

            if (dragon.getDistanceSq(owner) > minTeleportDist && (owner.getLowestRidingEntity().onGround || dragon.canFly()) && dragon.tryTeleportToOwner())
                dragon.getNavigator().clearPath();
            else dragon.getNavigator().tryMoveToEntityLiving(owner, 1);
        }
    }
}
