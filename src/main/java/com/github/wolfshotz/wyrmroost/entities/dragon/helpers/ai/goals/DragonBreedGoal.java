package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;

public class DragonBreedGoal extends Goal
{
    protected final TameableDragonEntity dragon;
    protected final EntityPredicate predicate;
    protected TameableDragonEntity targetMate;
    protected int spawnBabyDelay;

    public DragonBreedGoal(TameableDragonEntity dragon)
    {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.dragon = dragon;
        this.predicate = new EntityPredicate()
                .range(dragon.getBbWidth() * 8)
                .allowInvulnerable()
                .allowSameTeam()
                .allowUnseeable()
                .selector(e -> ((AnimalEntity) e).canMate(dragon));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse()
    {
        if (!dragon.isInLove()) return false;
        int breedLimit = WRConfig.getBreedLimitFor(dragon.getType());
        if (breedLimit > 0 && dragon.breedCount >= breedLimit)
        {
            dragon.resetLove();
            return false;
        }
        return (targetMate = getNearbyMate()) != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse()
    {
        return targetMate.isAlive() && targetMate.isInLove() && dragon.isInLove() && spawnBabyDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop()
    {
        targetMate = null;
        spawnBabyDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        dragon.getLookControl().setLookAt(targetMate, 10f, dragon.getYawRotationSpeed());
        dragon.getNavigation().moveTo(targetMate, 1);
        if (++spawnBabyDelay >= 60 && dragon.distanceTo(targetMate) < dragon.getBbWidth() * 2)
            dragon.spawnChildFromBreeding((ServerWorld) dragon.level, targetMate);
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    @Nullable
    protected TameableDragonEntity getNearbyMate()
    {
        return dragon.level.getNearbyEntities(dragon.getClass(), predicate, dragon, dragon.getBoundingBox().inflate(dragon.getBbWidth() * 8))
                .stream()
                .min(Comparator.comparingDouble(dragon::distanceToSqr)).orElse(null);
    }
}
