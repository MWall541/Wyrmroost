package com.github.wolfshotz.wyrmroost.entities.projectile;


import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class DragonProjectileEntity extends Entity implements IEntityAdditionalSpawnData
{
    @Nullable // Potentially if the dragon is unloaded, or is not synced yet.
    public TameableDragonEntity shooter;
    public Vector3d acceleration;
    public float growthRate = 1f;
    public int life;
    public boolean hasCollided;

    protected DragonProjectileEntity(EntityType<?> type, World level)
    {
        super(type, level);
    }

    public DragonProjectileEntity(EntityType<? extends DragonProjectileEntity> type, TameableDragonEntity shooter, Vector3d position, Vector3d direction)
    {
        super(type, shooter.level);

        direction = direction.add(random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset());
        double length = direction.length();
        this.acceleration = new Vector3d(direction.x / length * getMotionFactor(), direction.y / length * getMotionFactor(), direction.z / length * getMotionFactor());

        this.shooter = shooter;
        this.life = 50;

        setDeltaMovement(acceleration);
        position = position.add(getDeltaMovement()).subtract(0, getBbHeight() / 2, 0);

        moveTo(position.x, position.y, position.z, yRot, xRot);
    }

    @Override
    public void tick()
    {
        if ((!level.isClientSide && (!shooter.isAlive() || tickCount > life || tickCount > getMaxLife())) || !level.hasChunkAt(blockPosition()))
        {
            remove();
            return;
        }

        super.tick();
        if (growthRate != 1) refreshDimensions();

        switch (getEffectType())
        {
            case RAYTRACE:
            {
                RayTraceResult rayTrace = ProjectileHelper.getHitResult(this, this::canImpactEntity);
                if (rayTrace.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, rayTrace))
                    hit(rayTrace);
                break;
            }
            case COLLIDING:
            {
                AxisAlignedBB box = getBoundingBox().inflate(0.05);
                for (Entity entity : level.getEntities(this, box, this::canImpactEntity))
                    onEntityImpact(entity);

                Vector3d position = position();
                Vector3d end = position.add(getDeltaMovement());
                BlockRayTraceResult rtr = level.clip(new RayTraceContext(position, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                if (rtr.getType() != RayTraceResult.Type.MISS) onBlockImpact(rtr.getBlockPos(), rtr.getDirection());
            }
            default:
                break;
        }

        Vector3d motion = getDeltaMovement();
        if (!isNoGravity()) setDeltaMovement(motion = motion.add(0, -0.05, 0));
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;

        if (isInWater())
        {
            setDeltaMovement(motion.scale(0.95f));
            for (int i = 0; i < 4; ++i)
                level.addParticle(ParticleTypes.BUBBLE, getX() * 0.25d, getY() * 0.25d, getZ() * 0.25D, motion.x, motion.y, motion.z);
        }
        absMoveTo(x, y, z);
    }

    public boolean canImpactEntity(Entity entity)
    {
        if (entity == shooter) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof LivingEntity)) return false;
        if (entity.getRootVehicle() == shooter) return false;
        if (entity.isSpectator() || !entity.isPickable() || entity.noPhysics) return false;
        return shooter != null && !entity.isAlliedTo(shooter);
    }

    public void hit(RayTraceResult result)
    {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.BLOCK)
        {
            final BlockRayTraceResult brtr = (BlockRayTraceResult) result;
            onBlockImpact(brtr.getBlockPos(), brtr.getDirection());
        }
        else if (type == RayTraceResult.Type.ENTITY) onEntityImpact(((EntityRayTraceResult) result).getEntity());
    }

    public void onEntityImpact(Entity entity)
    {
    }

    public void onBlockImpact(BlockPos pos, Direction direction)
    {
    }

    @Override
    public void setDeltaMovement(Vector3d motionIn)
    {
        super.setDeltaMovement(motionIn);
        ProjectileHelper.rotateTowardsMovement(this, 1);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        if (growthRate == 1) return getType().getDimensions();
        float size = Math.min(getBbWidth() * growthRate, 2.25f);
        return EntitySize.scalable(size, size);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = getBoundingBox().getSize() * 4;
        if (Double.isNaN(d0)) d0 = 4;
        d0 *= 64;
        return distance < d0 * d0;
    }

    public DamageSource getDamageSource(String name)
    {
        return new IndirectEntityDamageSource(name, this, shooter).setProjectile().setScalesWithDifficulty();
    }

    protected EffectType getEffectType()
    {
        return EffectType.NONE;
    }

    protected float getMotionFactor()
    {
        return 0.95f;
    }

    protected double getAccelerationOffset()
    {
        return 0.1;
    }

    protected int getMaxLife()
    {
        return 150;
    }

    @Override
    public boolean isNoGravity()
    {
        return true;
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public float getBrightness()
    {
        return 1f;
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    public float getPickRadius()
    {
        return getBbWidth();
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override // Does not Serialize
    protected void readAdditionalSaveData(CompoundNBT compound)
    {
    }

    @Override // Does not Serialize
    protected void addAdditionalSaveData(CompoundNBT compound)
    {
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buf)
    {
        buf.writeInt(shooter.getId());
        buf.writeFloat(growthRate);
    }

    @Override
    public void readSpawnData(PacketBuffer buf)
    {
        this.shooter = (TameableDragonEntity) level.getEntity(buf.readInt());
        this.growthRate = buf.readFloat();
    }

    protected enum EffectType
    {
        NONE,
        RAYTRACE,
        COLLIDING
    }
}
