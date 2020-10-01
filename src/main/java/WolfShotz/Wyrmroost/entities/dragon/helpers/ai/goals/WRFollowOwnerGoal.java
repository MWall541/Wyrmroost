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
        final int MINIMUM_FOLLOW_DIST = (int) dragon.getWidth() * 57;

        if (dragon.isSitting() || dragon.getLeashed() || dragon.detachHome()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null || owner.isSpectator()) return false;
        return dragon.getDistanceSq(owner) > MINIMUM_FOLLOW_DIST;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        final int MINIMUM_TRAVEL_DIST = (int) dragon.getWidth() * 9;

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

        if (dragon.ticksExisted % 10 == 0)
        {
            final int MINIMUM_TELEPORT_DIST = (int) dragon.getWidth() * 225;

            if (dragon.getDistanceSq(owner) > MINIMUM_TELEPORT_DIST && (owner.onGround || owner.isInWater()))
                dragon.tryTeleportToOwner();
            else dragon.getNavigator().tryMoveToEntityLiving(owner, 1.1);
        }
    }
}
