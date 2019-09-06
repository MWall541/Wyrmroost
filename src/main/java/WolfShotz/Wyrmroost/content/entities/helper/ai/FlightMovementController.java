package WolfShotz.Wyrmroost.content.entities.helper.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
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

    public FlightMovementController(AbstractDragonEntity entity) {
        super(entity);
        this.dragon = entity;
    }

    @Override
    public void tick() {
        // Handle Vanilla movement if not flying
        if (!dragon.isFlying()) {
            super.tick();
            mob.setNoGravity(false);
            return;
        }
    
        mob.setNoGravity(true);
        
        if (action == MovementController.Action.MOVE_TO) {
            double x = posX - mob.posX;
            double y = posY - mob.posY;
            double z = posZ - mob.posZ;
            double euclid = MathUtils.calcDistance3d(x, y, z);
            double lookAngle = (double) MathHelper.sqrt(x * x + z * z);
            float lookDir = MathUtils.toDegrees((float) MathHelper.atan2(x, z)) - 90f;
            float moveSpeed;
            float lookPitch = -MathUtils.toDegrees((float) MathHelper.atan2(y, lookAngle));

            if (euclid < (double)2.5000003E-7F) { // Too small of a move target, dont move
                mob.setMoveVertical(0.0F);
                mob.setMoveForward(0.0F);
                return;
            }

            if (mob.onGround) moveSpeed = (float)(speed * mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()); // May not be needed... unsure, so do it anyway.
            else moveSpeed = (float)(speed * mob.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());
            
            mob.setAIMoveSpeed(moveSpeed);
            mob.setMoveVertical(y > 0.1d? moveSpeed : -moveSpeed);

            mob.rotationPitch = limitAngle(mob.rotationPitch, lookPitch, 10.0F);
            mob.rotationYaw = limitAngle(mob.rotationYaw, lookDir, 10.0F);
    
            action = MovementController.Action.WAIT;
        } else {
            mob.setMoveVertical(0.0F);
            mob.setMoveForward(0.0F);
        }
    }
}
