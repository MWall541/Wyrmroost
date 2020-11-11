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

    protected DragonProjectileEntity(EntityType<?> type, World world) { super(type, world); }

    public DragonProjectileEntity(EntityType<? extends DragonProjectileEntity> type, AbstractDragonEntity shooter, Vector3d position, Vector3d velocity)
    {
        super(type, shooter.world);

        velocity = velocity.add(rand.nextGaussian() * getAccelerationOffset(), rand.nextGaussian() * getAccelerationOffset(), rand.nextGaussian() * getAccelerationOffset());
        double length = velocity.length();
        this.acceleration = new Vector3d(velocity.x / length * getMotionFactor(), velocity.y / length * getMotionFactor(), velocity.z / length * getMotionFactor());

        this.shooter = shooter;
        this.life = 50;

        setMotion(getMotion().add(acceleration));
        position = position.add(getMotion());

        Vector3d motion = getMotion();
        float x = (float) (motion.x - position.x);
        float y = (float) (motion.y - position.y);
        float z = (float) (motion.z - position.z);
        float planeSqrt = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) MathHelper.atan2(z, x) * 180f / Mafs.PI - 90f;
        float pitch = (float) -(MathHelper.atan2(y, planeSqrt) * 180f / Mafs.PI);

        setLocationAndAngles(position.x, position.y, position.z, yaw, pitch);
    }

    @Override
    public void tick()
    {
        if ((!world.isRemote && (!shooter.isAlive() || ticksExisted > life || ticksExisted > getMaxLife())) || !world.isBlockLoaded(getPosition()))
        {
            remove();
            return;
        }

        super.tick();
        if (growthRate != 1) recalculateSize();

        switch (getEffectType())
        {
            case RAYTRACE:
            {
                RayTraceResult rayTrace = ProjectileHelper.func_234618_a_(this, this::canImpactEntity);
                if (rayTrace.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, rayTrace))
                    hit(rayTrace);
                break;
            }
            case COLLIDING:
            {
                AxisAlignedBB box = getBoundingBox().grow(0.05);
                for (Entity entity : world.getEntitiesInAABBexcluding(this, box, this::canImpactEntity))
                    onEntityImpact(entity);

                Vector3d position = getPositionVec();
                Vector3d end = position.add(getMotion());
                BlockRayTraceResult rtr = world.rayTraceBlocks(new RayTraceContext(position, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                if (rtr.getType() != RayTraceResult.Type.MISS) onBlockImpact(rtr.getPos(), rtr.getFace());
            }
            default:
                break;
        }

        Vector3d motion = getMotion();
        if (!hasNoGravity()) setMotion(motion = motion.add(0, -0.05, 0));
        double x = getPosX() + motion.x;
        double y = getPosY() + motion.y;
        double z = getPosZ() + motion.z;

        if (isInWater())
        {
            setMotion(motion.scale(0.95f));
            for (int i = 0; i < 4; ++i)
                world.addParticle(ParticleTypes.BUBBLE, getPosX() * 0.25d, getPosY() * 0.25d, getPosZ() * 0.25D, motion.x, motion.y, motion.z);
        }
        setPosition(x, y, z);
    }

    public boolean canImpactEntity(Entity entity)
    {
        if (entity == shooter) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof LivingEntity)) return false;
        if (entity.getLowestRidingEntity() == shooter) return false;
        if (entity.isSpectator() || !entity.canBeCollidedWith() || entity.noClip) return false;
        return shooter != null && !entity.isOnSameTeam(shooter);
    }

    public void hit(RayTraceResult result)
    {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.BLOCK)
        {
            final BlockRayTraceResult brtr = (BlockRayTraceResult) result;
            onBlockImpact(brtr.getPos(), brtr.getFace());
        }
        else if (type == RayTraceResult.Type.ENTITY) onEntityImpact(((EntityRayTraceResult) result).getEntity());
    }

    public void onEntityImpact(Entity entity) {}

    public void onBlockImpact(BlockPos pos, Direction direction) {}

    @Override
    public void setMotion(Vector3d motionIn)
    {
        super.setMotion(motionIn);
        ProjectileHelper.rotateTowardsMovement(this, 1);
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        if (growthRate == 1) return getType().getSize();
        float size = Math.min(getWidth() * growthRate, 2.25f);
        return EntitySize.flexible(size, size);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = getBoundingBox().getAverageEdgeLength() * 4;
        if (Double.isNaN(d0)) d0 = 4;
        d0 *= 64;
        return distance < d0 * d0;
    }

    public DamageSource getDamageSource(String name) { return new IndirectEntityDamageSource(name, this, shooter).setProjectile().setDifficultyScaled(); }

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
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public float getBrightness()
    {
        return 1f;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    public float getCollisionBorderSize()
    {
        return getWidth();
    }

    @Override
    protected void registerData() {}

    @Override // Does not Serialize
    protected void readAdditional(CompoundNBT compound) {}

    @Override // Does not Serialize
    protected void writeAdditional(CompoundNBT compound) {}

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
        this.shooter = (AbstractDragonEntity) world.getEntityByID(buf.readInt());
        this.growthRate = buf.readFloat();
    }

    protected enum EffectType
    {
        NONE,
        RAYTRACE,
        COLLIDING
    }
}
