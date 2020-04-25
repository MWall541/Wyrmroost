package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class NonTamedTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T>
{
    private final AbstractDragonEntity dragon;
    private final boolean asChild;

    public NonTamedTargetGoal(AbstractDragonEntity goalOwnerIn, Class<T> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, boolean asChild, @Nullable Predicate<LivingEntity> targetPredicate)
    {
        super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, targetPredicate);
        this.asChild = asChild;
        this.dragon = goalOwnerIn;
    }

    public NonTamedTargetGoal(AbstractDragonEntity goalOwnerIn, Class<T> targetClassIn, boolean checkSight, boolean nearbyOnlyIn, boolean asChild)
    {
        super(goalOwnerIn, targetClassIn, checkSight, nearbyOnlyIn);
        this.dragon = goalOwnerIn;
        this.asChild = asChild;
        this.targetEntitySelector = new EntityPredicate().setDistance(getTargetDistance()).setCustomPredicate(getTargets());
    }

    public boolean shouldExecute()
    {
        return (!dragon.isTamed() || dragon.getHomePos().isPresent())
                && (asChild || !dragon.isChild())
                && super.shouldExecute();
    }

    public boolean shouldContinueExecuting()
    {
        return targetEntitySelector != null?
                targetEntitySelector.canTarget(goalOwner, nearestTarget)
                : super.shouldContinueExecuting();
    }

    private Predicate<LivingEntity> getTargets()
    {
        return e -> dragon.isTamed()? e instanceof IMob : e instanceof PlayerEntity && EntityPredicates.CAN_AI_TARGET.test(e);
    }
}
