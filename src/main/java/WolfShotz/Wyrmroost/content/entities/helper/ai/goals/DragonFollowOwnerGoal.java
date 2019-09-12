package WolfShotz.Wyrmroost.content.entities.helper.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.player.PlayerEntity;

//TODO

/**
 * Owner Following class made specifically for flyers.
 * minDistance and maxDistance is triple for flight, that way, the distance isnt too
 * short when in the air.
 */
public class DragonFollowOwnerGoal extends FollowOwnerGoal
{
    private final AbstractDragonEntity dragon;
    private final float minDistance, maxDistance, speed;
    
    public DragonFollowOwnerGoal(AbstractDragonEntity dragon, float speed, float minDistance, float maxDistance) {
        super(dragon, speed, minDistance, maxDistance);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.dragon = dragon;
    }
    
    @Override
    public boolean shouldExecute() {
        if (!dragon.isFlying()) return super.shouldExecute(); // Do normal checking.
        
        LivingEntity owner = dragon.getOwner();
        float minDistSq = (this.minDistance * this.minDistance) * 3;
        
        if (owner == null) return false; // *Visible confusion*
        if (dragon.isSitting()) return false; // Imagine if it did tho... starts scooting across the ground. lmao
        if (owner instanceof PlayerEntity && owner.isSpectator()) return false; // How would this... nvm
        if (dragon.getDistanceSq(owner) < minDistSq) return false; // Too small of a dist, so nope
    }
    
    @Override
    public void tick() {
        if (!dragon.isFlying()) super.tick(); // Just do the normal follow behaviour when not flying
        
        
    }
}
