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

        maxDistanceToWaypoint = ((int) (mob.getBbHeight() + 1f)) * 0.5f;
        boolean isWithinPathPoint = xDiff < maxDistanceToWaypoint && zDiff < maxDistanceToWaypoint && yDiff < 1;

        if (isWithinPathPoint || (mob.canCutCorner(path.getNextNode().type) && isPathLongEnough(pos)))
            path.advance();

        doStuckDetection(pos);
    }

    private boolean isPathLongEnough(Vector3d entityPosition)
    {
        if (path.getNextNodeIndex() + 1 >= path.getNodeCount()) return false;

        Vector3d pathPos = Vector3d.atBottomCenterOf(path.getNextNodePos());
        if (!entityPosition.closerThan(pathPos, maxDistanceToWaypoint)) return false;

        Vector3d nextPathPos = Vector3d.atBottomCenterOf(path.getNodePos(path.getNextNodeIndex() + 1));
        Vector3d midOfNextAndCurrent = nextPathPos.subtract(pathPos);
        Vector3d midOfEntityAndCurrent = entityPosition.subtract(pathPos);
        return midOfNextAndCurrent.dot(midOfEntityAndCurrent) > 0;
    }
}
