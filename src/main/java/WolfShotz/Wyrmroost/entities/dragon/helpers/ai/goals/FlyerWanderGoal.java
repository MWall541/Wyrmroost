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
    private Vec3d vec3d;

    public FlyerWanderGoal(AbstractDragonEntity dragon, double speed)
    {
        super(dragon, speed);
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute()
    {
        if (dragon.isSleeping()) return false;
        if (dragon.isFlying() && getPosition() != null)
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
    public void tick()
    {
        dragon.getLookController().setLookPosition(vec3d.x, vec3d.y, vec3d.z, 90, 90);
    }

    public Vec3d getPosition()
    {
        Vec3d position = null;

        if (dragon.isFlying())
        {
            if ((dragon.hasDataEntry(AbstractDragonEntity.SLEEPING) && !dragon.world.isDaytime()) || dragon.getRNG().nextFloat() >= probability)
            {
                position = RandomPositionGenerator.getLandPos(dragon, 20, 10);
            }
            else
            {
                Vec3d vec3d = dragon.getLookVec();
                if (!dragon.isWithinHomeDistanceCurrentPosition())
                    vec3d = new Vec3d(dragon.getHomePosition()).subtract(dragon.getPositionVec()).normalize();

                int yOffset = dragon.getAltitude() > 35? -10 : 3;
                position = RandomPositionGenerator.findAirTarget(dragon, 20, 10, vec3d, Mafs.PI * 0.75f, 5, yOffset);
            }
        }

        return position == null? super.getPosition() : position;
    }
}
