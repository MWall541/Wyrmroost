package WolfShotz.Wyrmroost.content.entities.dragon.minutus.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;

public class WalkRandom extends WaterAvoidingRandomWalkingGoal
{
    private MinutusEntity minutus;

    public WalkRandom(MinutusEntity entity) {
        super(entity, 1d);
        this.minutus = entity;
    }

    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !minutus.isBurrowed(); }
}
