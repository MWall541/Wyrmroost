package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.passive.TameableEntity;

/**
 * This ones a pain in the ass because everything is private...
 * <p>
 * Super {@link #shouldExecute()} does a 'is in water check', we don't want that for tameable sea creatures,
 * so we fix it with this new goal by simply removing that check.
 */
public class WaterSitGoal extends SitGoal
{
    public final TameableEntity tameable;
    public boolean isSitting;

    public WaterSitGoal(TameableEntity entityIn)
    {
        super(entityIn);
        this.tameable = entityIn;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!tameable.isTamed()) return false;
        else if (!tameable.onGround) return false;
        else
        {
            LivingEntity owner = tameable.getOwner();
            if (owner == null) return true;
            else
                return (!(tameable.getDistanceSq(owner) < 144.0D) || owner.getRevengeTarget() == null) && isSitting;
        }
    }

    /**
     * Literally because super isSitting is private. yeah.
     */
    @Override
    public void setSitting(boolean sitting)
    {
        super.setSitting(sitting);
        isSitting = sitting;
    }
}
