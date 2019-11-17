package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.util.MathUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class ButterFlyMoveController extends MovementController
{
    public ButterFlyMoveController(ButterflyLeviathanEntity blevi) {
        super(blevi);
    }
    
    public void tick() {
        if (mob.isInWater()) mob.setMotion(mob.getMotion().add(0, 0.005d, 0));
        
        if (action == MovementController.Action.MOVE_TO && !mob.getNavigator().noPath()) {
            double x = posX - mob.posX;
            double y = posY - mob.posY;
            double z = posZ - mob.posZ;
            double planeDistSq = x * x + y * y + z * z;
            if (planeDistSq < 2.5000003E-7) {
                mob.setMoveForward(0);
                return;
            }
            float f = (float)(MathHelper.atan2(z, x) * (180f / MathUtils.PI)) - 90f;
            mob.rotationYaw = limitAngle(mob.rotationYaw, f, 10f);
            mob.renderYawOffset = mob.rotationYaw;
            mob.rotationYawHead = mob.rotationYaw;
            float f1 = (float) (speed * mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
            if (mob.isInWater()) {
                mob.setAIMoveSpeed(f1 * 0.02f);
                float f2 = -((float) (MathHelper.atan2(y, (double) MathHelper.sqrt(x * x + z * z)) * (double) (180f / MathUtils.PI)));
                f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85f, 85f);
                mob.rotationPitch = limitAngle(mob.rotationPitch, f2, 5f);
                float f3 = MathHelper.cos(mob.rotationPitch * (MathUtils.PI / 180f));
                float f4 = MathHelper.sin(mob.rotationPitch * (MathUtils.PI / 180f));
                mob.moveForward = f3 * f1;
                mob.moveVertical = -f4 * f1;
            }
            else mob.setAIMoveSpeed(f1 * 0.1f);
        } else {
            mob.setAIMoveSpeed(0);
            mob.setMoveStrafing(0);
            mob.setMoveVertical(0);
            mob.setMoveForward(0);
        }
    }
}
