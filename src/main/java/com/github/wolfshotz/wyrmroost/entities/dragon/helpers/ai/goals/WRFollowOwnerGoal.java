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
        setControls(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.dragon = tameableEntity;
    }

    @Override
    public boolean canStart()
    {
        final double MINIMUM_FOLLOW_DIST = dragon.getWidth() * dragon.getWidth() + 100;

        if (dragon.isInSittingPose() || dragon.isLeashed() || dragon.hasPositionTarget()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null || owner.isSpectator()) return false;
        return dragon.squaredDistanceTo(owner) > MINIMUM_FOLLOW_DIST;
    }

    @Override
    public boolean shouldContinue()
    {
        final double MINIMUM_TRAVEL_DIST = dragon.getWidth() * dragon.getWidth() + 6.25;

        if (dragon.isInSittingPose() || dragon.isLeashed()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null) return false;
        if (owner.isSpectator()) return false;
        return dragon.squaredDistanceTo(owner) > MINIMUM_TRAVEL_DIST;
    }

    @Override
    public void stop()
    {
        dragon.getNavigation().stop();
    }

    @Override
    public void tick()
    {
        LivingEntity owner = dragon.getOwner();
        dragon.getLookControl().lookAt(owner, 90, 90);

        if (++newPathTicks >= 10 || dragon.getNavigation().isIdle())
        {
            newPathTicks = 0;

            final double minTeleportDist = (dragon.getWidth() * 5 * dragon.getWidth() * 5 * (dragon.isFlying()? dragon.getWidth() * 5 : 1)) + 196;

            if (dragon.squaredDistanceTo(owner) > minTeleportDist && (owner.getRootVehicle().isOnGround() || dragon.canFly()) && dragon.tryTeleportToOwner())
                dragon.getNavigation().stop();
            else dragon.getNavigation().startMovingTo(owner, 1);
        }
    }
}
