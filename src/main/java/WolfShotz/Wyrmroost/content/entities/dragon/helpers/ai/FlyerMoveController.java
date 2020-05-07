package WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.Vec3d;

public class FlyerMoveController extends MovementController
{
    private final AbstractDragonEntity dragon;
    private final boolean preferFlight;

    public FlyerMoveController(AbstractDragonEntity dragon, boolean preferFlight)
    {
        super(dragon);
        this.dragon = dragon;
        this.preferFlight = preferFlight;
    }

    @Override
    public void tick()
    {
        Vec3d dragonPos = dragon.getPositionVector();
        Vec3d movePos = new Vec3d(posX, posY, posZ);

        // get euclidean distance to target
        double dist = dragonPos.distanceTo(movePos);
        // minimum amount to be able to move
        double minDist = dragon.getWidth() / 2;
        if (preferFlight && dist > minDist * 3 && action == Action.MOVE_TO) dragon.setFlying(true);

        if (!dragon.isFlying())
        {
            super.tick();
            return;
        }

        // get direction vector by subtracting the current position from the
        // target position and normalizing the result
        Vec3d dir = movePos.subtract(dragonPos).normalize();

        // move towards target if it's far enough away
        if (dist > minDist)
        {
            double flySpeed = 0.1f;

            // update velocity to approach target
            dragon.setMotion(dir.scale(flySpeed).add(dragon.getMotion()));
        }
        dragon.setMotion(dragon.getMotion().scale(0.8d));

        // face entity towards target
        if (dist > 2.5E-7)
        {
            float newYaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(dir.x, dir.z));
            dragon.rotationYaw = limitAngle(dragon.rotationYaw, newYaw, dragon.getHorizontalFaceSpeed());
            dragon.setAIMoveSpeed((float) (speed * dragon.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
        }

        // apply movement
        dragon.move(MoverType.SELF, dragon.getMotion());
        action = Action.WAIT;
    }
}

