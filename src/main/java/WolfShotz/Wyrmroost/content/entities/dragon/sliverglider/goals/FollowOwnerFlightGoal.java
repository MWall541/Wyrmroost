package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FollowOwnerFlightGoal extends Goal
{
    private final AbstractDragonEntity dragon;
    private float minDistance, maxDistance;
    private LivingEntity owner;
    
    public FollowOwnerFlightGoal(AbstractDragonEntity dragon, float minDistance, float maxDistance) {
        this.dragon = dragon;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (dragon.getOwner() == null) return false;
        this.owner = dragon.getOwner();
        if (!dragon.isFlying()) return false;
        
        return dragon.getDistanceSq(owner) < minDistance * minDistance;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return dragon.getDistanceSq(owner) > 4 * 4 && dragon.isFlying();
    }
    
    @Override
    public void tick() {
        if (dragon.getDistanceSq(owner) < 144d) {
            dragon.setPosition(owner.posX + 0.5d, owner.posY + 15d, owner.posZ + 0.5d);
            
            return;
        }
        
        double x = owner.posX + 0.5d;
        double y = owner.posY + 15d;
        double z = owner.posZ + 0.5d;
        dragon.getMoveHelper().setMoveTo(x, y, z, 1);
        dragon.getLookController().setLookPosition(x, y, z, 180f, 30f);
    }
}
