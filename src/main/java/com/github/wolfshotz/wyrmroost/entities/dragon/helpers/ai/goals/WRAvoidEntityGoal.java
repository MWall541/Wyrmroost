package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.function.Predicate;

public class WRAvoidEntityGoal<T extends LivingEntity> extends Goal
{
    private final AbstractDragonEntity entity;
    private final Class<T> classToAvoid;
    private final float avoidDistance;
    private final double farSpeed;
    private final double nearSpeed;
    private final EntityPredicate builtTargetSelector;
    private T avoidTarget;

    public WRAvoidEntityGoal(AbstractDragonEntity entity, Class<T> classToAvoid, float avoidDistance, double speed)
    {
        this(entity, classToAvoid, e -> true, avoidDistance, speed, speed * 1.43);
    }

    public WRAvoidEntityGoal(AbstractDragonEntity entityIn, Class<T> avoidClass, Predicate<LivingEntity> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
        this.entity = entityIn;
        this.classToAvoid = avoidClass;
        this.avoidDistance = distance;
        this.farSpeed = nearSpeedIn;
        this.nearSpeed = farSpeedIn;
        this.builtTargetSelector = new EntityPredicate().setBaseMaxDistance(distance).setPredicate(targetPredicate);

        setControls(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canStart()
    {
        if (entity.isTamed()) return false;
        this.avoidTarget = entity.world.getClosestEntityIncludingUngeneratedChunks(classToAvoid, builtTargetSelector, entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(avoidDistance, 3.0D, avoidDistance));
        if (avoidTarget == null) return false;
        Vector3d pos = RandomPositionGenerator.findTargetAwayFrom(entity, 16, 7, avoidTarget.getPos());
        if (pos == null) return false;
        if (avoidTarget.getPos().squaredDistanceTo(pos) < avoidTarget.squaredDistanceTo(entity)) return false;
        return entity.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), farSpeed);
    }

    @Override
    public boolean shouldContinue()
    {
        return !entity.getNavigation().isIdle();
    }


    public void tick()
    {
        if (entity.squaredDistanceTo(avoidTarget) < 49) entity.getNavigation().setSpeed(nearSpeed);
        else entity.getNavigation().setSpeed(farSpeed);
    }

    @Override
    public void stop()
    {
        avoidTarget = null;
    }
}
