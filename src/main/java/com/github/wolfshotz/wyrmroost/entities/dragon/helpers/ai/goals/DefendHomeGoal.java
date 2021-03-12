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
    private static final Predicate<LivingEntity> FILTER = e -> e instanceof IMob && !(e instanceof CreeperEntity) && !e.getName().asString().equalsIgnoreCase("Ignore Me");

    private final AbstractDragonEntity defender;
    private final EntityPredicate predicate;

    public DefendHomeGoal(AbstractDragonEntity defender, Predicate<LivingEntity> additionalFilters)
    {
        super(defender, false, false);
        this.defender = defender;
        this.predicate = new EntityPredicate().setPredicate(FILTER.and(additionalFilters));
        setControls(EnumSet.of(Flag.TARGET));
    }

    public DefendHomeGoal(AbstractDragonEntity defender)
    {
        this(defender, e -> true);
    }

    @Override
    public boolean canStart()
    {
        if (defender.getHealth() <= defender.getMaxHealth() * 0.25) return false;
        if (!defender.getHomePos().isPresent()) return false;
        return defender.getRandom().nextDouble() < 0.2 && (target = findPotentialTarget()) != null;
    }

    @Override
    public void start()
    {
        super.start();

        // alert others!
        for (MobEntity mob : defender.world.getEntitiesByClass(MobEntity.class, defender.getBoundingBox().expand(WRConfig.homeRadius), defender::isTeammate))
            mob.setTarget(target);
    }

    @Override
    public boolean shouldContinue()
    {
        return defender.isInWalkTargetRange(target.getBlockPos()) && super.shouldContinue();
    }

    @Override
    protected double getFollowRange()
    {
        return defender.getPositionTargetRange();
    }

    public LivingEntity findPotentialTarget()
    {
        return defender.world.getClosestEntity(LivingEntity.class,
                predicate,
                defender,
                defender.getX(),
                defender.getEyeY(),
                defender.getZ(),
                new AxisAlignedBB(defender.getPositionTarget()).expand(WRConfig.homeRadius));
    }
}
