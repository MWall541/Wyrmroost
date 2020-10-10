package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * todo: I guess make our own node processor. Derivative of WalkAndSwim, just ditch all the water related shit.
 */
public class FlyerPathNavigator extends FlyingPathNavigator
{
    public FlyerPathNavigator(MobEntity entity)
    {
        super(entity, entity.world);
    }

    @Override
    public void tick()
    {
        if (!noPath() && canNavigate())
        {
            BlockPos target = getTargetPos();
            if (target != null) entity.getMoveHelper().setMoveTo(target.getX(), target.getY(), target.getZ(), speed);

            maxDistanceToWaypoint = entity.getWidth() * entity.getHorizontalFaceSpeed() * 3;
            Vec3d entityPos = getEntityPosition();
            if (target.distanceSq(entityPos.x, entityPos.y, entityPos.z, true) <= maxDistanceToWaypoint)
                currentPath = null;
        }

    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos) { return true; }
}
