package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.EntitySize;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class DragonEggProperties
{
    public final float WIDTH, HEIGHT;
    public final int HATCH_TIME;
    public final ResourceLocation TEXTURE;
    public final Predicate<DragonEggEntity> CONDITIONS;
    
    public DragonEggProperties(float width, float height, int hatchTime) {
        this(width, height, hatchTime, null, e -> true);
    }
    
    /**
     *
     * @param width Width of the egg (x size)
     * @param height Height
     * @param hatchTime the hatch time
     * @param customTexture custom texture (pass null if we dont need one)
     * @param conditions custom conditions the egg needs to be in to hatch
     */
    public DragonEggProperties(float width, float height, int hatchTime, @Nullable ResourceLocation customTexture, Predicate<DragonEggEntity> conditions) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.HATCH_TIME = hatchTime;
        this.TEXTURE = customTexture == null ? ModUtils.location("textures/entity/dragonegg/default.png") : customTexture;
        this.CONDITIONS = conditions;
    }
    
    public EntitySize getSize() { return EntitySize.flexible(WIDTH, HEIGHT); }
}
