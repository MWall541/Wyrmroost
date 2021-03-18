package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Im not actually sure if this is a good solution or not... but it seems to be working a little bit....
 */
public class BetterPathNavigator extends GroundPathNavigator
{
    public BetterPathNavigator(MobEntity entity)
    {
        super(entity, entity.level);
    }

    @Override
    protected void followThePath()
    {
        Vector3d pos = getTempMobPos();
        Vector3d pathPos = Vector3d.atBottomCenterOf(path.getNextNodePos());

        double xDiff = Math.abs(pathPos.x() - mob.getX());
        double yDiff = Math.abs(pathPos.y() - mob.getY());
        double zDiff = Math.abs(pathPos.z() - mob.getZ());

        nodeReachProximity = ((int) (mob.getBbHeight() + 1f)) * 0.5f;
        boolean isWithinPathPoint = xDiff < nodeReachProximity && zDiff < nodeReachProximity && yDiff < 1;

        if (isWithinPathPoint || (mob.method_29244(path.method_29301().type) && isPathLongEnough(pos)))
            path.next();

        checkTimeouts(pos);
    }

    private boolean isPathLongEnough(Vector3d entityPosition)
    {
        if (path.getCurrentNodeIndex() + 1 >= path.getLength()) return false;

        Vector3d pathPos = Vector3d.ofBottomCenter(path.method_31032());
        if (!entityPosition.isInRange(pathPos, nodeReachProximity)) return false;

        Vector3d nextPathPos = Vector3d.ofBottomCenter(path.method_31031(path.getCurrentNodeIndex() + 1));
        Vector3d midOfNextAndCurrent = nextPathPos.subtract(pathPos);
        Vector3d midOfEntityAndCurrent = entityPosition.subtract(pathPos);
        return midOfNextAndCurrent.dotProduct(midOfEntityAndCurrent) > 0;
    }
}
