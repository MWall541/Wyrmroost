package WolfShotz.Wyrmroost.entities.util;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EntityPredicates;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Shared Dragon Entity goals
 * A small library of *just* tweaked vanilla goals for Dragon entities
 * Because vanilla can't do it the right way
 * <p>
 * Created by WolfShotz on 10/23/2019
 */
public class CommonGoalWrappers
{
    /**
     * Look at random entities. Do this while NOT sleeping
     *
     * @param entity   goal Owner
     * @param toLookat Entity Class to look at
     * @param distance distance to look at entites
     */
    public static LookAtGoal lookAt(MobEntity entity, Class<LivingEntity> toLookat, float distance)
    {
        return new LookAtGoal(entity, toLookat, distance)
        {
            @Override
            public boolean shouldExecute()
            {
                return super.shouldExecute() && !entity.getPassengers().contains(closestEntity);
            }
        };
    }

    /**
     * Simplified version of {@link #lookAt(MobEntity, float)}
     * Goal Owner will look at everything extending off of {@link LivingEntity}
     *
     * @param entity   Goal Owner
     * @param distance distance to at entities
     * @return
     */
    public static LookAtGoal lookAt(MobEntity entity, float distance)
    {
        return lookAt(entity, LivingEntity.class, distance);
    }

    /**
     * Have the goal owner be tempted to players holding items (walk towards them)
     *
     * @param dragon           Goal Owner
     * @param speed            speed to "walk towards" at
     * @param scaredByMovement should fast movement reset this task
     * @param temptedItems     the items to temp the goal owner
     */
    public static TemptGoal nonTamedTemptGoal(TameableEntity dragon, double speed, boolean scaredByMovement, Ingredient temptedItems)
    {
        return new TemptGoal(dragon, speed, scaredByMovement, temptedItems)
        {
            @Override
            public boolean shouldExecute() { return !((TameableEntity) creature).isTamed() && super.shouldExecute(); }
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
        FollowParentGoal goal = new FollowParentGoal(animal, speed)
        {
            { setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }

            @Override
            public boolean shouldExecute() { return !animal.isTamed() && super.shouldExecute(); }
        };
        return goal;
    }

    /**
     * Follow owner but for dragons (RESPECT THE HOME)
     *
     * @param dragon  if I need to explain this I swear to god
     * @param speed   speed at which it follows
     * @param minDist the distance to follow to
     * @param maxDist the distance at which to start following (if too far from this, start follow)
     * @return a WAY cooler follow owner goal.
     */
    public static FollowOwnerGoal followOwner(AbstractDragonEntity dragon, double speed, float minDist, float maxDist)
    {
        return new FollowOwnerGoal(dragon, speed, minDist, maxDist, false)
        {
            @Override
            public boolean shouldExecute()
            {
                if (dragon.isSleeping() || dragon.getHomePos().isPresent()) return false;
                return super.shouldExecute();
            }
        };
    }

    /**
     * Targets when not tamed but also control if we should as a bb
     *
     * @param tameable    same scenario as above. please don't make me shout its been a long day
     * @param targetClass the class to target
     * @param asChild     should we attack as a child?
     * @param checkSight  honestly, idk if this is even useful but self-explanatory
     * @param targets     additional target filters
     */
    public static <T extends LivingEntity> NonTamedTargetGoal<T> nonTamedTarget(TameableEntity tameable, Class<T> targetClass, boolean asChild, boolean checkSight, Predicate<LivingEntity> targets)
    {
        return new NonTamedTargetGoal<T>(tameable, targetClass, checkSight, targets)
        {
            @Override
            public boolean shouldExecute() { return (!tameable.isChild() && !asChild) && super.shouldExecute(); }
        };
    }

    /**
     * Targets when not tamed but also control if we should as a bb
     *
     * @param tameable    same scenario as above. please don't make me shout its been a long day
     * @param targetClass the class to target
     * @param asChild     should we attack as a child?
     */
    public static <T extends LivingEntity> NonTamedTargetGoal<T> nonTamedTarget(TameableEntity tameable, Class<T> targetClass, boolean asChild)
    {
        return nonTamedTarget(tameable, targetClass, asChild, true, EntityPredicates.CAN_AI_TARGET::test);
    }

    /**
     * Avoid goal but we arent scared pussies when tamed
     *
     * @param tameable  pls stop
     * @param avoiding  the type of mob were avoiding
     * @param filter    if the mob needs to be specific bcus were picky
     * @param distance  distance to check
     * @param nearSpeed walk away speed
     * @param farSpeed  OMG HE'S CLOSE RUN FASTER
     */
    public static <T extends LivingEntity> AvoidEntityGoal<T> nonTamedAvoidGoal(TameableEntity tameable, Class<T> avoiding, Predicate<LivingEntity> filter, float distance, float nearSpeed, float farSpeed)
    {
        return new AvoidEntityGoal<T>(tameable, avoiding, filter, distance, nearSpeed, farSpeed, EntityPredicates.CAN_AI_TARGET::test)
        {
            @Override
            public boolean shouldExecute() { return !tameable.isTamed() && super.shouldExecute(); }
        };
    }

    public static <T extends LivingEntity> AvoidEntityGoal<T> nonTamedAvoidGoal(TameableEntity tameable, Class<T> avoiding, float distance, float speed)
    {
        return nonTamedAvoidGoal(tameable, avoiding, e -> true, distance, speed, speed * 1.43f);
    }
}
