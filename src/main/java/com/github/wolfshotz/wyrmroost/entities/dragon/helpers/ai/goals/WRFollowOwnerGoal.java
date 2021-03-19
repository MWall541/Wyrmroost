package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class WRFollowOwnerGoal extends Goal
{
    private final TameableDragonEntity dragon;
    private int newPathTicks = 0;

    public WRFollowOwnerGoal(TameableDragonEntity tameableEntity)
    {
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.dragon = tameableEntity;
    }

    @Override
    public boolean canUse()
    {
        final double MINIMUM_FOLLOW_DIST = dragon.getBbWidth() * dragon.getBbWidth() + 100;

        if (dragon.isInSittingPose() || dragon.isLeashed() || dragon.hasRestriction()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null || owner.isSpectator()) return false;
        return dragon.distanceToSqr(owner) > MINIMUM_FOLLOW_DIST;
    }

    @Override
    public boolean canContinueToUse()
    {
        final double MINIMUM_TRAVEL_DIST = dragon.getBbWidth() * dragon.getBbWidth() + 6.25;

        if (dragon.isInSittingPose() || dragon.isLeashed()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null) return false;
        if (owner.isSpectator()) return false;
        return dragon.distanceToSqr(owner) > MINIMUM_TRAVEL_DIST;
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
        dragon.getLookControl().setLookAt(owner, 90, 90);

        if (++newPathTicks >= 10 || dragon.getNavigation().isDone())
        {
            newPathTicks = 0;

            final double minTeleportDist = (dragon.getBbWidth() * 5 * dragon.getBbWidth() * 5 * (dragon.isFlying()? dragon.getBbWidth() * 5 : 1)) + 196;

            if (dragon.distanceToSqr(owner) > minTeleportDist && (owner.getRootVehicle().isOnGround() || dragon.canFly()) && dragon.tryTeleportToOwner())
                dragon.getNavigation().stop();
            else dragon.getNavigation().moveTo(owner, 1);
        }
    }
}
