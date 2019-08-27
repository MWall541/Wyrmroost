package WolfShotz.Wyrmroost.content.entities.helper.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

public class NonTamedAvoidGoal extends AvoidEntityGoal
{
    private AbstractDragonEntity dragon;
    private boolean flysAway;
    
    public NonTamedAvoidGoal(AbstractDragonEntity dragon, float distance, float speedNear, float speedFar) {
        this(dragon, PlayerEntity.class, distance, speedNear, speedFar, false);
    }
    
    public NonTamedAvoidGoal(AbstractDragonEntity dragon, Class avoidingClass, float distance, float speedNear, float speedFar, boolean flysAway) {
        super(dragon, avoidingClass, distance, speedNear, speedFar, EntityPredicates.CAN_AI_TARGET);
        this.dragon = dragon;
        this.flysAway = flysAway;
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        if (flysAway) dragon.setFlying(true);
    }
}
