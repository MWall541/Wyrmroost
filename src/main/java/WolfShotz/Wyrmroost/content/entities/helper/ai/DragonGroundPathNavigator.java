package WolfShotz.Wyrmroost.content.entities.helper.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by WolfShotz - 8/26/19 - 16:21
 *
 * Fix issues where entities with large hitboxes would "spin" at the end of their path or ontop of certain blocks.
 */
public class DragonGroundPathNavigator extends GroundPathNavigator
{
    public DragonGroundPathNavigator(AbstractDragonEntity dragon, World world) {
        super(dragon, world);
    }
    
    @Override
    protected void pathFollow() {
        Vec3d vec3d = getEntityPosition();
        Vec3d vec3d1 = currentPath.getCurrentPos();
        double entityNavOffset = (entity.getWidth() + 1) / 2d;
        maxDistanceToWaypoint = entity.getWidth() > 0.75F ? entity.getWidth() / 2.0F : 0.75F - entity.getWidth() / 2.0F;
        if (Math.abs(entity.posX - (vec3d1.x + entityNavOffset)) < (double) maxDistanceToWaypoint && Math.abs(entity.posZ - (vec3d1.z + entityNavOffset)) < (double) maxDistanceToWaypoint && Math.abs(entity.posY - vec3d1.y) < 1.0D)
            currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);
    
        checkForStuck(vec3d);
    }
}
