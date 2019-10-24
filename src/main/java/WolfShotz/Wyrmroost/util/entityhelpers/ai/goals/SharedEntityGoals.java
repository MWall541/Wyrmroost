package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.item.crafting.Ingredient;

/**
 * Shared Dragon Entity goals
 * A small library of tweaked vanilla goals for Dragon entities
 *
 * Created by WolfShotz on 10/23/2019
 */
public class SharedEntityGoals
{
    public static WaterAvoidingRandomWalkingGoal wanderAvoidWater(AbstractDragonEntity entity, double speed) {
        return new WaterAvoidingRandomWalkingGoal(entity, speed) {
            @Override public boolean shouldExecute() { return !entity.isSleeping() && super.shouldExecute(); }
        };
    }
    
    public static LookAtGoal lookAtNoSleeping(AbstractDragonEntity entity, Class<LivingEntity> toLookat, float distance) {
        return new LookAtGoal(entity, toLookat, distance) {
            @Override public boolean shouldExecute() {
                if (!super.shouldExecute()) return false;
                if (entity.isSleeping()) return false;
                return !entity.getPassengers().contains(closestEntity);
            }
        };
    }
    
    public static LookAtGoal lookAtNoSleeping(AbstractDragonEntity entity, float distance) { return lookAtNoSleeping(entity, LivingEntity.class, distance); }
    
    public static LookRandomlyGoal lookRandomlyNoSleeping(AbstractDragonEntity entity) {
        return new LookRandomlyGoal(entity) {
            @Override public boolean shouldExecute() { return !entity.isSleeping() && super.shouldExecute(); }
        };
    }
    
    public static TemptGoal nonTamedTemptGoal(AbstractDragonEntity dragon, double speed, boolean scaredByMovement, Ingredient temptedItems) {
        return new TemptGoal(dragon, speed, scaredByMovement, temptedItems) {
            @Override public boolean shouldExecute() { return !((AbstractDragonEntity) creature).isTamed() && super.shouldExecute(); }
        };
    }
}
