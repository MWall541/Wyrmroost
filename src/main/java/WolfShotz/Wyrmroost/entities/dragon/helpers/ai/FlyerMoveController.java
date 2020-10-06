package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class FlyerMoveController extends MovementController
{
    private final AbstractDragonEntity dragon;
    private boolean flyIfFar = true;

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

        if (action == Action.MOVE_TO)
        {
            double x = posX - dragon.getPosX();
            double y = posY - dragon.getPosY();
            double z = posZ - dragon.getPosZ();
            double distSq = x * x + y * y + z * z;
            if (y > dragon.getFlightThreshold() + 1) dragon.setFlying(true);

            if (dragon.isFlying())
            {
                if (distSq < 2.5000003E-7) dragon.setMoveForward(0f); // why move...
                else
                {
                    if (!dragon.getLookController().getIsLooking())
                        dragon.getLookController().setLookPosition(posX, posY, posZ, dragon.getHorizontalFaceSpeed() * 3, 75);

                    dragon.rotationYaw = limitAngle(dragon.rotationYaw, (float) (MathHelper.atan2(z, x) * (180f / Mafs.PI)) - 90f, dragon.getHorizontalFaceSpeed());
                    float speed = (float) this.speed * dragon.getTravelSpeed();
                    dragon.setAIMoveSpeed(speed);
                    if (y != 0) dragon.setMoveVertical(y > 0? speed : -speed);
                }
            }
            else super.tick();
            action = Action.WAIT;
        }
        else
        {
            dragon.setAIMoveSpeed(0);
            dragon.setMoveStrafing(0);
            dragon.setMoveVertical(0);
            dragon.setMoveForward(0);
        }
    }

    public void setFlyIfFar(boolean b) { this.flyIfFar = b; }
}
