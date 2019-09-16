package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.FlightWanderGoal;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.Random;

public class OrbitFlightGoal extends FlightWanderGoal
{
    private final int SWITCH_PATH_THRESHOLD = 850;
    
    private SilverGliderEntity glider;
    private FlightFlag currentFlightAction;
    private FlightFlag ORBIT = FlightFlag.create("ORBIT");
    private Vec3d orbitPos;
    
    public OrbitFlightGoal(SilverGliderEntity glider) {
        super(glider, 1500, 1);
        this.glider = glider;
    }
    
     /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !glider.isRiding(); }
    
    @Override
    public void tick() {
        if (!isDescending() && RAND.nextInt(SWITCH_PATH_THRESHOLD) == 0) switchFlightFlag();
        
        if (isOrbitting()) {
            double x = orbitPos.x + (10 * MathHelper.cos(glider.ticksExisted * 5));
            double z = orbitPos.z + (10 * MathHelper.sin(glider.ticksExisted * 5));
        
            airTarget = new Vec3d(x + 0.5d, orbitPos.y, z + 0.5d);
        }
    }
    
    private void switchFlightFlag() {
        if (isWandering()) {
            currentFlightAction = ORBIT;
            orbitPos = glider.getPositionVec();
        }
        else currentFlightAction = FlightFlag.WANDER;
    }
    
    public void generateAirTarget() {
        super.generateAirTarget();
        
        for (int i = 1; i < 6; ++i)
            if (glider.world.getBlockState(glider.getPosition().down(i)).getMaterial() == Material.WATER) { // Avoid Water
                airTarget = new Vec3d(airTarget.x, Math.abs(airTarget.y), airTarget.z);
                return;
            }
    }
    
    public boolean isOrbitting() { return currentFlightAction == ORBIT; }
}
