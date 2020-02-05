package WolfShotz.Wyrmroost.content.entities.dragon.owdrake.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class DrakeTargetGoal extends NearestAttackableTargetGoal<LivingEntity>
{
    private static final BiFunction<OWDrakeEntity, Double, EntityPredicate> TAMED_TARGETS = (drake, distance) -> new EntityPredicate().setDistance(distance).setCustomPredicate(getTargetPredicate(drake));

    private final OWDrakeEntity drake;
    
    public DrakeTargetGoal(OWDrakeEntity drake)
    {
        super(drake, LivingEntity.class, true, false);
        this.drake = drake;
        this.targetEntitySelector = TAMED_TARGETS.apply(drake, getTargetDistance());
    }
    
    @Override
    public boolean shouldExecute()
    {
        return super.shouldExecute() && (!drake.isTamed() || drake.getHomePos().isPresent()) && !drake.isSleeping() && !drake.isChild();
    }
    
    @Override
    public void startExecuting()
    {
        if (!drake.isTamed() && drake.getAnimation() != OWDrakeEntity.ROAR_ANIMATION)
            NetworkUtils.sendAnimationPacket(drake, OWDrakeEntity.ROAR_ANIMATION);
        drake.getLookController().setLookPositionWithEntity(nearestTarget, 180, 30);

        super.startExecuting();
    }

    private static Predicate<LivingEntity> getTargetPredicate(OWDrakeEntity drake)
    {
        return e -> {
            if (!drake.isTamed()) return e instanceof PlayerEntity && !e.isSpectator() && ((PlayerEntity) e).isCreative();
            else if (drake.getHomePos().isPresent()) return e instanceof MonsterEntity || e instanceof SlimeEntity;
            return false;
        };
    }
}
