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
        super(entity, entity.level);
    }

    @Override
    @SuppressWarnings("ConstantConditions") // IT CAN BE NULL DAMNIT
    public void tick()
    {
        if (!isDone() && isAtValidPosition())
        {
            AbstractDragonEntity dragon = ((AbstractDragonEntity) entity);
            BlockPos target = getTargetPos();
            if (target != null)
            {
                entity.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), speed);
                nodeReachProximity = entity.getWidth() * entity.getWidth() * dragon.getYawRotationSpeed() * dragon.getYawRotationSpeed();
                Vector3d entityPos = getPos();
                if (target.getSquaredDistance(entityPos.x, entityPos.y, entityPos.z, true) <= nodeReachProximity)
                    currentPath = null;
            }
        }
    }

    @Override
    public boolean isValidPosition(BlockPos pos)
    {
        return true;
    }
}
