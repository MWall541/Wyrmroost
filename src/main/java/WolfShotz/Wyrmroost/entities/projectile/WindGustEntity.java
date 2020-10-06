package WolfShotz.Wyrmroost.entities.projectile;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class WindGustEntity extends DragonProjectileEntity
{
    public WindGustEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    public WindGustEntity(EntityType<? extends DragonProjectileEntity> type, AbstractDragonEntity shooter)
    {
        super(type, shooter);
    }


}
