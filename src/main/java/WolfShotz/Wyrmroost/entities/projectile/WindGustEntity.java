package WolfShotz.Wyrmroost.entities.projectile;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WindGustEntity extends DragonProjectileEntity
{
    public WindGustEntity(EntityType<? extends DragonProjectileEntity> type, World world)
    {
        super(type, world);
    }

    public WindGustEntity(AbstractDragonEntity shooter, Vector3d position, Vector3d acceleration)
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

        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(shooter, getBoundingBox()))
        {
            if (entity instanceof LivingEntity)
            {
                if (shooter.isOnSameTeam(entity)) continue;
                entity.addVelocity(accelerationX * 5, 1 + accelerationY * 3, accelerationZ * 5);
                entity.attackEntityFrom(new IndirectEntityDamageSource("windGust", this, shooter), 3);
                if (entity instanceof ServerPlayerEntity) ((ServerWorld) world).getChunkProvider().sendToTrackingAndSelf(entity, new SEntityVelocityPacket(entity));
            }
        }

        if (world.isRemote)
        {
            double multiplier = Math.min(ticksExisted / 5d, 4d);
            Vector3d motion = getMotion().inverse().mul(0.1, 0.1, 0.1);
            for (int i = 0; i < 30; i++)
            {
                Vector3d vec3d = getPositionVec().add(getMotion()).add(Mafs.nextDouble(rand) * multiplier, Mafs.nextDouble(rand) * multiplier, Mafs.nextDouble(rand) * multiplier);
                double xMot = motion.x + Mafs.nextDouble(rand) * 0.1;
                double yMot = motion.y + Mafs.nextDouble(rand) * 0.1;
                double zMot = motion.z + Mafs.nextDouble(rand) * 0.1;
                world.addParticle(ParticleTypes.CLOUD, vec3d.x, vec3d.y, vec3d.z, xMot, yMot, zMot);
            }
        }
    }

    @Override
    public void onBlockImpact(BlockRayTraceResult result)
    {
        final int PARTICLE_COUNT = 75;
        BlockPos pos = result.getPos();
        Direction face = result.getFace();
        BlockParticleData blockParticle = new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos));
        pos = pos.offset(face);
        if (world.isRemote)
        {
            for (int i = 0; i < PARTICLE_COUNT; i++)
            {
                Vector3d motion = new Vector3d(1, 1, 0);
                if (face.getAxis().getPlane() == Direction.Plane.VERTICAL) motion = motion.rotatePitch(0.5f * Mafs.PI);
                else motion = motion.rotateYaw(face.getHorizontalAngle() / 180f * Mafs.PI);
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
}
