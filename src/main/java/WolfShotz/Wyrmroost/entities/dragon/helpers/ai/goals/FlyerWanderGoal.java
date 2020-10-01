package WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class FlyerWanderGoal extends WaterAvoidingRandomWalkingGoal
{
    private final AbstractDragonEntity dragon;

    public FlyerWanderGoal(AbstractDragonEntity dragon, double speed, float probability)
    {
        super(dragon, speed, probability);
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        this.dragon = dragon;
    }

    public FlyerWanderGoal(AbstractDragonEntity dragon, double speed)
    {
        this(dragon, speed, 0.001f);
    }

    @Override
    public boolean shouldExecute()
    {
        if (dragon.isSitting()) return false;
        if (dragon.canPassengerSteer()) return false;
        Vec3d vec3d;
        if (dragon.isFlying() && (vec3d = getPosition()) != null)
        {
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            this.mustUpdate = false;
            return true;
        }

        return super.shouldExecute();
    }

    @Override
    public void startExecuting()
    {
        if (y > dragon.getHeight()) dragon.setFlying(true);
    }

    @Override
    public void tick()
    {
        dragon.getLookController().setLookPosition(x, y, z, 90, 90);
    }

    @Override
    public Vec3d getPosition()
    {
        Vec3d position = null;

        if (dragon.isFlying() || dragon.getRNG().nextFloat() <= probability)
        {
            if ((dragon.hasDataEntry(AbstractDragonEntity.SLEEPING) && !dragon.world.isDaytime()) || dragon.getRNG().nextFloat() <= probability)
                position = RandomPositionGenerator.getLandPos(dragon, 20, 25);
            else
            {
                Vec3d vec3d = dragon.getLookVec();
                if (!dragon.isWithinHomeDistanceCurrentPosition())
                    vec3d = new Vec3d(dragon.getHomePosition()).subtract(dragon.getPositionVec()).normalize();

                int yOffset = dragon.getAltitude() > 40? -10 : 3;
                position = RandomPositionGenerator.findAirTarget(dragon, 20, 10, vec3d, Mafs.PI * 0.75f, 5, yOffset);
            }
        }

        return position == null? super.getPosition() : position;
    }
}
