package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.vector.Vector3d;

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
        if (dragon.func_233684_eK_()) return false;
        if (dragon.canPassengerSteer()) return false;
        Vector3d vec3d;
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
    public Vector3d getPosition()
    {
        Vector3d position = null;

        if (dragon.isFlying() || (!dragon.getLeashed() && dragon.getRNG().nextFloat() <= probability + 0.02))
        {
            if ((dragon.hasDataParameter(AbstractDragonEntity.SLEEPING) && !dragon.world.isDaytime()) || dragon.getRNG().nextFloat() <= probability)
                position = RandomPositionGenerator.getLandPos(dragon, 20, 25);
            else
            {
                Vector3d vec3d = dragon.getLookVec();
                if (!dragon.isWithinHomeDistanceCurrentPosition())
                    vec3d = Vector3d.copy(dragon.getHomePosition()).subtract(dragon.getPositionVec()).normalize();

                int yOffset = dragon.getAltitude() > 40? 10 : 0;
                position = RandomPositionGenerator.findAirTarget(dragon, 50, 30, vec3d, Mafs.PI / 2, 10, yOffset);
            }
            if (position != null && position.y > dragon.getPosY() + dragon.getHeight() && !dragon.isFlying()) dragon.setFlying(true);
        }

        return position == null? super.getPosition() : position;
    }
}
