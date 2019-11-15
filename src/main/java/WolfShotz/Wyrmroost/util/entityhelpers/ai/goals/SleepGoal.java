package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class SleepGoal extends Goal
{
    public AbstractDragonEntity dragon;
    public int sleepTimeout;
    public final boolean NOCTURNAL;
    public final Random RANDOM = new Random();
    public final World WORLD;
    
    public SleepGoal(AbstractDragonEntity dragon, boolean nocturnal) {
        this.dragon = dragon;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
        this.NOCTURNAL = nocturnal;
        this.WORLD = dragon.world;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (dragon.isSleeping()) return true;
        if (--sleepTimeout > 0) return false;
        if (dragon.isInWaterOrBubbleColumn() || dragon.isFlying()) return false;
        return NOCTURNAL == WORLD.isDaytime() && (!dragon.isTamed() || dragon.isSitting()) && RANDOM.nextInt(300) == 0;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (WORLD.isDaytime() && dragon.isSleeping() && RANDOM.nextInt(150) != 0) return false; // Stop sleeping at daytime
        if ((dragon.isTamed() && !dragon.isSitting()) || dragon.isBeingRidden()) return false; // lol sleep while being ridden. Snorlax intensifies
        if (dragon.getAttackTarget() != null || !dragon.getNavigator().noPath() || dragon.isAngry()) return false; // Check ai shtuffs. Shouldnt sleep when were angry
        return !dragon.isInWaterOrBubbleColumn() && !dragon.isFlying(); // Check actually reasonable things (imagine sleeping while flying)
    }
    
    @Override
    public void startExecuting() { dragon.setSleeping(true); }
    
    @Override
    public void resetTask() {
        sleepTimeout = 350;
        dragon.setSleeping(false);
    }
}
