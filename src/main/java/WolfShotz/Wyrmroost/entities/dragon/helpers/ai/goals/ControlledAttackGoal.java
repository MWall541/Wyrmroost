package WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
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
    public void tick() { if (dragon.noActiveAnimation()) super.tick(); }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
    {
        double reach = getAttackReachSqr(enemy);
        if (distToEnemySqr <= reach && func_234040_h_() && !dragon.isRidingOrBeingRiddenBy(enemy) && dragon.noActiveAnimation())
        {
            attack.accept(dragon);
            func_234039_g_();
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity attackTarget)
    {
        return attacker.getWidth() * 2 * attacker.getWidth() * 2 + attackTarget.getWidth();
    }
}
