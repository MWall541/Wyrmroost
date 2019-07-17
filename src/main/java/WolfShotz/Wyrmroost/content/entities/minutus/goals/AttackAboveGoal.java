package WolfShotz.Wyrmroost.content.entities.minutus.goals;

import WolfShotz.Wyrmroost.content.entities.minutus.MinutusEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AttackAboveGoal extends Goal
{
    private final Predicate<Entity> predicateFilter =
            filter -> filter instanceof FishingBobberEntity ||
                              (filter instanceof LivingEntity && filter.getSize(filter.getPose()).width < 0.9f && filter.getSize(filter.getPose()).height < 0.9f);
    private MinutusEntity minutus;
    private Entity entity;

    public AttackAboveGoal(MinutusEntity entity) {
        this.minutus = entity;
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() { return minutus.isBurrowed() && hasValidEntityAbove(); }

    @Override
    public boolean shouldContinueExecuting() { return false; }

    @Override
    public void startExecuting() {
        if (entity instanceof FishingBobberEntity) {
            entity.remove();
            minutus.setBurrowed(false);
            minutus.setMotion(0, 0.8, 0);
        }
        else minutus.attackEntityAsMob(entity);
    }

    private boolean hasValidEntityAbove() {
        AxisAlignedBB aabb = minutus.getBoundingBox().expand(0, 2, 0).grow(0.5, 0, 0.5);
        List<Entity> entities = minutus.world.getEntitiesInAABBexcluding(minutus, aabb, predicateFilter);
        if (entities.isEmpty()) return false;
        Optional<Entity> closest = entities.stream().min((entity1, entity2) -> Float.compare(entity1.getDistance(minutus), entity2.getDistance(minutus)));
        entity = closest.get();
        return true;
    }
}
