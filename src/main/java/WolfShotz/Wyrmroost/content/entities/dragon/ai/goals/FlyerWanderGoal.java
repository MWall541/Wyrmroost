package WolfShotz.Wyrmroost.content.entities.dragon.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.*;
import WolfShotz.Wyrmroost.util.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.controller.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.util.math.*;

import java.util.*;

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
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;

        if (dragon.isFlying())
        {
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
        if (dragon.isFlying()) dragon.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1);
        else dragon.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1);
    }

    public Vec3d getPosition()
    {
        if (dragon.isFlying())
        {
            Random rand = dragon.getRNG();
            double x = dragon.posX + QuikMaths.nextPseudoDouble(rand) * 20d;
            double y = dragon.posY + QuikMaths.nextPseudoDouble(rand) * 16d;
            double z = dragon.posZ + QuikMaths.nextPseudoDouble(rand) * 20d;
            if (sleepTempted && !dragon.world.isDaytime()) y = Math.max(-Math.abs(y), 0);
            if (y > 175) y -= 25;
            return new Vec3d(x, y, z);
        }
        else return RandomPositionGenerator.findRandomTarget(dragon, 10, 7);
    }
}
