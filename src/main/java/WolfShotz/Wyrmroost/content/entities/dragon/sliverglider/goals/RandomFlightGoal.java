package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
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
    public void startExecuting() { currentFlightAction = FlightFlag.FLY; }
    
    @Override
    public void tick() {
        int landThresholdNight = glider.world.isDaytime()? LAND_THRESHOLD : LAND_THRESHOLD / 4;
        
        if (rand.nextInt(landThresholdNight) == 0 && !isDescending()) {
            currentFlightAction = FlightFlag.DESCEND;
            glider.getNavigator().clearPath();
        }
        
//        if (rand.nextInt(SWITCH_PATH_THRESHOLD) == 0 && !isDescending()) switchFlightFlag();
        
        if (isFlying()) flyTick();
        else if (isDescending()) descendTick();
        else if (isOrbitting()) orbitTick();
        
    }
    
    private void flyTick() {
        PathNavigator nav = glider.getNavigator();
        
        if (!glider.hasPath()) {
            double x = glider.posX + rand.nextDouble() * 16;
            double y = glider.posY + rand.nextDouble() * 16;
            double z = glider.posZ + rand.nextDouble() * 16;
            nav.tryMoveToXYZ(x, y, z, 1);
        }
    }
    
    private void descendTick() {
        Vec3d look = glider.getLookVec();
        glider.setMotion(look.x / 20, -0.5f, look.z / 20);
    }
    
    private void orbitTick() {
    }
    
    protected boolean isFlying() { return currentFlightAction == FlightFlag.FLY; }
    protected boolean isOrbitting() { return currentFlightAction == FlightFlag.ORBIT; }
    protected boolean isDescending() { return currentFlightAction == FlightFlag.DESCEND; }
    
    private void switchFlightFlag() {
        if (isFlying()) {
            currentFlightAction = FlightFlag.ORBIT;
            orbitPos = glider.getPosition();
        }
        else currentFlightAction = FlightFlag.FLY;
    }
    
    private enum FlightFlag {
        FLY,
        ORBIT,
        DESCEND
    }
}
