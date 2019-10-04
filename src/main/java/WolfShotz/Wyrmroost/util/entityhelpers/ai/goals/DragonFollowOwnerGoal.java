package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

public class DragonFollowOwnerGoal extends Goal {
    protected final AbstractDragonEntity dragon;
    private LivingEntity owner;
    protected final IWorldReader world;
    private final double followSpeed;
    private final PathNavigator navigator;
    private int timeToRecalcPath;
    private final double maxDist, minDist, maxHeight;
    private float oldWaterCost;
    
    public DragonFollowOwnerGoal(AbstractDragonEntity dragonIn, double followSpeedIn, double minDistIn, double maxDistIn, double maxHeightIn) {
        this.dragon = dragonIn;
        this.world = dragonIn.world;
        this.followSpeed = followSpeedIn;
        this.navigator = dragonIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.maxHeight = maxHeightIn;
        setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    
    public DragonFollowOwnerGoal(AbstractDragonEntity dragonIn, double followSpeedIn, double minDistIn, double maxDistIn) {
        this.dragon = dragonIn;
        this.world = dragonIn.world;
        this.followSpeed = followSpeedIn;
        this.navigator = dragonIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.maxHeight = 0;
        setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (dragon.isSleeping()) return false;
        
        LivingEntity preOwner = dragon.getOwner();
        
        if (dragon.isSitting() || preOwner == null || (preOwner instanceof PlayerEntity && preOwner.isSpectator()))
            return false;
    
        double minDistSq = (minDist * minDist);
        boolean tooClose = dragon.isFlying()? (dragon.getDistanceSq(preOwner.getPositionVec().add(0, maxHeight, 0)) < minDistSq) : (dragon.getDistanceSq(preOwner) < minDistSq);
        
        if (tooClose) return false;
        else {
            owner = preOwner;
            return true;
        }
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (dragon.isSitting() || owner == null) return false;
        
        double maxDistSq = (maxDist * maxDist);
        
        if (dragon.isFlying())
            return dragon.getDistanceSq(owner.getPositionVec().add(0, maxHeight, 0)) > maxDistSq;
        else return !navigator.noPath() && dragon.getDistanceSq(owner) > maxDistSq;
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.dragon.getPathPriority(PathNodeType.WATER);
        this.dragon.setPathPriority(PathNodeType.WATER, 0.0F);
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.owner = null;
        this.navigator.clearPath();
        this.dragon.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (dragon.isSitting()) return;
        if (--this.timeToRecalcPath > 0) return;
        timeToRecalcPath = 10;
        if (dragon.getLeashed() || dragon.isPassenger()) return;
        
        if (dragon.isFlying()) {
            if (dragon.getDistanceSq(owner.getPositionVec().add(0, maxHeight, 0)) > (3d * (minDist * minDist)))
                dragon.tryTeleportToPos(owner.getPosition().add(-2, maxHeight, -2));
            else {
                double x = owner.posX + 0.5d;
                double y = owner.posY + maxHeight;
                double z = owner.posZ + 0.5d;
                dragon.getMoveHelper().setMoveTo(x, y, z, followSpeed);
                dragon.getLookController().setLookPosition(x, y, z, 10f, dragon.getVerticalFaceSpeed());
            }
        } else {
            if (dragon.getDistanceSq(owner) > (1.5d * (minDist * minDist)))
                dragon.tryTeleportToOwner();
            else {
                navigator.tryMoveToEntityLiving(owner, followSpeed);
                dragon.getLookController().setLookPositionWithEntity(owner, 10f, dragon.getVerticalFaceSpeed());
            }
        }
    }
}