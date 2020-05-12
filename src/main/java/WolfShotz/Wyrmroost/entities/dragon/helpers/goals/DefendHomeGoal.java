package WolfShotz.Wyrmroost.entities.dragon.helpers.goals;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Basically another target goal that targets things within the home
 */
public class DefendHomeGoal extends TargetGoal
{
    private static final Predicate<LivingEntity> FILTER = e -> e instanceof IMob && !e.getName().getUnformattedComponentText().equalsIgnoreCase("Ignore Me");

    private final AbstractDragonEntity defender;
    private final Predicate<LivingEntity> additionalFilters;

    public DefendHomeGoal(AbstractDragonEntity defender, Predicate<LivingEntity> additionalFilters)
    {
        super(defender, false, false);
        this.defender = defender;
        this.additionalFilters = additionalFilters;
        setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public DefendHomeGoal(AbstractDragonEntity defender) { this(defender, e -> true); }

    @Override
    public boolean shouldExecute()
    {
        if (!defender.getHomePos().isPresent()) return false;
        return (target = findPotentialTarget()) != null;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        defender.setAttackTarget(target);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return defender.getHomePos().map(pos -> target.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) > getTargetDistance() && super.shouldContinueExecuting()).orElse(true);
    }

    @Override
    protected double getTargetDistance() { return WRConfig.homeRadius; }

    public LivingEntity findPotentialTarget()
    {
        return defender.world.func_225318_b(
                LivingEntity.class,
                new EntityPredicate().setCustomPredicate(FILTER.and(additionalFilters)),
                defender,
                defender.getPosX(),
                defender.getPosY() + defender.getEyeHeight(),
                defender.getPosZ(),
                new AxisAlignedBB(defender.getHomePos().get()).grow(WRConfig.homeRadius)
        );
    }
}
