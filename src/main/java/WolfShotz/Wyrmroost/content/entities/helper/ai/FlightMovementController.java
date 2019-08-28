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
            return;
        }

        if (this.action == MovementController.Action.MOVE_TO) {
            this.action = MovementController.Action.WAIT;
            double x = posX - mob.posX;
            double y = posY - mob.posY;
            double z = posZ - mob.posZ;
            double euclid = MathUtils.calcDistance3d(x, y, z);

            this.mob.setNoGravity(true);

            if (euclid < (double)2.5000003E-7F) { // Too small, dont move
                mob.setMoveVertical(0.0F);
                mob.setMoveForward(0.0F);
                return;
            }

            float f = (float)(MathHelper.atan2(z, x) * (double)(180F / (float)Math.PI)) - 90.0F;
            float moveSpeed;

            mob.rotationYaw = limitAngle(mob.rotationYaw, f, 10.0F);

            if (mob.onGround) moveSpeed = (float)(speed * mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
            else moveSpeed = (float)(speed * mob.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());

            mob.setAIMoveSpeed(moveSpeed);

            double d4 = (double) MathHelper.sqrt(x * x + z * z);
            float f2 = (float) (-(MathHelper.atan2(y, d4) * (double)(180F / (float)Math.PI)));

            mob.rotationPitch = limitAngle(mob.rotationPitch, f2, 10.0F);
            mob.setMoveVertical(y > 0.0D ? moveSpeed : -moveSpeed);
        } else {
            mob.setNoGravity(false);
            mob.setMoveVertical(0.0F);
            mob.setMoveForward(0.0F);
        }
    }
}
