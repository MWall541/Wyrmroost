package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

public class NonTamedAvoidGoal extends AvoidEntityGoal
{
    private AbstractDragonEntity dragon;
    private boolean flysAway;
    private float nearSpeed, farSpeed;
    
    public NonTamedAvoidGoal(AbstractDragonEntity dragon, float distance, float speedNear, float speedFar) {
        this(dragon, PlayerEntity.class, distance, speedNear, speedFar, false);
    }
    
    public NonTamedAvoidGoal(AbstractDragonEntity dragon, Class avoidingClass, float distance, float speedNear, float speedFar, boolean flysAway) {
        super(dragon, avoidingClass, distance, speedNear, speedFar, EntityPredicates.CAN_AI_TARGET);
        this.dragon = dragon;
        this.flysAway = flysAway;
        this.nearSpeed = speedNear;
        this.farSpeed = speedFar;
    }
    
    public void tick() {
        if (dragon.getDistanceSq(avoidTarget) < 49.0D) {
            if (flysAway && !dragon.isFlying()) dragon.setFlying(true);
            else dragon.getNavigator().setSpeed(nearSpeed);
        }
        else entity.getNavigator().setSpeed(farSpeed);
    }
}
