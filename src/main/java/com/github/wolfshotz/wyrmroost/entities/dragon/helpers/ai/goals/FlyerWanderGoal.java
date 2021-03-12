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
        setControls(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        this.dragon = dragon;
    }

    public FlyerWanderGoal(AbstractDragonEntity dragon, double speed)
    {
        this(dragon, speed, 0.001f);
    }

    @Override
    public boolean canStart()
    {
        if (dragon.isInSittingPose()) return false;
        if (dragon.canBeControlledByRider()) return false;
        if (!dragon.isFlying()) return false;
        Vector3d vec3d = getWanderTarget();
        if (vec3d != null)
        {
            this.targetX = vec3d.x;
            this.targetY = vec3d.y;
            this.targetZ = vec3d.z;
            this.ignoringChance = false;
            return true;
        }

        return false;
    }

    @Override
    public Vector3d getWanderTarget()
    {
        Vector3d position = null;

        if (dragon.isFlying() || (!dragon.isLeashed() && dragon.getRandom().nextFloat() <= probability + 0.02))
        {
            if ((dragon.hasDataParameter(AbstractDragonEntity.SLEEPING) && !dragon.world.isDay()) || dragon.getRandom().nextFloat() <= probability)
                position = RandomPositionGenerator.findGroundTarget(dragon, 20, 25);
            else
            {
                Vector3d vec3d = dragon.getRotationVector();
                if (!dragon.isInWalkTargetRange())
                    vec3d = Vector3d.of(dragon.getPositionTarget()).subtract(dragon.getPos()).normalize();

                int yOffset = dragon.getAltitude() > 40? 10 : 0;
                position = RandomPositionGenerator.findAirTarget(dragon, 50, 30, vec3d, Mafs.PI / 2, 10, yOffset);
            }
            if (position != null && position.y > dragon.getY() + dragon.getHeight() && !dragon.isFlying()) dragon.setFlying(true);
        }

        return position == null? super.getWanderTarget() : position;
    }
}
