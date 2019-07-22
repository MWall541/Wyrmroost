package WolfShotz.Wyrmroost.content.entities.ai;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class SleepGoal extends Goal
{
    private AbstractDragonEntity dragon;
    private int buffer = new Random().nextInt(120);

    public SleepGoal(AbstractDragonEntity dragonIn) {
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        this.dragon = dragonIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (dragon.isAngry()) return false;
        if (dragon.isTamed() && !dragon.isSitting()) return false;
        if (dragon.isSitting()) return new Random().nextInt(120) == 0 || trySleep();
        return trySleep();
    }

    @Override
    public boolean shouldContinueExecuting() { return trySleep(); }

    @Override
    public void startExecuting() {
        dragon.setSprinting(false);
        dragon.setJumping(false);
        dragon.getNavigator().clearPath();
        dragon.getMoveHelper().setMoveTo(dragon.posX, dragon.posY, dragon.posZ, 0d);
        dragon.setAsleep(true);
    }

    @Override
    public void resetTask() { buffer = new Random().nextInt(120); }

    private boolean trySleep() {
        if (--buffer > 0) return false;
        return !dragon.world.isDaytime() || dragon.isAsleep();
    }
}
