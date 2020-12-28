package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;

public class SleepGoal extends Goal
{
    protected final BooleanSupplier sleepTest;
    protected final BooleanSupplier wakeTest;
    private final AbstractDragonEntity dragon;
    protected int cooldown;

    public SleepGoal(AbstractDragonEntity dragon, BooleanSupplier sleepTest, BooleanSupplier wakeTest)
    {
        this.dragon = dragon;
        this.sleepTest = sleepTest;
        this.wakeTest = wakeTest;

        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK, Flag.TARGET));
    }

    public SleepGoal(AbstractDragonEntity dragon, boolean watchesHome)
    {
        this(dragon, () -> shouldSleep(dragon, watchesHome), () -> shouldWakeUp(dragon));
    }

    @Override
    public boolean isPreemptible()
    {
        return true;
    }

    @Override
    public boolean shouldExecute()
    {
        if (dragon.isSleeping()) return true;
        if (cooldown > 0)
        {
            --cooldown;
            return false;
        }

        return sleepTest.getAsBoolean();
    }

    @Override
    public void startExecuting()
    {
        dragon.setSleeping(true);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return dragon.isSleeping() && wakeTest.getAsBoolean();
    }

    @Override
    public void resetTask()
    {
        cooldown = 350;
        dragon.setSleeping(false);
    }

    public static boolean shouldSleep(AbstractDragonEntity dragon, boolean watchesHome)
    {
        if (dragon.world.isDaytime()) return false;
        if (!dragon.isIdling()) return false;
        if (dragon.isTamed())
        {
            if (dragon.isAtHome())
            {
                if (watchesHome) return dragon.getHealth() < dragon.getMaxHealth() * 0.25;
            }
            else if (!dragon.func_233684_eK_()) return false;
        }

        return dragon.getRNG().nextDouble() < 0.01;
    }

    public static boolean shouldWakeUp(AbstractDragonEntity dragon)
    {
        return dragon.world.isDaytime() && dragon.getRNG().nextDouble() > 0.01;
    }
}
