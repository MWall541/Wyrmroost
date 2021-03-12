package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

/**
 * todo: I guess make our own node processor. Derivative of WalkAndSwim, just ditch all the water related shit.
 */
public class FlyerPathNavigator extends FlyingPathNavigator
{
    public FlyerPathNavigator(AbstractDragonEntity entity)
    {
        super(entity, entity.world);
    }

    @Override
    @SuppressWarnings("ConstantConditions") // IT CAN BE NULL DAMNIT
    public void tick()
    {
        if (!isIdle() && canNavigate())
        {
            AbstractDragonEntity dragon = ((AbstractDragonEntity) entity);
            BlockPos target = getTargetPos();
            if (target != null)
            {
                entity.getMoveHelper().setMoveTo(target.getX(), target.getY(), target.getZ(), speed);
                maxDistanceToWaypoint = entity.getWidth() * entity.getWidth() * dragon.getYawRotationSpeed() * dragon.getYawRotationSpeed();
                Vector3d entityPos = getEntityPosition();
                if (target.distanceSq(entityPos.x, entityPos.y, entityPos.z, true) <= maxDistanceToWaypoint)
                    currentPath = null;
            }
        }
    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos)
    {
        return true;
    }
}
