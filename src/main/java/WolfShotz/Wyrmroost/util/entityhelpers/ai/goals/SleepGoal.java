package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class SleepGoal extends Goal
{
    public AbstractDragonEntity dragon;
    public int sleepTimeout;
    public final boolean NOCTURNAL;
    public final Random RANDOM = new Random();
    public final World WORLD;
    private static final Predicate<AbstractDragonEntity> SHOULD_SLEEP = dragon -> dragon.isBeingRidden()
                   || dragon.getAttackTarget() != null
                   || !dragon.getNavigator().noPath()
                   || dragon.isAngry()
                   || dragon.isInWaterOrBubbleColumn()
                   || dragon.isFlying();
    
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
        if (SHOULD_SLEEP.test(dragon)) return false;
        return NOCTURNAL == WORLD.isDaytime() && (!dragon.isTamed() || dragon.isSitting()) && RANDOM.nextInt(300) == 0;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        if (!dragon.isSleeping() || SHOULD_SLEEP.test(dragon)) return false;
        return NOCTURNAL != WORLD.isDaytime() && RANDOM.nextInt(150) == 0;
    }
    
    @Override
    public void startExecuting() { dragon.setSleeping(true); }
    
    @Override
    public void resetTask() {
        sleepTimeout = 350;
        dragon.setSleeping(false);
    }
}
