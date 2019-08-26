package WolfShotz.Wyrmroost.content.entities.helper.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;

public class RandomLookGoal extends LookRandomlyGoal
{
    private AbstractDragonEntity dragon;
    
    public RandomLookGoal(AbstractDragonEntity dragon) {
        super(dragon);
        this.dragon = dragon;
    }
    
    @Override
    public boolean shouldExecute() { return !dragon.isSleeping() && super.shouldExecute(); }
}
