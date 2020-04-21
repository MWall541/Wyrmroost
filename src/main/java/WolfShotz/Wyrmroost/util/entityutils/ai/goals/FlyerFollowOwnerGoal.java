package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.QuikMaths;
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
        if (dragon.isSleeping() || dragon.getHomePos().isPresent()) return false;

        LivingEntity preOwner = dragon.getOwner();
        if (dragon.isSitting() || preOwner == null || preOwner.isSpectator()) return false;
        this.owner = preOwner;

        double minDistSq = (minDist * minDist);
        boolean tooClose = (dragon.getDistanceSq(preOwner) < minDistSq);

        if (dragon.isFlying())
        {
//            if (orbitOverHead) return true;
            tooClose = (dragon.getDistanceSq(preOwner.getPositionVec().add(0, maxHeight, 0)) < minDistSq);
        }
        return !tooClose;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (dragon.isSitting() || owner == null) return false;

        double maxDistSq = (maxDist * maxDist);
        if (dragon.isFlying())
        {
//            if (orbitOverHead) return true;
            return dragon.getDistanceSq(owner.getPositionVec().add(0, maxHeight, 0)) > maxDistSq;
        }
        else return !dragon.getNavigator().noPath() && dragon.getDistanceSq(owner) > maxDistSq;
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
        dragon.getLookController().setLookPositionWithEntity(owner, 10f, dragon.getVerticalFaceSpeed());
        if (dragon.isSitting()) return;
        if (--timeToRecalcPath > 0) return;
        this.timeToRecalcPath = 10;

        if (dragon.posY < owner.posY + (maxDist * 2)) dragon.setFlying(true);

        if (dragon.isFlying())
        {
            if (dragon.getDistanceSq(owner.getPositionVec().add(0, maxHeight, 0)) > (3d * (minDist * minDist)))
            {
                double xOff = QuikMaths.nextPseudoDouble(dragon.getRNG()) * 2;
                double zOff = QuikMaths.nextPseudoDouble(dragon.getRNG()) * 2;
                dragon.tryTeleportToPos(owner.getPosition().add(xOff, maxHeight, zOff));
            }
//            else if (circleOverHead)
//            {
//
//            }
            else dragon.getMoveHelper().setMoveTo(owner.posX, owner.posY + maxHeight, owner.posZ, 1);
        }
        else
        {
            if (dragon.getDistanceSq(owner) > (1.5d * (minDist * minDist)))
                dragon.tryTeleportToOwner();
            else dragon.getNavigator().tryMoveToEntityLiving(owner, 1);
        }
    }
}
