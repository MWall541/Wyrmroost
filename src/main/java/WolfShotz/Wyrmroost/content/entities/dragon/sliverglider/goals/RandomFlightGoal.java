package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class RandomFlightGoal extends Goal
{
    private final int SWITCH_PATH_THRESHOLD = 850;
    private final int LAND_THRESHOLD = 1500;
    
    private SilverGliderEntity glider;
    private FlightFlag currentFlightAction;
    private Vec3d orbitPos;
    private Vec3d airTarget;
    private final Random rand;
    private int updateInterval;
    
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
    public void startExecuting() {
        generateAirTarget();
        updateInterval = 80 + new Random().nextInt(20) + 10;
        currentFlightAction = FlightFlag.WANDER;
    }
    
    @Override
    public void tick() {
        // Increase chances to "want" to land at night, for sleep
        final int LAND_THRESHOLD = glider.world.isDaytime()? this.LAND_THRESHOLD : this.LAND_THRESHOLD / 6;
        
        if (rand.nextInt(LAND_THRESHOLD) == 0 && !isDescending()) { // Small chance to start descending
            currentFlightAction = FlightFlag.DESCEND;
            glider.getNavigator().clearPath();
        } // Start circling
//        else if (rand.nextInt(SWITCH_PATH_THRESHOLD) == 0 && !isDescending()) switchFlightFlag();
        
        glider.getMoveHelper().setMoveTo(airTarget.x, airTarget.y, airTarget.z, 1);
        glider.getLookController().setLookPosition(airTarget.x, glider.posY, airTarget.z, 180f, 20f);
        
        if (isWandering() && --updateInterval <= 0) {
            generateAirTarget();
            updateInterval = 120;
        }
        else if (isDescending()) {
            Vec3d look = glider.getLookVec();
            glider.setMotion(look.x / 20, -0.5f, look.z / 20);
        }
        else if (isOrbitting()) {
            double x = orbitPos.x + (10 * MathHelper.cos(glider.ticksExisted * 5));
            double z = orbitPos.z + (10 * MathHelper.sin(glider.ticksExisted * 5));
    
            airTarget = new Vec3d(x + 0.5d, orbitPos.y, z + 0.5d);
        }
    }
    
    private void switchFlightFlag() {
        if (isWandering()) {
            currentFlightAction = FlightFlag.ORBIT;
            orbitPos = glider.getPositionVec();
        }
        else currentFlightAction = FlightFlag.WANDER;
    }
    
    private void generateAirTarget() {
        double x = glider.posX + rand.nextInt(100) - 50;
        double y = glider.posY + rand.nextInt(6) - 2;
        double z = glider.posZ + rand.nextInt(100) - 50;
        for (int i=1; i < 6; ++i) {
            if (glider.world.getBlockState(glider.getPosition().down(i)).getMaterial() == Material.WATER) { // Avoid Water
                y = Math.abs(y);
                break;
            }
        }
        
        airTarget = new Vec3d(x + 0.5d, y, z + 0.5d);
    }
    
    private boolean isWandering() { return currentFlightAction == FlightFlag.WANDER; }
    private boolean isOrbitting() { return currentFlightAction == FlightFlag.ORBIT; }
    private boolean isDescending() { return currentFlightAction == FlightFlag.DESCEND; }
    
    private enum FlightFlag {
        WANDER,
        ORBIT,
        DESCEND
    }
}
