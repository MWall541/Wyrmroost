package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class ControlledAttackGoal extends MeleeAttackGoal
{
    private final AbstractDragonEntity dragon;
    private final Runnable attack;

    public ControlledAttackGoal(AbstractDragonEntity dragon, double speed, boolean longMemory, Runnable attack)
    {
        super(dragon, speed, longMemory);
        this.attack = attack;
        this.dragon = dragon;
    }

    @Override
    public boolean canStart()
    {
        return super.canStart() && !dragon.hasPassengers();
    }

    @Override
    public boolean shouldContinue()
    {
        LivingEntity target = dragon.getTarget();
        if (target == null) return false;
        return !dragon.hasPassengers() && dragon.canAttackWithOwner(target, dragon.getOwner()) && super.shouldContinue();
    }

    @Override
    public void start()
    {
        dragon.setAttacking(true);
    }

    @Override
    protected void attack(LivingEntity enemy, double distToEnemySqr)
    {
        double reach = this.getSquaredMaxAttackDistance(enemy);
        if (distToEnemySqr <= reach && method_28347()) {
            attack.run();
            method_28346();
        }

    }

    @Override
    protected double getSquaredMaxAttackDistance(LivingEntity attackTarget)
    {
        return dragon.getWidth() * 2 * dragon.getWidth() * 2 + attackTarget.getWidth();
    }
}
