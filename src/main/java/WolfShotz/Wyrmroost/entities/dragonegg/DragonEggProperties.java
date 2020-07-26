package WolfShotz.Wyrmroost.entities.dragonegg;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;

import java.util.Map;
import java.util.function.Predicate;

public class DragonEggProperties
{
    public static final Map<EntityType<?>, DragonEggProperties> MAP = Maps.newHashMap();

    private final EntitySize size;
    private final int hatchTime;
    private Predicate<DragonEggEntity> conditions = e -> true;

    /**
     * @param width     Width of the egg (x size)
     * @param height    Height of the egg (y size)
     * @param hatchTime the hatch time, in game ticks
     */
    public DragonEggProperties(float width, float height, int hatchTime)
    {
        this.size = EntitySize.fixed(width, height);
        this.hatchTime = hatchTime;
    }

    /**
     * Get the size of the egg
     */
    public EntitySize getSize() { return size; }

    /**
     * Get the hatch time of the egg
     */
    public int getHatchTime() { return hatchTime; }

    /**
     * Gets the growth time for the dragon
     * This is typically just doubled the hatch time as a negative value (for some reason non adults are defined as non-positives)
     */
    public int getGrowthTime() { return -hatchTime * 2; }

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

    public static DragonEggProperties get(EntityType<?> type)
    {
        DragonEggProperties props = MAP.get(type);
        if (props == null)
            throw new NullPointerException(String.format("Missing Egg Properties for key: %s, Wolf did a hickup go scream at him", type.getName().getUnformattedComponentText()));
        return props;
    }
}
