package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Objects;

/**
 * Owner Following class made specifically for flyers.
 * minDistance and maxDistance is triple for flight, that way, the distance isnt too
 * short when in the air.
 */
public class DragonFollowOwnerGoal extends FollowOwnerGoal
{
    private AbstractDragonEntity dragon;
    private LivingEntity owner;
    private float minDistance, maxDistance;
    private double speed, height;
    private final boolean FLAG;
    
    public DragonFollowOwnerGoal(AbstractDragonEntity dragon, double speed, float minDistance, float maxDistance, double height) {
        super(dragon, speed, minDistance, maxDistance);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.height = height;
        this.dragon = dragon;
        this.FLAG = dragon.aiFlyWander != null && dragon.aiFlyWander.isDescending();
        
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }
    
    @Override
    public boolean shouldExecute() {
        if (!dragon.isFlying()) return super.shouldExecute(); // Do normal behaviour
        
        this.owner = dragon.getOwner();
        float minDistSq = (this.minDistance * this.minDistance) * 2;
        
        if (owner == null) return false; // *Visible confusion*
        if (FLAG) return false;
        if (owner instanceof PlayerEntity && owner.isSpectator()) return false; // How would this... nvm
        return !(dragon.getDistanceSq(owner.posX, owner.posY + height, owner.posZ) < minDistSq); // Too small of a dist, so nope
    }
    
    @Override
    public void startExecuting() {
        if (!dragon.isFlying()) {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            super.startExecuting();
        } else setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (!dragon.isFlying()) return super.shouldContinueExecuting(); // Do normal behaviour
        if (FLAG) return false;
    
        double maxDistSq = (maxDistance * maxDistance) * 2;
        if (owner == null) return false;
        double distEuclid = (float) dragon.getDistanceSq(owner.posX, owner.posY + height, owner.posZ);
        
        if (dragon.isSitting()) return false; // uhhhhhhh
        return distEuclid > maxDistSq; // em no?
    }
    
    @Override
    public void tick() {
        if (!dragon.isFlying()) { // Do normal behaviour
            super.tick();
            
            return;
        }
    
        Vec3d moveTo = new Vec3d(owner.posX + 0.5d, owner.posY + height, owner.posZ + 0.5d);
        BlockPos tpPos = new BlockPos(moveTo.x, moveTo.y, moveTo.z);
    
        if (MathUtils.getPlaneDistSq(owner, dragon) > (minDistance * minDistance) * 7 && canTeleportToBlock(tpPos)) { // WOAH, too far, tp instead
            dragon.setPositionAndRotation(tpPos.getX() + 0.5d, tpPos.getY(), tpPos.getZ() + 0.5d, owner.rotationYawHead, owner.rotationPitch);
        
            return;
        }
        
        dragon.getMoveHelper().setMoveTo(moveTo.x, moveTo.y, moveTo.z, speed);
        dragon.getLookController().setLookPosition(moveTo.x, moveTo.y, moveTo.z, 90f, 40f);
    }
    
    @Override
    public void resetTask() {
        if (!dragon.isFlying()) super.resetTask();
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    
    @Override
    protected boolean canTeleportToBlock(BlockPos pos) {
        if (!dragon.isFlying()) return super.canTeleportToBlock(pos);

        boolean canTeleport = true;

        for (int xz = (int) (-Math.floor(dragon.getWidth() / 2)); xz < dragon.getWidth(); ++xz) {
            for (int y = 0; y < dragon.getHeight(); ++y) {
                if (world.getBlockState(pos.add(xz, y, xz)).getMaterial().blocksMovement()) {
                    canTeleport = false;
                    break;
                }
            }
        }

        return canTeleport;
    }
}
