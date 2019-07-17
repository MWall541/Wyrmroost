package WolfShotz.Wyrmroost.content.entities.minutus.goals;

import WolfShotz.Wyrmroost.content.entities.minutus.MinutusEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.util.EntityPredicates;

public class RunAwayGoal<T extends LivingEntity> extends AvoidEntityGoal<T>
{
    private MinutusEntity entity;

    public RunAwayGoal(MinutusEntity entity, Class<T> avoidingEntity) {
        super(entity, avoidingEntity, 6f, 0.8d, 1.2d, EntityPredicates.CAN_AI_TARGET::test);
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !entity.isBurrowed(); }

    @Override
    public boolean shouldContinueExecuting() { return super.shouldContinueExecuting() && !entity.isBurrowed(); }
}
