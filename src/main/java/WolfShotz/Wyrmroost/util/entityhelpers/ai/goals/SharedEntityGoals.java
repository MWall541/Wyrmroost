package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Shared Dragon Entity goals
 * A small library of tweaked vanilla goals for Dragon entities
 *
 * Created by WolfShotz on 10/23/2019
 */
public class SharedEntityGoals
{
    /**
     * Walk randomly but do not execute if sleeping
     * @param entity Goal Owner
     * @param speed speed to walk at
     */
    public static WaterAvoidingRandomWalkingGoal wanderAvoidWater(AbstractDragonEntity entity, double speed) {
        return new WaterAvoidingRandomWalkingGoal(entity, speed) {
            @Override public boolean shouldExecute() { return !entity.isSleeping() && super.shouldExecute(); }
        };
    }
    
    /**
     * Look at random entities. Do this while NOT sleeping
     * @param entity goal Owner
     * @param toLookat Entity Class to look at
     * @param distance distance to look at entites
     */
    public static LookAtGoal lookAtNoSleeping(AbstractDragonEntity entity, Class<LivingEntity> toLookat, float distance) {
        return new LookAtGoal(entity, toLookat, distance) {
            @Override public boolean shouldExecute() {
                if (!super.shouldExecute()) return false;
                if (entity.isSleeping()) return false;
                return !entity.getPassengers().contains(closestEntity);
            }
        };
    }
    
    /**
     * Simplified version of {@link #lookAtNoSleeping(AbstractDragonEntity, float)}
     * Goal Owner will look at everything extending off of {@link LivingEntity}
     * @param entity Goal Owner
     * @param distance distance to at entities
     * @return
     */
    public static LookAtGoal lookAtNoSleeping(AbstractDragonEntity entity, float distance) { return lookAtNoSleeping(entity, LivingEntity.class, distance); }
    
    public static LookRandomlyGoal lookRandomlyNoSleeping(AbstractDragonEntity entity) {
        return new LookRandomlyGoal(entity) {
            @Override public boolean shouldExecute() { return !entity.isSleeping() && super.shouldExecute(); }
        };
    }
    
    /**
     * Have the goal owner be tempted to players holding items (walk towards them)
     * @param dragon Goal Owner
     * @param speed speed to "walk towards" at
     * @param scaredByMovement should fast movement reset this task
     * @param temptedItems the items to temp the goal owner
     */
    public static TemptGoal nonTamedTemptGoal(AbstractDragonEntity dragon, double speed, boolean scaredByMovement, Ingredient temptedItems) {
        return new TemptGoal(dragon, speed, scaredByMovement, temptedItems) {
            @Override public boolean shouldExecute() { return !((AbstractDragonEntity) creature).isTamed() && super.shouldExecute(); }
        };
    }
    
    /**
     * When not tamed, attack the nearest attackable target matching the entity class and predicate filter
     * @param dragon Goal Owner
     * @param tClass Target Class
     * @param distance Distance to search for targets
     * @param checkSight Should the target be in sight of the goal Owner?
     * @param nearbyOnly Should the target be near the goal owner?
     * @param filter Conditions the target has to pass
     */
    @SuppressWarnings("unchecked") // Controlled
    public static <T extends LivingEntity> NearestAttackableTargetGoal nonTamedTargetGoal(AbstractDragonEntity dragon, Class<T> tClass, int distance, boolean checkSight, boolean nearbyOnly, @Nullable Predicate<? extends Entity> filter) {
        return new NearestAttackableTargetGoal(dragon, tClass, distance, checkSight, nearbyOnly, filter) {
            public boolean shouldExecute() { return !((AbstractDragonEntity) goalOwner).isTamed() && super.shouldExecute(); }
    
            public boolean shouldContinueExecuting() { return targetEntitySelector != null ? targetEntitySelector.canTarget(goalOwner, nearestTarget) : super.shouldContinueExecuting(); }
        };
    }
}
