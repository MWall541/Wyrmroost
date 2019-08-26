package WolfShotz.Wyrmroost.content.entities.owdrake.goals;

import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.util.NetworkUtils;
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
    public void startExecuting() {
        if (drake.getAnimation() != OWDrakeEntity.ROAR_ANIMATION) NetworkUtils.sendAnimationPacket(drake, OWDrakeEntity.ROAR_ANIMATION);
        drake.getLookController().setLookPositionWithEntity(nearestTarget, 30, 30);
        drake.prevRotationYaw = drake.rotationYaw = drake.rotationYawHead;
        drake.setRotation(drake.rotationYaw, drake.rotationPitch);
        drake.renderYawOffset = drake.rotationYaw;
    
        super.startExecuting();
    }
}
