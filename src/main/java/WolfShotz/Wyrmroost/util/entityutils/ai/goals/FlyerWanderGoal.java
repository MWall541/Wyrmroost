package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class FlyerWanderGoal extends Goal
{
    private final AbstractDragonEntity dragon;
    private final int preferFlightOffset;

    public FlyerWanderGoal(AbstractDragonEntity dragon, int preferFlightOffset)
    {
        this.dragon = dragon;
        this.preferFlightOffset = preferFlightOffset;
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute()
    {
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;

        if (dragon.isFlying())
        {
            // Were in the air, don't just sit there
            if (!dragon.getMoveHelper().isUpdating()) return true;

            MovementController moveController = dragon.getMoveHelper();
            double euclid = QuikMaths.getSpaceDistSq(dragon.posX, moveController.getX(), dragon.posY, moveController.getY(), dragon.posZ, moveController.getZ());

            return euclid < 1 || euclid > 3068d;
        }

        return dragon.getRNG().nextInt(120) == 0;
    }

    @Override
    public boolean shouldContinueExecuting() { return false; }

    @Override
    public void startExecuting()
    {
        Vec3d vec3d = getPosition();
        if (vec3d == null) return;
        dragon.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1);
    }

    public Vec3d getPosition()
    {
        if (dragon.isFlying())
        {
            Random rand = dragon.getRNG();
            double x = dragon.posX + QuikMaths.nextPseudoDouble(rand) * 20d;
            double y = dragon.posY + QuikMaths.nextPseudoDouble(rand) * 16d;
            double z = dragon.posZ + QuikMaths.nextPseudoDouble(rand) * 20d;
            return new Vec3d(x, y, z);
        }
        else return RandomPositionGenerator.findRandomTarget(dragon, 10, 7);
    }
}
