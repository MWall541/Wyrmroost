package com.github.wolfshotz.wyrmroost.entities.projectile;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WindGustEntity extends DragonProjectileEntity
{
    public WindGustEntity(EntityType<? extends DragonProjectileEntity> type, World level)
    {
        super(type, level);
    }

    public WindGustEntity(TameableDragonEntity shooter, Vector3d position, Vector3d acceleration)
    {
        super(WREntities.WIND_GUST.get(), shooter, position, acceleration);
        life = 20;
    }

    public WindGustEntity(TameableDragonEntity shooter)
    {
        this(shooter, shooter.getLookAngle().add(shooter.position().add(0, -1.5, 0)), shooter.getLookAngle());
    }

    @Override
    public void tick()
    {
        super.tick();

        if (level.isClientSide)
        {
            double multiplier = Math.min(tickCount / 5d, 4d);
            Vector3d motion = getDeltaMovement().reverse().multiply(0.1, 0.1, 0.1);
            for (int i = 0; i < 30; i++)
            {
                Vector3d vec3d = position().add(getDeltaMovement()).add(Mafs.nextDouble(random) * multiplier, Mafs.nextDouble(random) * multiplier, Mafs.nextDouble(random) * multiplier);
                double xMot = motion.x + Mafs.nextDouble(random) * 0.1;
                double yMot = motion.y + Mafs.nextDouble(random) * 0.1;
                double zMot = motion.z + Mafs.nextDouble(random) * 0.1;
                level.addParticle(ParticleTypes.CLOUD, vec3d.x, vec3d.y, vec3d.z, xMot, yMot, zMot);
            }
        }
    }

    @Override
    public void onEntityImpact(Entity entity)
    {
        if (!level.isClientSide)
        {
            entity.push(acceleration.x() * 5, 1 + acceleration.y() * 3, acceleration.z() * 5);
            entity.hurt(getDamageSource("windGust"), 3);
            if (entity instanceof ServerPlayerEntity)
                ((ServerWorld) level).getChunkSource().broadcastAndSend(entity, new SEntityVelocityPacket(entity));
        }
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
        final int PARTICLE_COUNT = 75;
        BlockParticleData blockParticle = new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(pos));
        pos = pos.relative(direction);
        if (level.isClientSide)
        {
            for (int i = 0; i < PARTICLE_COUNT; i++)
            {
                Vector3d motion = new Vector3d(1, 1, 0);
                if (direction.getAxis().getPlane() == Direction.Plane.VERTICAL) motion = motion.yRot(0.5f * Mafs.PI);
                else motion = motion.yRot(direction.toYRot() / 180f * Mafs.PI);
                motion = motion.multiply(Mafs.nextDouble(random) * 0.8, Mafs.nextDouble(random) * 0.8, Mafs.nextDouble(random) * 0.8);
                level.addParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), motion.x, motion.y, motion.z);
                level.addParticle(blockParticle, pos.getX(), pos.getY(), pos.getZ(), motion.x * 10, motion.y, motion.z * 10);
            }
        }
        else
        {
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(1), this::canImpactEntity))
            {
                double angle = Mafs.getAngle(getX(), getZ(), e.getX(), e.getZ()) * Math.PI / 180;
                e.push(2 * -Math.cos(angle), 0.5d, 2 * -Math.sin(angle));
            }
        }

        remove();
    }

    @Override
    protected double getAccelerationOffset() { return 0; }

    @Override
    protected float getMotionFactor() { return 1.5f; }

    @Override
    protected EffectType getEffectType() { return EffectType.COLLIDING; }
}
