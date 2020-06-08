package WolfShotz.Wyrmroost.entities.dragonegg;

import net.minecraft.entity.EntitySize;

import java.util.function.Predicate;

public class DragonEggProperties
{
    private final float width, height;
    private final int hatchTime;
    private Predicate<DragonEggEntity> conditions = e -> true;

    /**
     * @param width     Width of the egg (x size)
     * @param height    Height of the egg (y size)
     * @param hatchTime the hatch time, in game ticks
     */
    public DragonEggProperties(float width, float height, int hatchTime)
    {
        this.width = width;
        this.height = height;
        this.hatchTime = hatchTime;
    }

    /**
     * Get the size of the egg
     */
    public EntitySize getSize() { return EntitySize.flexible(width, height); }

    /**
     * Get the hatch time of the egg
     */
    public int getHatchTime() { return hatchTime; }

    /**
     * Gets the growth time for the dragon
     * This is typically just doubled the hatch time as a negative value (for some reaon non adults are defined as non-positives)
     *
     * @return
     */
    public int getGrowthTime() { return -(hatchTime * 2); }

    /**
     * Get the conditions the egg has to be under to continue hatching
     */
    public Predicate<DragonEggEntity> getConditions() { return conditions; }

    /**
     * Set custom conditions this egg has to be under to hatch
     * Default: none. always return true.
     */
    public DragonEggProperties setConditions(Predicate<DragonEggEntity> conditions)
    {
        this.conditions = conditions;

        return this;
    }
}
