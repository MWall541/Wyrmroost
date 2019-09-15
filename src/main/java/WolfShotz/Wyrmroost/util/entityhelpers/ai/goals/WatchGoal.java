package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;

public class WatchGoal extends LookAtGoal
{
    private AbstractDragonEntity dragon;
    
    public WatchGoal(AbstractDragonEntity entityIn, Class<? extends LivingEntity> watchTargetClass, float maxDistance) {
        this(entityIn, watchTargetClass, maxDistance, 0.02F);
    }
    
    public WatchGoal(AbstractDragonEntity entity, Class<? extends LivingEntity> watchTargetClass, float maxDistance, float chanceIn) {
        super(entity, watchTargetClass, maxDistance, chanceIn);
        this.dragon = entity;
    }
    
    @Override
    public boolean shouldExecute() {
        return !dragon.isSleeping() && super.shouldExecute();
    }
}
