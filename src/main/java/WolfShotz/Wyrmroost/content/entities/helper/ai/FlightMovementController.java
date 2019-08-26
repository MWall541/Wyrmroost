package WolfShotz.Wyrmroost.content.entities.helper.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
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
            double d0 = posX - mob.posX;
            double d1 = posY - mob.posY;
            double d2 = posZ - mob.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            this.mob.setNoGravity(true);

            if (d3 < (double)2.5000003E-7F) {
                mob.setMoveVertical(0.0F);
                mob.setMoveForward(0.0F);
                return;
            }

            float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            float f1;

            mob.rotationYaw = limitAngle(mob.rotationYaw, f, 10.0F);

            if (mob.onGround) f1 = (float)(speed * mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
            else f1 = (float)(speed * mob.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());

            mob.setAIMoveSpeed(f1);

            double d4 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f2 = (float)(-(MathHelper.atan2(d1, d4) * (double)(180F / (float)Math.PI)));

            mob.rotationPitch = limitAngle(mob.rotationPitch, f2, 10.0F);
            mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
        } else {
            mob.setNoGravity(false);
            mob.setMoveVertical(0.0F);
            mob.setMoveForward(0.0F);
        }
    }
}
