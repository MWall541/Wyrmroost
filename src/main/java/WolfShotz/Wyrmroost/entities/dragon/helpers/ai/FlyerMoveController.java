package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class FlyerMoveController extends MovementController
{
    private final AbstractDragonEntity dragon;

    public FlyerMoveController(AbstractDragonEntity mob)
    {
        super(mob);
        this.dragon = mob;
    }

    public void tick()
    {
        if (dragon.canPassengerSteer())
        {
            action = Action.WAIT;
            return;
        }

        double y = posY - dragon.getPosY();
        if (y > dragon.getFlightThreshold() + 1)
            dragon.setFlying(true);

        if (dragon.isFlying())
        {
            if (action == Action.MOVE_TO)
            {
                action = Action.WAIT;
                double x = posX - dragon.getPosX();
                double z = posZ - dragon.getPosZ();
                double distSq = x * x + y * y + z * z;
                if (distSq < 2.5000003E-7) dragon.setMoveForward(0f); // why move...
                else
                {
                    dragon.rotationYawHead = (float) Math.toDegrees(MathHelper.atan2(z, x)) - 90f;
                    dragon.rotationYaw = limitAngle(dragon.rotationYaw, dragon.rotationYawHead, dragon.getHorizontalFaceSpeed());
                    float speed = dragon.getTravelSpeed();
                    dragon.setAIMoveSpeed(speed);
                    dragon.setMoveVertical(y > 0? speed : -speed);
                }
            }
            else
            {
                dragon.setAIMoveSpeed(0);
                dragon.setMoveStrafing(0);
                dragon.setMoveVertical(0);
                dragon.setMoveForward(0);
                ((LessShitLookController) dragon.getLookController()).setShouldResetPitch(true);
            }
        }
        else super.tick();
    }
}
