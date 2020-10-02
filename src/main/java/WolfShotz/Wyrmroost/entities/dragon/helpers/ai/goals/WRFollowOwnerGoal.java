package WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class WRFollowOwnerGoal extends Goal
{
    private final AbstractDragonEntity dragon;

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
        dragon.getLookController().setLookPositionWithEntity(owner, dragon.getHorizontalFaceSpeed(), dragon.getVerticalFaceSpeed());

        if (dragon.getNavigator().noPath() || dragon.ticksExisted % 15 == 0)
        {
            double minimumTeleportDist = dragon.getWidth() * dragon.getWidth() + 196;
            if (dragon.isFlying()) minimumTeleportDist *= 5;

/*            if (dragon.getDistanceSq(owner) > minimumTeleportDist && (owner.onGround || owner.isInWater() || dragon.isFlying()) && dragon.tryTeleportToOwner())
                dragon.getNavigator().clearPath();
            else */dragon.getNavigator().tryMoveToEntityLiving(owner, 1);
        }
    }
}
