package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.crafting.Ingredient;

public class NonTamedBabyTemptGoal extends TemptGoal
{
    private final DragonFruitDrakeEntity dfd;

    public NonTamedBabyTemptGoal(DragonFruitDrakeEntity creatureIn, double speedIn, Ingredient temptItemsIn)
    {
        super(creatureIn, speedIn, false, temptItemsIn);
        this.dfd = creatureIn;
    }

    @Override
    public boolean shouldExecute()
    {
        return !dfd.isTamed() && dfd.isChild() && super.shouldExecute();
    }
}
