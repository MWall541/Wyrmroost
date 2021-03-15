package com.github.wolfshotz.wyrmroost.entities.projectile;


import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
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
    public AbstractDragonEntity shooter;
    public Vector3d acceleration;
    public float growthRate = 1f;
    public int life;
    public boolean hasCollided;

    protected DragonProjectileEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    public DragonProjectileEntity(EntityType<? extends DragonProjectileEntity> type, AbstractDragonEntity shooter, Vector3d position, Vector3d velocity)
    {
        super(type, shooter.world);

        velocity = velocity.add(random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset());
        double length = velocity.length();
        this.acceleration = new Vector3d(velocity.x / length * getMotionFactor(), velocity.y / length * getMotionFactor(), velocity.z / length * getMotionFactor());

        this.shooter = shooter;
        this.life = 50;

        setVelocity(getVelocity().add(acceleration));
        position = position.add(getVelocity());

        Vector3d motion = getVelocity();
        float x = (float) (motion.x - position.x);
        float y = (float) (motion.y - position.y);
        float z = (float) (motion.z - position.z);
        float planeSqrt = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) MathHelper.atan2(z, x) * 180f / Mafs.PI - 90f;
        float pitch = (float) -(MathHelper.atan2(y, planeSqrt) * 180f / Mafs.PI);

        refreshPositionAndAngles(position.x, position.y, position.z, yaw, pitch);
    }

    @Override
    public void tick()
    {
        if ((!world.isClient && (!shooter.isAlive() || age > life || age > getMaxLife())) || !world.isChunkLoaded(getBlockPos()))
        {
            remove();
            return;
        }

        super.tick();
        if (growthRate != 1) calculateDimensions();

        switch (getEffectType())
        {
            case RAYTRACE:
            {
                RayTraceResult rayTrace = ProjectileHelper.getCollision(this, this::canImpactEntity);
                if (rayTrace.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, rayTrace))
                    hit(rayTrace);
                break;
            }
            case COLLIDING:
            {
                AxisAlignedBB box = getBoundingBox().expand(0.05);
                for (Entity entity : world.getOtherEntities(this, box, this::canImpactEntity))
                    onEntityImpact(entity);

                Vector3d position = getPos();
                Vector3d end = position.add(getVelocity());
                BlockRayTraceResult rtr = world.raycast(new RayTraceContext(position, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                if (rtr.getType() != RayTraceResult.Type.MISS) onBlockImpact(rtr.getBlockPos(), rtr.getSide());
            }
            default:
                break;
        }

        Vector3d motion = getVelocity();
        if (!hasNoGravity()) setVelocity(motion = motion.add(0, -0.05, 0));
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;

        if (isTouchingWater())
        {
            setVelocity(motion.multiply(0.95f));
            for (int i = 0; i < 4; ++i)
                world.addParticle(ParticleTypes.BUBBLE, getX() * 0.25d, getY() * 0.25d, getZ() * 0.25D, motion.x, motion.y, motion.z);
        }
        updatePosition(x, y, z);
    }

    public boolean canImpactEntity(Entity entity)
    {
        if (entity == shooter) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof LivingEntity)) return false;
        if (entity.getRootVehicle() == shooter) return false;
        if (entity.isSpectator() || !entity.collides() || entity.noClip) return false;
        return shooter != null && !entity.isTeammate(shooter);
    }

    public void hit(RayTraceResult result)
    {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.BLOCK)
        {
            final BlockRayTraceResult brtr = (BlockRayTraceResult) result;
            onBlockImpact(brtr.getBlockPos(), brtr.getSide());
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
    public void setVelocity(Vector3d motionIn)
    {
        super.setVelocity(motionIn);
        ProjectileHelper.method_7484(this, 1);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        if (growthRate == 1) return getType().getDimensions();
        float size = Math.min(getWidth() * growthRate, 2.25f);
        return EntitySize.changing(size, size);
    }

    @Override
    public boolean shouldRender(double distance)
    {
        double d0 = getBoundingBox().getAverageSideLength() * 4;
        if (Double.isNaN(d0)) d0 = 4;
        d0 *= 64;
        return distance < d0 * d0;
    }

    public DamageSource getDamageSource(String name)
    {
        return new IndirectEntityDamageSource(name, this, shooter).setProjectile().setScaledWithDifficulty();
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
    public boolean hasNoGravity()
    {
        return true;
    }

    @Override
    protected boolean canClimb()
    {
        return false;
    }

    @Override
    public float getBrightnessAtEyes()
    {
        return 1f;
    }

    @Override
    public boolean damage(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    public float getTargetingMargin()
    {
        return getWidth();
    }

    @Override
    protected void initDataTracker()
    {
    }

    @Override // Does not Serialize
    protected void readCustomDataFromTag(CompoundNBT compound)
    {
    }

    @Override // Does not Serialize
    protected void writeCustomDataToTag(CompoundNBT compound)
    {
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buf)
    {
        buf.writeInt(shooter.getEntityId());
        buf.writeFloat(growthRate);
    }

    @Override
    public void readSpawnData(PacketBuffer buf)
    {
        this.shooter = (AbstractDragonEntity) world.getEntityById(buf.readInt());
        this.growthRate = buf.readFloat();
    }

    protected enum EffectType
    {
        NONE,
        RAYTRACE,
        COLLIDING
    }
}
