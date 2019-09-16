package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.EnumSet;
import java.util.Random;

public class FlightWanderGoal extends WaterAvoidingRandomWalkingGoal
{
    public AbstractDragonEntity dragon;
    public FlightFlag currentFlightAction;
    public Vec3d airTarget;
    public final Random RAND;
    public int updateInterval, landThreshold;
    
    public FlightWanderGoal(AbstractDragonEntity dragon, int landThreshold, double speed) {
        super(dragon, speed);
        this.dragon = dragon;
        this.RAND = dragon.getRNG();
        this.landThreshold = landThreshold;
    }
    
    @Override
    public boolean shouldExecute() {
        if (dragon.isFlying()) return true;
        else return super.shouldExecute();
    }
    
    @Override // Start with default flight
    public void startExecuting() {
        if (!dragon.isFlying()) {
            super.startExecuting();
            return;
        }
        
        generateAirTarget();
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        updateInterval = 90 + new Random().nextInt(30);
        currentFlightAction = FlightFlag.WANDER;
    }
    
    @Override
    public boolean shouldContinueExecuting() { return dragon.isFlying() || super.shouldContinueExecuting(); }
    
    @Override
    public void tick() {
        // Increase chances to "want" to land at night, for sleep
        final int LAND_THRESHOLD = dragon.world.isDaytime()? this.landThreshold : this.landThreshold / 6;
    
        // Small chance to start descending
        if (RAND.nextInt(LAND_THRESHOLD) == 0 && !isDescending()) setDescending();
        
        if (isWandering()) {
            dragon.getMoveHelper().setMoveTo(airTarget.x, airTarget.y, airTarget.z, 1);
            dragon.getLookController().setLookPosition(airTarget.x, dragon.posY, airTarget.z, 10, 40f);
            if (--updateInterval <= 0) {
                generateAirTarget();
                updateInterval = 90 + new Random().nextInt(30);
            }
        }
        else if (isDescending()) {
            Vec3d look = dragon.getLookVec();
            dragon.setMotion(look.x / 20, -0.5f, look.z / 20);
            dragon.getLookController().setLookPosition(look.x, dragon.posY - 2, look.z, 10f, 40f);
        }
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }
    
    public void generateAirTarget() {
        double x = dragon.posX + RAND.nextInt(100) - 50;
        double y = dragon.posY + RAND.nextInt(6) - 2;
        double z = dragon.posZ + RAND.nextInt(100) - 50;
        
        airTarget = new Vec3d(x + 0.5d, y, z + 0.5d);
    }
    
    public boolean isWandering() { return currentFlightAction == FlightFlag.WANDER; }
    public boolean isDescending() { return currentFlightAction == FlightFlag.DESCEND; }
    
    public void setDescending() {
        currentFlightAction = FlightFlag.DESCEND;
        dragon.getNavigator().clearPath();
        airTarget = null;
    }
    
    public enum FlightFlag implements IExtensibleEnum {
        WANDER,
        DESCEND;
        
        public static FlightFlag create(String name) { throw new IllegalStateException("Enum not extended"); }
    }
}
