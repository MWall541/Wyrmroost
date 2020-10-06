package WolfShotz.Wyrmroost.entities.projectile;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WindGustEntity extends DragonProjectileEntity
{
    public WindGustEntity(EntityType<? extends DragonProjectileEntity> type, World world)
    {
        super(type, world);
    }

    public WindGustEntity(AbstractDragonEntity shooter)
    {
        super(WREntities.WIND_GUST.get(), shooter, shooter.getLookVec().add(shooter.getPositionVec().add(0, -1.5, 0)));
        life = 150;
    }

    @Override
    public void tick()
    {
        super.tick();

        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(shooter, getBoundingBox()))
        {
            if (entity instanceof LivingEntity)
            {
                entity.addVelocity(accelerationX * 5, 1, accelerationZ * 5);
                if (entity instanceof ServerPlayerEntity) ((ServerWorld) world).getChunkProvider().sendToTrackingAndSelf(entity, new SEntityVelocityPacket(entity));
            }
        }

        if (world.isRemote)
        {
            Vec3d vec3d = getPositionVec().add(getMotion());
            Vec3d motion = getMotion().inverse().mul(0.1, 0.1, 0.1);
            for (int i = 0; i < 30; i++)
            {
                double xMot = motion.x + Mafs.nextDouble(rand) * 0.5;
                double yMot = motion.y + Mafs.nextDouble(rand) * 0.5;
                double zMot = motion.z + Mafs.nextDouble(rand) * 0.5;
                world.addParticle(ParticleTypes.CLOUD, vec3d.x + Mafs.nextDouble(rand) * 2, vec3d.y + 2 + Mafs.nextDouble(rand) * 2, vec3d.z + Mafs.nextDouble(rand) * 2, xMot, yMot, zMot);
            }
        }
    }

    @Override
    protected double getAccelerationOffset() { return 0; }

    @Override
    protected float getMotionFactor() { return 1.5f; }

    @Override
    protected boolean directRaytrace() { return false; }
}
