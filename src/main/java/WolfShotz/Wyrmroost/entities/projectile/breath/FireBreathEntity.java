package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
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
    public void tick()
    {
        super.tick();
        if (isInWater())
        {
            if (rand.nextInt(3) == 0) playSound(SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, 1, 1);
            for (int i = 0; i < 25; i++)
                world.addParticle(ParticleTypes.SMOKE, getPosX(), getPosY(), getPosZ(), Mafs.nextPseudoDouble(rand) * 0.2f, rand.nextDouble() * 0.1f, Mafs.nextPseudoDouble(rand) * 0.2f);
            remove();
        }
    }

    @Override
    public void onEntityImpact(LivingEntity entity)
    {
        if (!world.isRemote && entity != shootingEntity && !entity.isImmuneToFire())
        {
            entity.setFire(8);
            entity.attackEntityFrom(DamageSource.IN_FIRE, (float) shootingEntity.getAttribute(AbstractDragonEntity.PROJECTILE_DAMAGE).getValue());
        }
    }
}
