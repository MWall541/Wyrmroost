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
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
        this.dragon = dragon;
        this.NOCTURNAL = nocturnal;
        this.WORLD = dragon.world;
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (dragon.isSleeping() && --sleepTimeout <= 0) return true;
        return NOCTURNAL == WORLD.isDaytime() && shouldSleep(dragon) && RANDOM.nextInt(300) == 0;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (!dragon.isSleeping() || !shouldSleep(dragon)) return false;
        return NOCTURNAL != WORLD.isDaytime() && RANDOM.nextInt(150) == 0;
    }
    
    @Override
    public void startExecuting() { dragon.setSleeping(true); }
    
    @Override
    public void resetTask() {
        sleepTimeout = 350;
        dragon.setSleeping(false);
    }
    
    public static boolean shouldSleep(AbstractDragonEntity dragon) {
        return (!dragon.isTamed() || dragon.isSitting()) &&
                       !dragon.isBeingRidden()
                       && dragon.getAttackTarget() == null
                       && dragon.getNavigator().noPath()
                       && !dragon.isAngry()
                       && !dragon.isInWaterOrBubbleColumn()
                       && !dragon.isFlying();
    }
}
