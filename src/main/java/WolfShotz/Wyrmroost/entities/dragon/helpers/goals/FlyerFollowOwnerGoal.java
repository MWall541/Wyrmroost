package WolfShotz.Wyrmroost.entities.dragon.helpers.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNodeType;

import java.util.EnumSet;

public class FlyerFollowOwnerGoal extends Goal
{
    private final AbstractDragonEntity dragon;
    private final double minDist, maxDist, maxHeight;
    private final boolean orbitOverHead;
    private LivingEntity owner;
    // vanilla stuff I stole
    private float oldWaterCost;
    private int timeToRecalcPath;


    public FlyerFollowOwnerGoal(AbstractDragonEntity dragon, double minDist, double maxDist, double maxHeight, boolean orbitOverHead)
    {
        this.dragon = dragon;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.maxHeight = maxHeight;
        this.orbitOverHead = orbitOverHead;

        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute()
    {
        if (dragon.isSitting()) return false;
        if (dragon.getHomePos().isPresent()) return false;

        owner = dragon.getOwner();
        if (owner == null || owner.isSpectator()) return false;

        return dragon.getDistanceSq(owner) > (minDist * minDist);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (dragon.isSitting() || owner == null) return false;
        return dragon.getDistanceSq(owner) <= (maxDist * maxDist);
    }

    @Override
    public void startExecuting()
    {
        timeToRecalcPath = 0;
        oldWaterCost = dragon.getPathPriority(PathNodeType.WATER);
        dragon.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    public void resetTask()
    {
        owner = null;
        dragon.getNavigator().clearPath();
        dragon.setPathPriority(PathNodeType.WATER, oldWaterCost);
    }

    @Override
    public void tick()
    {
        dragon.getLookController().setLookPositionWithEntity(owner, dragon.getFaceRotSpeed(), dragon.getVerticalFaceSpeed());
        if (--timeToRecalcPath > 0) return;
        this.timeToRecalcPath = 10;

        if (dragon.isFlying())
        {
            if (dragon.getDistanceSq(owner) > (1.5d * ((minDist * minDist) * 2))) dragon.tryTeleportToOwner();
            else dragon.getMoveHelper().setMoveTo(owner.getPosX(), owner.getPosYEye(), owner.getPosZ(), 1);
        }
        else
        {
            if (dragon.getDistanceSq(owner) > (1.5d * (minDist * minDist))) dragon.tryTeleportToOwner();
            else dragon.getNavigator().tryMoveToEntityLiving(owner, 1);
        }
    }
}
