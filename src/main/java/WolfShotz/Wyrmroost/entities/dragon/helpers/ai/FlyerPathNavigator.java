package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.Wyrmroost;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FlyerPathNavigator extends FlyingPathNavigator
{
    public FlyerPathNavigator(MobEntity entity)
    {
        super(entity, entity.world);
    }

    protected void pathFollow()
    {
        Vec3d entityPos = getEntityPosition();
        Vec3d targetPos = currentPath.getVectorFromIndex(entity, currentPath.getCurrentPathIndex());
        if (Math.abs(entity.getPosY() - targetPos.y) <= entity.getHeight() * 0.15)
        {
            maxDistanceToWaypoint = entity.getWidth() * entity.getHorizontalFaceSpeed() * 3;
            Wyrmroost.LOG.info("Distance to Target: {}, Diameter: {}", entityPos.squareDistanceTo(targetPos), maxDistanceToWaypoint);
            if (entityPos.squareDistanceTo(targetPos) <= maxDistanceToWaypoint)
                currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);
        }

        checkForStuck(targetPos);
    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos) { return true; }
}
