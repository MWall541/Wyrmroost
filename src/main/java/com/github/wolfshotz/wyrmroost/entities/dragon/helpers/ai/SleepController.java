package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;

import java.util.function.BooleanSupplier;

public class SleepController
{
    private final AbstractDragonEntity dragon;
    public int coolDown;
    public double sleepChance = 0.01;
    public boolean nocturnal = false;
    public boolean defendsHome = false;
    public BooleanSupplier sleepConditions;
    public BooleanSupplier wakeConditions;

    public SleepController(AbstractDragonEntity dragon)
    {
        this.dragon = dragon;
    }

    public SleepController setSleepChance(double sleepChance)
    {
        this.sleepChance = sleepChance;
        return this;
    }

    public SleepController setNocturnal()
    {
        this.nocturnal = true;
        return this;
    }

    /**
     * This essentially just doesn't allow the dragon to sleep if a home position is set and is not sitting.
     */
    public SleepController setHomeDefender()
    {
        this.defendsHome = true;
        return this;
    }

    public SleepController addSleepCondition(BooleanSupplier sup)
    {
        if (sleepConditions == null) this.sleepConditions = sup;
        else this.sleepConditions = () -> sleepConditions.getAsBoolean() && sup.getAsBoolean();
        return this;
    }

    public SleepController addWakeConditions(BooleanSupplier sup)
    {
        if (wakeConditions == null) this.wakeConditions = sup;
        else this.wakeConditions = () -> wakeConditions.getAsBoolean() && sup.getAsBoolean();
        return this;
    }


    public void tick()
    {
        if (coolDown > 0)
        {
            coolDown--;
            return;
        }

        if (dragon.isSleeping())
        {
            if (shouldWakeUp() && dragon.getRNG().nextDouble() < sleepChance * 1.75) dragon.setSleeping(false);
        }
        else
        {
            if (shouldSleep() && dragon.getRNG().nextDouble() < sleepChance) dragon.setSleeping(true);
        }
    }

    public boolean shouldSleep()
    {
        if (dragon.isIdling() && dragon.world.isDaytime() == nocturnal && (sleepConditions == null || sleepConditions.getAsBoolean()))
        {
            if (dragon.isTamed())
                return dragon.isSitting() || (dragon.isAtHome() && (!defendsHome || dragon.getHealth() <= dragon.getMaxHealth() * 0.25));
            else return true;
        }
        return false;
    }

    public boolean shouldWakeUp()
    {
        return dragon.world.isDaytime() != nocturnal && (wakeConditions == null || wakeConditions.getAsBoolean());
    }
}
