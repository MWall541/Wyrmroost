package WolfShotz.Wyrmroost.entities.dragon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

/**
 * Simple Entity really, just bob and down in the same spot, and land to sleep at night. Easy.
 */
public class CoinDragonEntity extends MobEntity
{
    public CoinDragonEntity(EntityType<? extends CoinDragonEntity> type, World worldIn)
    {
        super(type, worldIn);
    }
}
