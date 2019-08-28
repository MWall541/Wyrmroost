package WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.sliverglider.SilverGliderEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AbstractFlightGoal extends Goal
{
    protected SilverGliderEntity glider;
    protected BlockPos circlePoint;
    protected FlightFlag currentFlightAction;
    protected Random rand = new Random();
    protected BlockPos currentPos;
    
    public AbstractFlightGoal(SilverGliderEntity glider) {
        this.glider = glider;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() { return glider.isFlying() && !glider.isRiding(); }
    
    @Override
    public void startExecuting() { currentPos = glider.getPosition(); }
    
    protected void circlePoint() {
    
    }
    
    protected boolean isFlying() { return currentFlightAction == FlightFlag.FLY; }
    protected boolean isCircling() { return currentFlightAction == FlightFlag.CIRCLE; }
    protected boolean isDescending() { return currentFlightAction == FlightFlag.DESCEND; }
    
    protected void switchFlightFlag() { currentFlightAction = isFlying()? FlightFlag.CIRCLE : FlightFlag.FLY; }
    
    protected enum FlightFlag {
        FLY,
        CIRCLE,
        DESCEND
    }
}
