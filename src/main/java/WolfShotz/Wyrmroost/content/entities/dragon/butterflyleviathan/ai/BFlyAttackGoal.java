package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class BFlyAttackGoal extends MeleeAttackGoal
{
    private final ButterflyLeviathanEntity entity;

    public BFlyAttackGoal(ButterflyLeviathanEntity creature)
    {
        super(creature, 1, true);
        this.entity = creature;
    }

    @Override
    public boolean shouldExecute() { return !entity.isBeingRidden() && super.shouldExecute(); }

    @Override
    public boolean shouldContinueExecuting() { return !entity.isBeingRidden() && super.shouldContinueExecuting(); }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
    {
        double reach = getAttackReachSqr(enemy);
        if (attackTick <= 0)
        {
            if (distToEnemySqr <= reach)
            {
                attackTick = 20;
                attacker.swingArm(Hand.MAIN_HAND);
                attacker.attackEntityAsMob(enemy);
            }
            else if (entity.canLightningStrike()) entity.strikeTarget();
        }
    }
}
