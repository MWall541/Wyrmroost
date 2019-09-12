package WolfShotz.Wyrmroost.content.entities.dragon.minutus.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.util.EntityPredicates;

import java.util.function.Predicate;

public class RunAwayGoal<T extends LivingEntity> extends AvoidEntityGoal<T>
{
    private MinutusEntity entity;
    private static final Predicate<LivingEntity> TEST = t -> EntityPredicates.CAN_AI_TARGET.test(t) && !(t instanceof MinutusEntity);

    public RunAwayGoal(MinutusEntity entity, Class<T> avoidingEntity) {
        super(entity, avoidingEntity, 6f, 0.8d, 1.2d, TEST);
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !entity.isBurrowed(); }

    @Override
    public boolean shouldContinueExecuting() { return super.shouldContinueExecuting() && !entity.isBurrowed(); }
    
}
