package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.function.Predicate;

public class WRAvoidEntityGoal<T extends LivingEntity> extends Goal
{
    private final TameableDragonEntity entity;
    private final Class<T> classToAvoid;
    private final float avoidDistance;
    private final double farSpeed;
    private final double nearSpeed;
    private final EntityPredicate builtTargetSelector;
    private T avoidTarget;

    public WRAvoidEntityGoal(TameableDragonEntity entity, Class<T> classToAvoid, float avoidDistance, double speed)
    {
        this(entity, classToAvoid, e -> true, avoidDistance, speed, speed * 1.43);
    }

    public WRAvoidEntityGoal(TameableDragonEntity entityIn, Class<T> avoidClass, Predicate<LivingEntity> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
        this.entity = entityIn;
        this.classToAvoid = avoidClass;
        this.avoidDistance = distance;
        this.farSpeed = nearSpeedIn;
        this.nearSpeed = farSpeedIn;
        this.builtTargetSelector = new EntityPredicate().range(distance).selector(targetPredicate);

        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        if (entity.isTame()) return false;
        this.avoidTarget = entity.level.getNearestEntity(classToAvoid, builtTargetSelector, entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().inflate(avoidDistance, 3.0D, avoidDistance));
        if (avoidTarget == null) return false;
        Vector3d pos = RandomPositionGenerator.getPosAvoid(entity, 16, 7, avoidTarget.position());
        if (pos == null) return false;
        if (avoidTarget.position().distanceToSqr(pos) < avoidTarget.distanceToSqr(entity)) return false;
        return entity.getNavigation().moveTo(pos.x(), pos.y(), pos.z(), farSpeed);
    }

    @Override
    public boolean canContinueToUse()
    {
        return !entity.getNavigation().isDone();
    }


    public void tick()
    {
        if (entity.distanceToSqr(avoidTarget) < 49) entity.getNavigation().setSpeedModifier(nearSpeed);
        else entity.getNavigation().setSpeedModifier(farSpeed);
    }

    @Override
    public void stop()
    {
        avoidTarget = null;
    }
}
