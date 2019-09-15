package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;

public class WanderGoal extends WaterAvoidingRandomWalkingGoal
{
    private AbstractDragonEntity dragon;
    
    public WanderGoal(AbstractDragonEntity dragon, double speed) {
        super(dragon, speed);
        this.dragon = dragon;
    }
    
    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !dragon.isFlying(); }
}
