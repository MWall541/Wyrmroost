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
        this.builtTargetSelector = new EntityPredicate().setDistance(distance).setCustomPredicate(targetPredicate);

        setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute()
    {
        if (entity.isTamed()) return false;
        this.avoidTarget = entity.world.func_225318_b(classToAvoid, builtTargetSelector, entity, entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.getBoundingBox().grow(avoidDistance, 3.0D, avoidDistance));
        if (avoidTarget == null) return false;
        Vector3d pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 16, 7, avoidTarget.getPositionVec());
        if (pos == null) return false;
        if (avoidTarget.getPositionVec().squareDistanceTo(pos) < avoidTarget.getDistanceSq(entity)) return false;
        return entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), farSpeed);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !entity.getNavigator().noPath();
    }


    public void tick()
    {
        if (entity.getDistanceSq(avoidTarget) < 49) entity.getNavigator().setSpeed(nearSpeed);
        else entity.getNavigator().setSpeed(farSpeed);
    }

    @Override
    public void resetTask()
    {
        avoidTarget = null;
    }
}
