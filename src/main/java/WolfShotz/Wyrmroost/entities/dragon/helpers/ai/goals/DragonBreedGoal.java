package WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class DragonBreedGoal extends Goal
{
    protected final AbstractDragonEntity dragon;
    protected final int limit;
    protected final EntityPredicate predicate;
    protected AbstractDragonEntity targetMate;
    protected int spawnBabyDelay;

    public DragonBreedGoal(AbstractDragonEntity dragon, int limit)
    {
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.dragon = dragon;
        this.limit = limit;
        this.predicate = new EntityPredicate()
                .setDistance(dragon.getWidth() * 8)
                .allowInvulnerable()
                .allowFriendlyFire()
                .setLineOfSiteRequired()
                .setCustomPredicate(e -> ((AbstractDragonEntity) e).canMateWith(dragon));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (limit > 0 && dragon.breedCount >= limit) return false;
        if (!dragon.isInLove()) return false;
        if ((targetMate = getNearbyMate()) == null) return false;
        if (dragon.hasDataEntry(AbstractDragonEntity.GENDER)) return dragon.isMale() != targetMate.isMale();
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return targetMate.isAlive() && targetMate.isInLove() && dragon.isInLove() && spawnBabyDelay < 60;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        targetMate = null;
        spawnBabyDelay = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        dragon.getLookController().setLookPositionWithEntity(targetMate, 10f, dragon.getVerticalFaceSpeed());
        dragon.getNavigator().tryMoveToEntityLiving(targetMate, 1);
        if (++spawnBabyDelay >= 60 && dragon.getDistance(targetMate) < dragon.getWidth() * 2)
            spawnBaby();
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    @Nullable
    protected AbstractDragonEntity getNearbyMate()
    {
        List<AbstractDragonEntity> potentialMates = dragon.world.getTargettableEntitiesWithinAABB(dragon.getClass(), predicate, dragon, dragon.getBoundingBox().grow(dragon.getWidth() * 8));
        return potentialMates.stream().min(Comparator.comparingDouble(dragon::getDistanceSq)).orElse(null);
    }

    /**
     * Spawns an egg item at the dragons location when bred.
     * Forge breed events are taking in <code>AgeableEntity</code> as a param, thus, they cannot be posted.
     * Mod Makers: Feel free to PR a workaround if you need this!
     * todo in 1.16: with the new breed methods, fix this
     */
    public void spawnBaby()
    {
        dragon.breedCount++;
        dragon.createChild(targetMate);
        ServerPlayerEntity serverplayerentity = dragon.getLoveCause();

        if (serverplayerentity == null && targetMate.getLoveCause() != null)
            serverplayerentity = targetMate.getLoveCause();

        if (serverplayerentity != null) serverplayerentity.addStat(Stats.ANIMALS_BRED);

        dragon.setGrowingAge(6000);
        targetMate.setGrowingAge(6000);
        dragon.resetInLove();
        targetMate.resetInLove();
        if (dragon.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))
            dragon.world.addEntity(new ExperienceOrbEntity(dragon.world, dragon.getPosX(), dragon.getPosY(), dragon.getPosZ(), dragon.getRNG().nextInt(7) + 1));
    }
}
