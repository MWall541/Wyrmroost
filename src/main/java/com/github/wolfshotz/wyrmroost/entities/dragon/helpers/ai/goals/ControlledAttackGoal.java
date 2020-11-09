package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

import java.util.function.Consumer;

public class ControlledAttackGoal extends MeleeAttackGoal
{
    private final AbstractDragonEntity dragon;
    private final Consumer<AbstractDragonEntity> attack;

    public ControlledAttackGoal(AbstractDragonEntity dragon, double speed, boolean longMemory, Consumer<AbstractDragonEntity> attack)
    {
        super(dragon, speed, longMemory);
        this.attack = attack;
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !dragon.isBeingRidden(); }

    @Override
    public boolean shouldContinueExecuting()
    {
        LivingEntity target = dragon.getAttackTarget();
        if (target == null) return false;
        return !dragon.isBeingRidden() && dragon.shouldAttackEntity(target, dragon.getOwner()) && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() { attacker.setAggroed(true); }

    @Override
    public void tick() { super.tick(); }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
    {
        double reach = getAttackReachSqr(enemy);
        if (distToEnemySqr <= reach && attackTick <= 0 && !dragon.isRidingOrBeingRiddenBy(enemy) && dragon.noActiveAnimation())
        {
            attack.accept(dragon);
            attackTick = 20;
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget)
    {
        return attacker.getWidth() * 2 * attacker.getWidth() * 2 + attackTarget.getWidth();
    }
}
