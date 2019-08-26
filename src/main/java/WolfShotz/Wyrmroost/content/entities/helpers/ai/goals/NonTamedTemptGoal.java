package WolfShotz.Wyrmroost.content.entities.helpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.crafting.Ingredient;

public class NonTamedTemptGoal extends TemptGoal
{
    private AbstractDragonEntity dragon;

    public NonTamedTemptGoal(AbstractDragonEntity dragon, double speed, boolean scaredByMovement, Ingredient temptedItems) {
        super(dragon, speed, scaredByMovement, temptedItems);
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() {
        if (dragon.isTamed()) return false;
        return super.shouldExecute();
    }
}
