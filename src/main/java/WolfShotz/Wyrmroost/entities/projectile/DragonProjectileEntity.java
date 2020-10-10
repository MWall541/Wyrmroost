package WolfShotz.Wyrmroost.entities.projectile;


import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class DragonProjectileEntity extends Entity implements IEntityAdditionalSpawnData
{
    public AbstractDragonEntity shooter;
    public double accelerationX, accelerationY, accelerationZ;
    public float growthRate = 1f;
    public int life;
    public boolean hasCollided;

    protected DragonProjectileEntity(EntityType<?> type, World world) { super(type, world); }

    public DragonProjectileEntity(EntityType<? extends DragonProjectileEntity> type, AbstractDragonEntity shooter, Vec3d position, Vec3d acceleration)
    {
        super(type, shooter.world);

        acceleration = acceleration.add(rand.nextGaussian() * getAccelerationOffset(), rand.nextGaussian() * getAccelerationOffset(), rand.nextGaussian() * getAccelerationOffset());
        double length = acceleration.length();
        this.accelerationX = acceleration.x / length * getMotionFactor();
        this.accelerationY = acceleration.y / length * getMotionFactor();
        this.accelerationZ = acceleration.z / length * getMotionFactor();

        this.shooter = shooter;
        this.life = 50;

        setMotion(getMotion().add(accelerationX, accelerationY, accelerationZ));
        position = position.add(getMotion());

        Vec3d motion = getMotion();
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

        if (directRaytrace())
        {
            RayTraceResult rayTrace = ProjectileHelper.rayTrace(this, true, false, shooter, RayTraceContext.BlockMode.OUTLINE);
            if (rayTrace.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, rayTrace))
                onImpact(rayTrace);
        }

        Vec3d motion = getMotion();
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

    public void onImpact(RayTraceResult result)
    {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.BLOCK) onBlockImpact((BlockRayTraceResult) result);
        else if (type == RayTraceResult.Type.ENTITY) onEntityImpact(((EntityRayTraceResult) result).getEntity());
    }

    public void onEntityImpact(Entity result) {}

    public void onBlockImpact(BlockRayTraceResult result) {}

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

    protected float getMotionFactor() { return 0.95f; }

    protected double getAccelerationOffset() { return 0.1; }

    protected int getMaxLife() { return 150; }

    @Override
    protected boolean canTriggerWalking() { return false; }

    protected boolean directRaytrace() { return true; }

    @Override
    public float getBrightness() { return 1f; }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) { return false; }

    @Override
    public float getCollisionBorderSize() { return getWidth(); }

    @Override
    protected void registerData() {}

    @Override // Does not Serialize
    protected void readAdditional(CompoundNBT compound) {}

    @Override // Does not Serialize
    protected void writeAdditional(CompoundNBT compound) {}

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

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
}
