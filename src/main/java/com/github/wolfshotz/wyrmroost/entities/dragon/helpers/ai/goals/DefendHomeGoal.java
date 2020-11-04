package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Basically another target goal that targets things within the home
 */
public class DefendHomeGoal extends TargetGoal
{
    private static final Predicate<LivingEntity> FILTER = e -> e instanceof IMob && !(e instanceof CreeperEntity) && !e.getName().getUnformattedComponentText().equalsIgnoreCase("Ignore Me");

    private final AbstractDragonEntity defender;
    private final EntityPredicate predicate;

    public DefendHomeGoal(AbstractDragonEntity defender, Predicate<LivingEntity> additionalFilters)
    {
        super(defender, false, false);
        this.defender = defender;
        this.predicate = new EntityPredicate().setCustomPredicate(FILTER.and(additionalFilters));
        setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public DefendHomeGoal(AbstractDragonEntity defender) { this(defender, e -> true); }

    @Override
    public boolean shouldExecute()
    {
        if (defender.getHealth() <= defender.getMaxHealth() * 0.25) return false;
        if (!defender.getHomePos().isPresent()) return false;
        return defender.getRNG().nextDouble() < 0.2 && (target = findPotentialTarget()) != null;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();

        // alert others!
        for (MobEntity mob : defender.world.getEntitiesWithinAABB(MobEntity.class, defender.getBoundingBox().grow(WRConfig.homeRadius), defender::isOnSameTeam))
            mob.setAttackTarget(target);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return defender.isWithinHomeDistanceFromPosition(target.getPosition()) && super.shouldContinueExecuting();
    }

    @Override
    protected double getTargetDistance() { return defender.getMaximumHomeDistance(); }

    public LivingEntity findPotentialTarget()
    {
        return defender.world.func_225318_b(LivingEntity.class,
                predicate,
                defender,
                defender.getPosX(),
                defender.getPosY() + defender.getEyeHeight(),
                defender.getPosZ(),
                new AxisAlignedBB(defender.getHomePosition()).grow(WRConfig.homeRadius));
    }
}
