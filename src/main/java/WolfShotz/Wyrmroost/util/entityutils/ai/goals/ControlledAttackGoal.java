package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

import java.util.function.Consumer;

public class ControlledAttackGoal extends MeleeAttackGoal
{
    private final AbstractDragonEntity dragon;
    private final double reach;
    private final Consumer<AbstractDragonEntity> attack;

    public ControlledAttackGoal(AbstractDragonEntity dragon, double speed, boolean longMemory, double reach, Consumer<AbstractDragonEntity> attack)
    {
        super(dragon, speed, longMemory);
        this.reach = reach;
        this.attack = attack;
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !dragon.isBeingRidden(); }

    @Override
    public boolean shouldContinueExecuting() { return !dragon.isBeingRidden() && super.shouldContinueExecuting(); }

    @Override
    public void startExecuting() { attacker.setAggroed(true); }

    @Override
    public void tick() { super.tick(); }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
    {
        double d0 = getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && attackTick <= 0 && !dragon.isRidingOrBeingRiddenBy(enemy) && dragon.noActiveAnimation())
        {
            attack.accept(dragon);
            attackTick = 10;
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget)
    {
//        return (this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
        return (dragon.getWidth() * dragon.getWidth()) * reach + (attackTarget.getWidth() * 0.5) * 2;
    }
}
