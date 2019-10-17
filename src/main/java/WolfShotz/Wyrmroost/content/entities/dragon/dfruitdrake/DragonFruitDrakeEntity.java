package WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class DragonFruitDrakeEntity extends AbstractDragonEntity
{
    public DragonFruitDrakeEntity(EntityType<? extends DragonFruitDrakeEntity> dragon, World world) {
        super(dragon, world);
    }
    
    @Override
    public boolean canFly() { return false; }
    
    /**
     * Array Containing all of the dragons food items
     */
    @Override
    protected Item[] getFoodItems() { return new Item[0]; }
    
    @Override
    public Animation[] getAnimations() { return new Animation[0]; }
}
