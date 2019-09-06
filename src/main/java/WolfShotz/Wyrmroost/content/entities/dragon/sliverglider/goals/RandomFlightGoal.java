package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import net.minecraft.entity.ai.controller.MovementController;
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
    
    @Override // Start with default flight
    public void startExecuting() { currentFlightAction = FlightFlag.WANDER; }
    
    @Override
    public void tick() {
        // Increase chances to "want" to land at night, for sleep
        final int LAND_THRESHOLD = glider.world.isDaytime()? this.LAND_THRESHOLD : this.LAND_THRESHOLD / 6;
        
        if (rand.nextInt(LAND_THRESHOLD) == 0 && !isDescending()) { // Small chance to start descending
            currentFlightAction = FlightFlag.DESCEND;
            glider.getNavigator().clearPath();
        } // Start cicling
//        else if (rand.nextInt(SWITCH_PATH_THRESHOLD) == 0 && !isDescending()) switchFlightFlag();
        
        if (isWandering()) wanderTick();
        else if (isDescending()) descendTick();
        else if (isOrbitting()) orbitTick();
    }
    
    private void wanderTick() {
        MovementController moveHelper = glider.getMoveHelper();
        
        if (!moveHelper.isUpdating()) {
            double x = glider.posX + rand.nextInt(50) - 25;
            double y = glider.posY + rand.nextInt(6) - 2;
            double z = glider.posZ + rand.nextInt(50) - 25;
            moveHelper.setMoveTo(x + 0.5d, y, z + 0.5d, 1);
            glider.getLookController().setLookPosition(x, glider.posY, z, 180f, 20f);
        }
    }
    
    private void descendTick() {
        System.out.println("out");
        
        Vec3d look = glider.getLookVec();
        glider.setMotion(look.x / 20, -0.5f, look.z / 20);
    }
    
    private void orbitTick() {
        // ...
    }
    
    protected boolean isWandering() { return currentFlightAction == FlightFlag.WANDER; }
    protected boolean isOrbitting() { return currentFlightAction == FlightFlag.ORBIT; }
    protected boolean isDescending() { return currentFlightAction == FlightFlag.DESCEND; }
    
    private void switchFlightFlag() {
        if (isWandering()) {
            currentFlightAction = FlightFlag.ORBIT;
            orbitPos = glider.getPosition();
        }
        else currentFlightAction = FlightFlag.WANDER;
    }
    
    private enum FlightFlag {
        WANDER,
        ORBIT,
        DESCEND
    }
}
