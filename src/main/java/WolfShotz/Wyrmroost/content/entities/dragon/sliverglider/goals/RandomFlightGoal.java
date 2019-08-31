package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class RandomFlightGoal extends Goal
{
    private final int SWITCH_PATH_THRESHOLD = 850;
    private final int LAND_THRESHOLD = 1500;
    
    private SilverGliderEntity glider;
    private FlightFlag currentFlightAction;
    private int changeDirInterval;
    private BlockPos currentPos;
    private BlockPos orbitPos;
    private final Random rand;
    
    public RandomFlightGoal(SilverGliderEntity glider) {
        this.glider = glider;
        this.rand = glider.getRNG();
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() { return glider.isFlying() && !glider.isRiding(); }
    
    @Override
    public void startExecuting() {
        currentPos = glider.getPosition();
        currentFlightAction = FlightFlag.FLY;
    }
    
    @Override
    public void tick() {
        if (rand.nextInt(LAND_THRESHOLD) == 0 && !isDescending()) {
            currentFlightAction = FlightFlag.DESCEND;
            glider.getNavigator().clearPath();
            glider.setMotion(Vec3d.ZERO);
        }
        
//        if (rand.nextInt(SWITCH_PATH_THRESHOLD) == 0 && !isDescending()) switchFlightFlag();
        
        if (isFlying()) {
            flyTick();
            
            return;
        }
        
        if (isDescending()) {
            descendTick();
            
            return;
        }
        
        if (isOrbitting())
            orbitTick();
        
    }
    
    private void flyTick() { //TODO ReEvaluate!
        if (--changeDirInterval <= 0) {
            changeDirInterval = 40 + rand.nextInt(30) + 10;
        }
    }
    
    private void descendTick() {
        Vec3d look = glider.getLookVec();
        glider.setMotion(look.x / 22, -0.5f, look.z / 22);
    }
    
    private void orbitTick() {
    }
    
    protected boolean isFlying() { return currentFlightAction == FlightFlag.FLY; }
    protected boolean isOrbitting() { return currentFlightAction == FlightFlag.ORIBT; }
    protected boolean isDescending() { return currentFlightAction == FlightFlag.DESCEND; }
    
    private void switchFlightFlag() {
        if (isFlying()) {
            currentFlightAction = FlightFlag.ORIBT;
            orbitPos = currentPos;
        } else currentFlightAction = FlightFlag.FLY;
    }
    
    private enum FlightFlag {
        FLY,
        ORIBT,
        DESCEND
    }
}
