package WolfShotz.Wyrmroost.util.entityhelpers.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

/**
 * Created by WolfShotz 7/31/19 - 19:20
 *
 * Class Responsible for handling the movement of dragons while flying
 * If The Dragon is on the ground however, just use the vanilla movement controller.
 */
public class FlightMovementController extends MovementController
{
    private AbstractDragonEntity dragon;
    private int courseCooldown;

    public FlightMovementController(AbstractDragonEntity entity) {
        super(entity);
        this.dragon = entity;
    }

    @Override
    public void tick() {
        // Handle Vanilla movement if not flying
        if (!dragon.isFlying()) {
            super.tick();
            return;
        }
        
        if (action == Action.MOVE_TO) {
            double x = posX - mob.posX;
            double y = posY - mob.posY;
            double z = posZ - mob.posZ;
            double euclid = MathHelper.sqrt(MathUtils.getSpaceDistSq(mob.posX, posX, mob.posY, posY, mob.posZ, posZ));
            
            if (euclid < (double)2.5000003E-7F) { // Too small of a move target, dont move
                action = Action.WAIT;
                return;
            }
            
            if (isNotColliding(posX, posY, posZ, euclid)) {
                mob.addVelocity(x / euclid * 0.1d, y / euclid * 0.1d, z / euclid * 0.1d);
                courseCooldown = dragon.getRNG().nextInt(5) + 2;
            }
            else action = Action.WAIT;
        }
    }
    
    /**
     * Checks if entity bounding box is not colliding with terrain
     */
    private boolean isNotColliding(double x, double y, double z, double offset)
    {
        double x1 = (x - mob.posX) / offset;
        double y1 = (y - mob.posY) / offset;
        double z1 = (z - mob.posZ) / offset;
        AxisAlignedBB axisalignedbb = mob.getBoundingBox();
        
        for (int i = 1; (double) i < offset; ++i)
            if (mob.world.checkBlockCollision(axisalignedbb.offset(x1, y1, z1))) return false;
        
        return true;
    }
}
