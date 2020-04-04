package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
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
    public boolean shouldExecute()
    {
        return !entity.isBeingRidden() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !entity.isBeingRidden() && super.shouldContinueExecuting();
    }

    @Override
    public void tick()
    {
        super.tick();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
    {
        double d0 = this.getAttackReachSqr(enemy);
        if (attackTick <= 0)
        {
            if (distToEnemySqr <= d0)
            {
                attackTick = 20;
                attacker.swingArm(Hand.MAIN_HAND);
                attacker.attackEntityAsMob(enemy);
            }
            else if (entity.lightningAttackCooldown <= 0 && (entity.isInWater() || entity.world.isRaining()))
                NetworkUtils.sendAnimationPacket(entity, ButterflyLeviathanEntity.ROAR_ANIMATION);
        }
    }
}
