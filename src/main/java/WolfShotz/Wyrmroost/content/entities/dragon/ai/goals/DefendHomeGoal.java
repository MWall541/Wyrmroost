package WolfShotz.Wyrmroost.content.entities.dragon.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.*;
import WolfShotz.Wyrmroost.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.*;
import net.minecraft.util.math.*;

import java.util.*;
import java.util.function.*;

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
    protected double getTargetDistance() { return ConfigData.homeRadius; }

    public LivingEntity findPotentialTarget()
    {
        return defender.world.func_225318_b(
                LivingEntity.class,
                new EntityPredicate().setCustomPredicate(FILTER.and(additionalFilters)),
                defender,
                defender.posX,
                defender.posY + defender.getEyeHeight(),
                defender.posZ,
                new AxisAlignedBB(defender.getHomePos().get()).grow(ConfigData.homeRadius)
        );
    }
}
