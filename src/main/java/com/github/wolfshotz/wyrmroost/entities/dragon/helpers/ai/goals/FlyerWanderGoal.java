package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class FlyerWanderGoal extends WaterAvoidingRandomWalkingGoal
{
    private final TameableDragonEntity dragon;

    public FlyerWanderGoal(TameableDragonEntity dragon, double speed, float probability)
    {
        super(dragon, speed, probability);
        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        this.dragon = dragon;
    }

    public FlyerWanderGoal(TameableDragonEntity dragon, double speed)
    {
        this(dragon, speed, 0.001f);
    }

    @Override
    public boolean canUse()
    {
        if (dragon.isInSittingPose()) return false;
        if (dragon.canBeControlledByRider()) return false;
        if (!dragon.isFlying()) return false;
        Vector3d vec3d = getPosition();
        if (vec3d != null)
        {
            this.wantedX = vec3d.x;
            this.wantedY = vec3d.y;
            this.wantedZ = vec3d.z;
            this.forceTrigger = false;
            return true;
        }

        return false;
    }

    @Override
    public Vector3d getPosition()
    {
        Vector3d position = null;

        if (dragon.isFlying() || (!dragon.isLeashed() && dragon.getRandom().nextFloat() <= probability + 0.02))
        {
            if ((dragon.hasDataParameter(TameableDragonEntity.SLEEPING) && !dragon.level.isDay()) || dragon.getRandom().nextFloat() <= probability)
                position = RandomPositionGenerator.getLandPos(dragon, 20, 25);
            else
            {
                Vector3d vec3d = dragon.getLookAngle();
                if (!dragon.isWithinRestriction())
                    vec3d = Vector3d.atLowerCornerOf(dragon.getRestrictCenter()).subtract(dragon.position()).normalize();

                int yOffset = dragon.getAltitude() > 40? 10 : 0;
                position = RandomPositionGenerator.getAboveLandPos(dragon, 50, 30, vec3d, Mafs.PI / 2, 10, yOffset);
            }
            if (position != null && position.y > dragon.getY() + dragon.getBbHeight() && !dragon.isFlying()) dragon.setFlying(true);
        }

        return position == null? super.getPosition() : position;
    }
}
