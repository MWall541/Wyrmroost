package WolfShotz.Wyrmroost.content.entities.helper.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class DragonFollowOwnerGoal extends FollowOwnerGoal
{
    private final AbstractDragonEntity dragon;
    
    public DragonFollowOwnerGoal(AbstractDragonEntity dragon, float speed, float minDistance, float maxDistance) {
        super(dragon, speed, minDistance, maxDistance);
        this.dragon = dragon;
    }
    
    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !dragon.isFlying();
    }
}
