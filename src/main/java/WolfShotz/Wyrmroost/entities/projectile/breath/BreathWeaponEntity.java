package WolfShotz.Wyrmroost.entities.projectile.breath;


import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class BreathWeaponEntity extends Entity implements IEntityAdditionalSpawnData
{
    public AbstractDragonEntity shooter;
    public double accelerationX, accelerationY, accelerationZ;
    public float growthRate = 1.01f;
    private int life;
    public boolean hasCollided;

    /**
     * @deprecated this is used for registration
     */
    public BreathWeaponEntity(EntityType<?> type, World world) { super(type, world); }

    public BreathWeaponEntity(EntityType<? extends BreathWeaponEntity> type, AbstractDragonEntity shooter)
    {
        super(type, shooter.world);

        Vec3d mouth = shooter.getApproximateMouthPos();
        Vec3d acceleration = shooter.getLookVec().add(rand.nextGaussian() * 0.15d, rand.nextGaussian() * 0.15d, rand.nextGaussian() * 0.15d);
        double sqrt = MathHelper.sqrt(acceleration.x * acceleration.x + acceleration.y * acceleration.y + acceleration.z * acceleration.z);

        setLocationAndAngles(mouth.x, mouth.y, mouth.z, rotationYaw, rotationPitch);
        this.shooter = shooter;
        this.accelerationX = acceleration.x / sqrt * 0.1d;
        this.accelerationY = acceleration.y / sqrt * 0.1d;
        this.accelerationZ = acceleration.z / sqrt * 0.1d;
        this.life = 50;
    }

    @Override
    public void tick()
    {
        if ((!world.isRemote && (!shooter.isAlive() || ticksExisted > life || ticksExisted > 150)) || !world.isBlockLoaded(getPosition()))
        {
            remove();
            return;
        }

        super.tick();
        recalculateSize();

        RayTraceResult rayTrace = ProjectileHelper.rayTrace(this, true, false, shooter, RayTraceContext.BlockMode.OUTLINE);
        if (rayTrace.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, rayTrace))
            onImpact(rayTrace);

        Vec3d motion = getMotion();
        float motionScale = getMotionFactor();
        double x = getPosX() + motion.x;
        double y = getPosY() + motion.y;
        double z = getPosZ() + motion.z;

        if (isInWater())
        {
            motionScale *= 0.2f;
            for (int i = 0; i < 4; ++i)
                world.addParticle(ParticleTypes.BUBBLE, getPosX() * 0.25d, getPosY() * 0.25d, getPosZ() * 0.25D, motion.x, motion.y, motion.z);
        }
        setMotion(motion.add(accelerationX, accelerationY, accelerationZ).scale(motionScale));
        setPosition(x, y, z);
    }

    public void onImpact(RayTraceResult result)
    {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.BLOCK) onBlockImpact((BlockRayTraceResult) result);
        else if (type == RayTraceResult.Type.ENTITY) onEntityImpact(((EntityRayTraceResult) result).getEntity());
    }

    public void onEntityImpact(Entity result) {}

    public void onBlockImpact(BlockRayTraceResult result)
    {
        BlockPos pos = result.getPos();
        BlockState state = world.getBlockState(result.getPos());
        state.onProjectileCollision(world, state, result, this);

        if (!world.isRemote && !noClip && !world.getBlockState(result.getPos()).getCollisionShape(world, pos).equals(VoxelShapes.empty()))
        {
            accelerationX += Mafs.nextDouble(rand) * 0.05d;
            accelerationY += Mafs.nextDouble(rand) * 0.05f;
            accelerationZ += Mafs.nextDouble(rand) * 0.05d;

            Direction dir = result.getFace();

            if (dir.getAxis() == Direction.Axis.X) accelerationX = 0;
            else if (dir.getAxis() == Direction.Axis.Y) accelerationY = 0;
            else if (dir.getAxis() == Direction.Axis.Z) accelerationZ = 0;

            setMotion(Vec3d.ZERO);

            if (!hasCollided)
            {
                life = ticksExisted + 20;
                this.hasCollided = true;
            }
        }
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        if (growthRate == 1) return getType().getSize();
        float size = Math.min(getWidth() * 1.04f, 2f);
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

    protected float getMotionFactor() { return 0.95f; }

    @Override
    protected boolean canTriggerWalking() { return false; }

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
    public void writeSpawnData(PacketBuffer buf) { buf.writeInt(shooter.getEntityId()); }

    @Override
    public void readSpawnData(PacketBuffer buf) { this.shooter = (AbstractDragonEntity) world.getEntityByID(buf.readInt()); }
}
