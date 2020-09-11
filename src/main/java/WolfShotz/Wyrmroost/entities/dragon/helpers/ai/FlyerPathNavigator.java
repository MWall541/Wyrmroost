package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
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

    @Override
    protected void pathFollow()
    {
        Vec3d pos = getEntityPosition();
        float width = entity.getWidth();
        Vec3d pathPos = currentPath.getCurrentPos();

        this.maxDistanceToWaypoint = ((AbstractDragonEntity) entity).isFlying()? width * 2 : width > 0.75f? width / 2f : 0.75f - width / 2f;
        if (Math.abs(entity.getPosX() - (pathPos.x + ((int) (width + 1) / 2D))) < maxDistanceToWaypoint && Math.abs(entity.getPosZ() - (pathPos.z + ((int) (entity.getWidth() + 1) / 2D))) < maxDistanceToWaypoint && Math.abs(entity.getPosY() - pathPos.y) < 1.0D)
            currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);

        checkForStuck(pos);
    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos) { return true; }
}
