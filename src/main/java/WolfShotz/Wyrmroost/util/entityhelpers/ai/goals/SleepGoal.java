package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Sleep Goal
 * Created by WolfShotz - 8/18/19
 *
 * Sole purpose of this goal is to completely override/stop any other goal from executing. Thus the mutex flags. NOT to put it asleep.
 * That should be handled in the entity class itself. <P>
 * A sleeping creature should act like its asleep.
 */
public class SleepGoal extends Goal
{
    private AbstractDragonEntity dragon;
    
    public SleepGoal(AbstractDragonEntity dragon) {
        this.dragon = dragon;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (dragon.isInWaterOrBubbleColumn() || dragon.isFlying() || !dragon.onGround)
            return false;
        return dragon.isSleeping();
    }
    
    @Override
    public void resetTask() { dragon.setSleeping(false); }
}
