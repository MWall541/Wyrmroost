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
        super(entity, entity.world);
    }

    @Override
    protected void continueFollowingPath()
    {
        Vector3d pos = getPos();
        Vector3d pathPos = Vector3d.ofBottomCenter(currentPath.method_31032());

        double xDiff = Math.abs(pathPos.getX() - entity.getX());
        double yDiff = Math.abs(pathPos.getY() - entity.getY());
        double zDiff = Math.abs(pathPos.getZ() - entity.getZ());

        nodeReachProximity = ((int) (entity.getWidth() + 1f)) * 0.5f;
        boolean isWithinPathPoint = xDiff < nodeReachProximity && zDiff < nodeReachProximity && yDiff < 1;

        if (isWithinPathPoint || (entity.method_29244(currentPath.method_29301().type) && isPathLongEnough(pos)))
            currentPath.next();

        checkTimeouts(pos);
    }

    private boolean isPathLongEnough(Vector3d entityPosition)
    {
        if (currentPath.getCurrentNodeIndex() + 1 >= currentPath.getLength()) return false;

        Vector3d pathPos = Vector3d.ofBottomCenter(currentPath.method_31032());
        if (!entityPosition.isInRange(pathPos, nodeReachProximity)) return false;

        Vector3d nextPathPos = Vector3d.ofBottomCenter(currentPath.method_31031(currentPath.getCurrentNodeIndex() + 1));
        Vector3d midOfNextAndCurrent = nextPathPos.subtract(pathPos);
        Vector3d midOfEntityAndCurrent = entityPosition.subtract(pathPos);
        return midOfNextAndCurrent.dotProduct(midOfEntityAndCurrent) > 0;
    }
}
