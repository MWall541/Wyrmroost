package WolfShotz.Wyrmroost.content.entities.ai.goals;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.BreedGoal;

public class DragonBreedGoal extends BreedGoal
{
    private AbstractDragonEntity dragon;
    
    public DragonBreedGoal(AbstractDragonEntity dragon) {
        super(dragon, 1.0d);
        this.dragon = dragon;
    }
    
    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute()) return ((AbstractDragonEntity) field_75391_e).getGender() == !dragon.getGender();
        return false;
    }
}
