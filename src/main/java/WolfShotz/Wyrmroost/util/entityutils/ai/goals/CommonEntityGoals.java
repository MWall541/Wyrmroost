package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.crafting.Ingredient;

import java.util.EnumSet;

/**
 * Shared Dragon Entity goals
 * A small library of tweaked vanilla goals for Dragon entities
 * Because vanilla can't do it the right way
 * <p>
 * Created by WolfShotz on 10/23/2019
 */
public class CommonEntityGoals
{
    /**
     * Walk randomly but do not execute if sleeping
     *
     * @param entity Goal Owner
     * @param speed  speed to walk at
     */
    public static WaterAvoidingRandomWalkingGoal wanderAvoidWater(AbstractDragonEntity entity, double speed)
    {
        return new WaterAvoidingRandomWalkingGoal(entity, speed)
        {
            @Override
            public boolean shouldExecute()
            {
                return !entity.isSleeping() && super.shouldExecute();
            }
        };
    }

    /**
     * Look at random entities. Do this while NOT sleeping
     *
     * @param entity   goal Owner
     * @param toLookat Entity Class to look at
     * @param distance distance to look at entites
     */
    public static LookAtGoal lookAt(AbstractDragonEntity entity, Class<LivingEntity> toLookat, float distance)
    {
        return new LookAtGoal(entity, toLookat, distance)
        {
            @Override
            public boolean shouldExecute()
            {
                if (!super.shouldExecute()) return false;
                if (entity.isSleeping()) return false;
                return !entity.getPassengers().contains(closestEntity);
            }
        };
    }

    /**
     * Simplified version of {@link #lookAt(AbstractDragonEntity, float)}
     * Goal Owner will look at everything extending off of {@link LivingEntity}
     *
     * @param entity   Goal Owner
     * @param distance distance to at entities
     * @return
     */
    public static LookAtGoal lookAt(AbstractDragonEntity entity, float distance)
    {
        return lookAt(entity, LivingEntity.class, distance);
    }

    public static LookRandomlyGoal lookRandomly(AbstractDragonEntity entity)
    {
        return new LookRandomlyGoal(entity)
        {
            @Override
            public boolean shouldExecute()
            {
                return !entity.isSleeping() && super.shouldExecute();
            }
        };
    }
    
    /**
     * Have the goal owner be tempted to players holding items (walk towards them)
     *
     * @param dragon           Goal Owner
     * @param speed            speed to "walk towards" at
     * @param scaredByMovement should fast movement reset this task
     * @param temptedItems     the items to temp the goal owner
     */
    public static TemptGoal nonTamedTemptGoal(AbstractDragonEntity dragon, double speed, boolean scaredByMovement, Ingredient temptedItems)
    {
        return new TemptGoal(dragon, speed, scaredByMovement, temptedItems)
        {
            @Override
            public boolean shouldExecute()
            {
                return !((AbstractDragonEntity) creature).isTamed() && super.shouldExecute();
            }
        };
    }

    /**
     * Follow parent goal but its cooler cus mutex flags
     *
     * @param animal The animal
     * @param speed  The Speed
     * @return a better follow parent goal
     */
    public static FollowParentGoal followParentGoal(AnimalEntity animal, double speed)
    {
        FollowParentGoal goal = new FollowParentGoal(animal, speed);
        goal.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        return goal;
    }
}
