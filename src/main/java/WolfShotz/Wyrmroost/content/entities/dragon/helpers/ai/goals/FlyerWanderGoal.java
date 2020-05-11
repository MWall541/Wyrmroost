package WolfShotz.Wyrmroost.content.entities.dragon.helpers.ai.goals;

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
    private final boolean sleepTempted;

    public FlyerWanderGoal(AbstractDragonEntity dragon, boolean sleepTempted)
    {
        this.dragon = dragon;
        this.sleepTempted = sleepTempted;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean shouldExecute()
    {
        if (dragon.isInWater()) return true;
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;
        if (sleepTempted && !dragon.world.isDaytime()) return false;

        if (dragon.isFlying())
        {
            MovementController moveController = dragon.getMoveHelper();
            double euclid = QuikMaths.getSpaceDistSq(dragon.getPosX(), moveController.getX(), dragon.getPosY(), moveController.getY(), dragon.getPosZ(), moveController.getZ());

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
        dragon.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1);
    }

    public Vec3d getPosition()
    {
        if (dragon.isInWater()) return RandomPositionGenerator.getLandPos(dragon, 20, 20);

        Random rand = dragon.getRNG();
        double x = dragon.getPosX() + QuikMaths.nextPseudoDouble(rand) * 20d;
        double y = dragon.getPosY() + QuikMaths.nextPseudoDouble(rand) * 16d;
        double z = dragon.getPosZ() + QuikMaths.nextPseudoDouble(rand) * 20d;
        if (!dragon.isFlying() && y < 0) y = Math.abs(y);
        if (dragon.getAltitude(false) > 30) y -= 30;
        if (sleepTempted && !dragon.world.isDaytime()) y = Math.max(-Math.abs(y), 0);
        return new Vec3d(x, y, z);
    }
}
