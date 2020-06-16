package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class FireBreathEntity extends BreathWeaponEntity
{
    public FireBreathEntity(EntityType<? extends DamagingProjectileEntity> type, World world)
    {
        super(type, world);
    }

    public FireBreathEntity(EntityType<? extends FireBreathEntity> type, AbstractDragonEntity shooter)
    {
        super(type, shooter);
    }

    public FireBreathEntity(AbstractDragonEntity shooter)
    {
        super(WREntities.FIRE_BREATH.get(), shooter);
    }

    @Override
    public void onEntityImpact(LivingEntity entity)
    {
        if (!world.isRemote)
        {
            entity.setFire(8);
            entity.attackEntityFrom(DamageSource.IN_FIRE, (float) shootingEntity.getAttribute(AbstractDragonEntity.PROJECTILE_DAMAGE).getValue());
        }
    }
}
