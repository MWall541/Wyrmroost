package WolfShotz.Wyrmroost.entities.dragon.helpers.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class FlyerWanderGoal extends Goal
{
    private final AbstractDragonEntity dragon;
    private final boolean sleepTempted;
    private Vec3d vec3d;

    public FlyerWanderGoal(AbstractDragonEntity dragon, boolean sleepTempted)
    {
        this.dragon = dragon;
        this.sleepTempted = sleepTempted;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dragon.isFlying()) return false;
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;
        if (dragon.isRiding()) return false;

        return getPosition() != null;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (!dragon.isFlying()) return false;
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;
        if (dragon.isRiding()) return false;

        return dragon.getDistanceSq(vec3d) > 1;
    }

    @Override
    public void startExecuting() { dragon.getMoveHelper().setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1); }

    @Override
    public void tick()
    {
        dragon.getLookController().setLookPosition(vec3d.x, vec3d.y, vec3d.z, 800, 10);
    }

    public Vec3d getPosition()
    {
        if (dragon.isInWater()) return RandomPositionGenerator.getLandPos(dragon, 20, 20);

        Random rand = dragon.getRNG();
        double x = dragon.getPosX() + Mafs.nextDouble(rand) * 20d;
        double y = dragon.getPosY() + Mafs.nextDouble(rand) * 2;
        double z = dragon.getPosZ() + Mafs.nextDouble(rand) * 20d;
        if (!dragon.isFlying() && y < 0) y = Math.abs(y);
        if (dragon.getAltitude() > 30) y -= 30;
        if (sleepTempted && !dragon.world.isDaytime()) y = Math.max(-Math.abs(y), 0);
        return vec3d = new Vec3d(x, y, z);
    }
}
