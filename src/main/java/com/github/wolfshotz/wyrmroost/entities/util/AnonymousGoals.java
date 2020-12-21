package com.github.wolfshotz.wyrmroost.entities.util;

import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.crafting.Ingredient;

import java.util.EnumSet;

/**
 * Shared Dragon Entity goals
 * A small library of *just* tweaked vanilla goals for Dragon entities
 * Because vanilla can't do it the right way
 * <p>
 * Created by WolfShotz on 10/23/2019
 */
public class AnonymousGoals
{
    /**
     * Have the goal owner be tempted to players holding items (walk towards them)
     *
     * @param tameable         Goal Owner
     * @param speed            speed to "walk towards" at
     * @param scaredByMovement should fast movement reset this task
     * @param temptedItems     the items to temp the goal owner
     */
    public static TemptGoal nonTamedTemp6tGoal(TameableEntity tameable, double speed, boolean scaredByMovement, Ingredient temptedItems)
    {
        return new TemptGoal(tameable, speed, scaredByMovement, temptedItems)
        {
            @Override
            public boolean shouldExecute() { return !tameable.isTamed() && super.shouldExecute(); }

            @Override
            public boolean shouldContinueExecuting() { return !tameable.isTamed() && super.shouldContinueExecuting(); }
        };
    }

    /**
     * Follow parent goal but its cooler cus mutex flags
     *
     * @param animal The animal
     * @param speed  The Speed
     * @return a better follow parent goal
     */
    public static FollowParentGoal followParent(TameableEntity animal, double speed)
    {
        return new FollowParentGoal(animal, speed)
        {
            { setMutexFlags(EnumSet.of(Flag.MOVE)); }

            @Override
            public boolean shouldExecute() { return !animal.isTamed() && super.shouldExecute(); }
        };
    }

    public static HurtByTargetGoal nonTamedHurtByTarget(TameableEntity mob)
    {
        return new HurtByTargetGoal(mob)
        {
            @Override
            public boolean shouldExecute()
            {
                return !mob.isTamed() && super.shouldExecute();
            }

            @Override
            public boolean shouldContinueExecuting()
            {
                return mob.isTamed() && super.shouldContinueExecuting();
            }
        };
    }
}
