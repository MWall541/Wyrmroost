package WolfShotz.Wyrmroost.content.entities.ai.goals;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

public class NonTamedAvoidGoal extends AvoidEntityGoal
{
    private AbstractDragonEntity dragon;
    private boolean flysAway;
    private float speedFar;
    private float speedNear;
    
    public NonTamedAvoidGoal(AbstractDragonEntity dragon, float distance, float speedNear, float speedFar) {
        this(dragon, PlayerEntity.class, distance, speedNear, speedFar, false);
    }
    
    public NonTamedAvoidGoal(AbstractDragonEntity dragon, Class avoidingClass, float distance, float speedNear, float speedFar, boolean flysAway) {
        super(dragon, avoidingClass, distance, speedNear, speedFar, EntityPredicates.CAN_AI_TARGET);
        this.dragon = dragon;
        this.flysAway = flysAway;
        this.speedFar = speedFar;
        this.speedNear = speedNear;
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (entity.getDistanceSq(field_75376_d) < 49.0D) {
            if (flysAway) dragon.setFlying(true);
            else entity.getNavigator().setSpeed(speedFar);
        } else entity.getNavigator().setSpeed(speedNear);
    }
    
}
