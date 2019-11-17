package WolfShotz.Wyrmroost.content.entities.dragon.owdrake.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import net.minecraft.entity.ai.goal.NonTamedTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

public class DrakeTargetGoal extends NonTamedTargetGoal
{
    private final OWDrakeEntity drake;
    
    public DrakeTargetGoal(OWDrakeEntity drake) {
        super(drake, PlayerEntity.class, true, EntityPredicates.CAN_AI_TARGET);
        this.drake = drake;
    }
    
    @Override
    public boolean shouldExecute() { return super.shouldExecute() && !drake.isSleeping() && !drake.isChild(); }
    
    @Override
    public void startExecuting() {
        if (drake.getAnimation() != OWDrakeEntity.ROAR_ANIMATION) NetworkUtils.sendAnimationPacket(drake, OWDrakeEntity.ROAR_ANIMATION);
        drake.getLookController().setLookPositionWithEntity(nearestTarget, 180, 30);
    
        super.startExecuting();
    }
}
