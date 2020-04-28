package WolfShotz.Wyrmroost.content.entities.dragon.owdrake.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class DrakeAttackGoal extends MeleeAttackGoal
{
    private final OWDrakeEntity drake;
    
    public DrakeAttackGoal(OWDrakeEntity drake)
    {
        super(drake, 1d, true);
        this.drake = drake;
    }

    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !drake.isBeingRidden(); }

    @Override
    public boolean shouldContinueExecuting() { return !drake.isBeingRidden() && super.shouldContinueExecuting(); }

    @Override
    public void tick()
    {
        if (drake.getAnimation() != OWDrakeEntity.ROAR_ANIMATION) super.tick();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr)
    {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && attackTick <= 0 && !drake.isRidingOrBeingRiddenBy(enemy) && drake.getAnimation() != OWDrakeEntity.HORN_ATTACK_ANIMATION)
        {
            NetworkUtils.sendAnimationPacket(drake, OWDrakeEntity.HORN_ATTACK_ANIMATION);
            attackTick = 10;
        }
    }
}
