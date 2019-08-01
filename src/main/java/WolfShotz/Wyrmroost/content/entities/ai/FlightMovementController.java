package WolfShotz.Wyrmroost.content.entities.ai;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.entity.SharedMonsterAttributes.FLYING_SPEED;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

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
        // Just Use the vanilla Controller if not flying
        if (!dragon.isFlying()) {
            super.tick();
            return;
        }

        Vec3d dragonPos = dragon.getPositionVector();
        Vec3d movePos = new Vec3d(posX, posY, posZ);

        // get direction vector by subtracting the current position from the
        // target position and normalizing the result
        Vec3d dir = movePos.subtract(dragonPos).normalize();

        // get euclidean distance to target
        double dist = dragonPos.distanceTo(movePos);

        // move towards target if it's far enough away
        if (dist > dragon.getWidth()) {
            double flySpeed = dragon.getAttribute(FLYING_SPEED).getValue();

            // update velocity to approach target
            dragon.setMotion(new Vec3d(dir.x * flySpeed, dir.y * flySpeed, dir.z * flySpeed));

        } else {
            // just slow down and hover at current location
            Vec3d mot = dragon.getMotion();

            dragon.setMotion(mot.x * 0.8, mot.y * 0.8, mot.z * 0.8);
            dragon.getMotion().add(0, Math.sin(dragon.ticksExisted / 5) * 0.03, 0);
        }

        // face entity towards target
        if (dist > 2.5E-7) {
            float newYaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(dir.x, dir.z));
            dragon.rotationYaw = limitAngle(dragon.rotationYaw, newYaw, 5);
            mob.setAIMoveSpeed((float)(speed * mob.getAttribute(MOVEMENT_SPEED).getValue()));
        }

        System.out.println("where are we calling..");

        // apply movement
        dragon.move(MoverType.SELF, dragon.getMotion());
    }
}
