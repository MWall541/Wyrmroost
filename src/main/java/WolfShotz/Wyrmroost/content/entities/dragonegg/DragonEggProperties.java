package WolfShotz.Wyrmroost.content.entities.dragonegg;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.EntitySize;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

public class DragonEggProperties
{
    private final float WIDTH, HEIGHT;
    private final int HATCH_TIME;
    private ResourceLocation texture = ModUtils.resource("textures/entity/dragonegg/default.png");
    private Predicate<DragonEggEntity> conditions = e -> true;
    
    /**
     * @param width Width of the egg (x size)
     * @param height Height of the egg (y size)
     * @param hatchTime the hatch time, in game ticks
     */
    public DragonEggProperties(float width, float height, int hatchTime) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.HATCH_TIME = hatchTime;
    }
    
    /**
     * Set a custom egg texture as opposed to the default
     */
    public DragonEggProperties setCustomTexture(ResourceLocation customTexture) {
        this.texture = customTexture;
        
        return this;
    }
    
    /**
     * Set custom conditions this egg has to be under to hatch
     * Default: none. always return true.
     */
    public DragonEggProperties setConditions(Predicate<DragonEggEntity> conditions) {
        this.conditions = conditions;
        
        return this;
    }
    
    /**
     * Get the size of the egg
     */
    public EntitySize getSize() { return EntitySize.flexible(WIDTH, HEIGHT); }
    
    /**
     * Get the hatch time of the egg
     */
    public int getHatchTime() { return HATCH_TIME; }
    
    /**
     * Get the conditions the egg has to be under to continue hatching
     */
    public Predicate<DragonEggEntity> getConditions() { return conditions; }
    
    /**
     * Get the texture this egg renders
     * Default: "textures/entity/dragonegg/default.png"
     */
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getEggTexture() { return texture; }
}
