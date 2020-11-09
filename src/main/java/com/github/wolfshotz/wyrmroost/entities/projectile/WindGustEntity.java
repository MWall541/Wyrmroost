package com.github.wolfshotz.wyrmroost.entities.projectile;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WindGustEntity extends DragonProjectileEntity
{
    public WindGustEntity(EntityType<? extends DragonProjectileEntity> type, World world)
    {
        super(type, world);
    }

    public WindGustEntity(AbstractDragonEntity shooter, Vec3d position, Vec3d acceleration)
    {
        super(WREntities.WIND_GUST.get(), shooter, position, acceleration);
        life = 20;
    }

    public WindGustEntity(AbstractDragonEntity shooter)
    {
        this(shooter, shooter.getLookVec().add(shooter.getPositionVec().add(0, -1.5, 0)), shooter.getLookVec());
    }

    @Override
    public void tick()
    {
        super.tick();

        if (world.isRemote)
        {
            double multiplier = Math.min(ticksExisted / 5d, 4d);
            Vec3d motion = getMotion().inverse().mul(0.1, 0.1, 0.1);
            for (int i = 0; i < 30; i++)
            {
                Vec3d vec3d = getPositionVec().add(getMotion()).add(Mafs.nextDouble(rand) * multiplier, Mafs.nextDouble(rand) * multiplier, Mafs.nextDouble(rand) * multiplier);
                double xMot = motion.x + Mafs.nextDouble(rand) * 0.1;
                double yMot = motion.y + Mafs.nextDouble(rand) * 0.1;
                double zMot = motion.z + Mafs.nextDouble(rand) * 0.1;
                world.addParticle(ParticleTypes.CLOUD, vec3d.x, vec3d.y, vec3d.z, xMot, yMot, zMot);
            }
        }
    }

    @Override
    public void onEntityImpact(Entity entity)
    {
        if (!world.isRemote)
        {
            entity.addVelocity(acceleration.getX() * 5, 1 + acceleration.getY() * 3, acceleration.getZ() * 5);
            entity.attackEntityFrom(getDamageSource("windGust"), 3);
            if (entity instanceof ServerPlayerEntity)
                ((ServerWorld) world).getChunkProvider().sendToTrackingAndSelf(entity, new SEntityVelocityPacket(entity));
        }
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
        final int PARTICLE_COUNT = 75;
        BlockParticleData blockParticle = new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos));
        pos = pos.offset(direction);
        if (world.isRemote)
        {
            for (int i = 0; i < PARTICLE_COUNT; i++)
            {
                Vec3d motion = new Vec3d(1, 1, 0);
                if (direction.getAxis().getPlane() == Direction.Plane.VERTICAL) motion = motion.rotatePitch(0.5f * Mafs.PI);
                else motion = motion.rotateYaw(direction.getHorizontalAngle() / 180f * Mafs.PI);
                motion = motion.mul(Mafs.nextDouble(rand) * 0.8, Mafs.nextDouble(rand) * 0.8, Mafs.nextDouble(rand) * 0.8);
                world.addParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), motion.x, motion.y, motion.z);
                world.addParticle(blockParticle, pos.getX(), pos.getY(), pos.getZ(), motion.x * 10, motion.y, motion.z * 10);
            }
        }
        else
        {
            for (LivingEntity e : world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(1), e -> !shooter.isOnSameTeam(e)))
            {
                double angle = Mafs.getAngle(getPosX(), getPosZ(), e.getPosX(), e.getPosZ()) * Math.PI / 180;
                e.addVelocity(2 * -Math.cos(angle), 0.5d, 2 * -Math.sin(angle));
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
