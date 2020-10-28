package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

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
    protected void pathFollow()
    {
        Vector3d pos = getEntityPosition();
        Vector3d pathPos = Vector3d.copyCenteredHorizontally(currentPath.func_242948_g());

        double xDiff = Math.abs(pathPos.getX() - entity.getPosX());
        double yDiff = Math.abs(pathPos.getY() - entity.getPosY());
        double zDiff = Math.abs(pathPos.getZ() - entity.getPosZ());

        maxDistanceToWaypoint = ((int) (entity.getWidth() + 1)) * 0.5f;
        boolean isWithinPathPoint = xDiff < maxDistanceToWaypoint && zDiff < maxDistanceToWaypoint && yDiff < 1;

        if (isWithinPathPoint || (entity.func_233660_b_(currentPath.func_237225_h_().nodeType) && isPathLongEnough(pos)))
            currentPath.incrementPathIndex();

        checkForStuck(pos);
    }

    private boolean isPathLongEnough(Vector3d entityPosition)
    {
        if (currentPath.getCurrentPathIndex() + 1 >= currentPath.getCurrentPathLength()) return false;

        Vector3d pathPos = Vector3d.copyCenteredHorizontally(this.currentPath.func_242948_g());
        if (!entityPosition.isWithinDistanceOf(pathPos, maxDistanceToWaypoint)) return false;

        Vector3d nextPathPos = Vector3d.copyCenteredHorizontally(this.currentPath.func_242947_d(this.currentPath.getCurrentPathIndex() + 1));
        Vector3d midOfNextAndCurrent = nextPathPos.subtract(pathPos);
        Vector3d midOfEntityAndCurrent = entityPosition.subtract(pathPos);
        return midOfNextAndCurrent.dotProduct(midOfEntityAndCurrent) > 0;
    }
}
