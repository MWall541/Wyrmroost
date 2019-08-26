package WolfShotz.Wyrmroost.content.entities.owdrake.goals;

import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.util.NetworkUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class DrakeAttackGoal extends MeleeAttackGoal
{
    private final OWDrakeEntity drake;
    
    public DrakeAttackGoal(OWDrakeEntity drake) {
        super(drake, 1d, true);
        this.drake = drake;
    }
    
    @Override
    public void tick() { if (!drake.hasActiveAnimation()) super.tick(); }
    
    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && this.attackTick <= 0 && !drake.isRidingOrBeingRiddenBy(enemy) && drake.getAnimation() != OWDrakeEntity.HORN_ATTACK_ANIMATION)
            NetworkUtils.sendAnimationPacket(drake, OWDrakeEntity.HORN_ATTACK_ANIMATION);
    }
}
