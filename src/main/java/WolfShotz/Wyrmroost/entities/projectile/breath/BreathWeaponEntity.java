package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class BreathWeaponEntity extends DamagingProjectileEntity
{
    public double life;

    public BreathWeaponEntity(EntityType<? extends DamagingProjectileEntity> type, World world) { super(type, world); }

    public BreathWeaponEntity(EntityType<? extends BreathWeaponEntity> type, AbstractDragonEntity shooter)
    {
        this(type, shooter.world);
        this.shootingEntity = shooter;
        Vec3d mouth = shooter.getApproximateMouthPos();
        setLocationAndAngles(mouth.x, mouth.y, mouth.z, rotationYaw, rotationPitch);
        Vec3d look = shooter.getLookVec();
        double d0 = MathHelper.sqrt(look.x * look.x + look.y * look.y + look.z * look.z);
        this.accelerationX = look.x / d0 * 0.1D;
        this.accelerationY = look.y / d0 * 0.1D;
        this.accelerationZ = look.z / d0 * 0.1D;
        this.life = 70;
    }

    @Override
    public void tick()
    {
        if (isInWater())
        {
            playSound(SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, 1, 1);
            for (int i = 0; i < 25; i++)
                world.addParticle(ParticleTypes.SMOKE, getPosX(), getPosY(), getPosZ(), Mafs.nextPseudoDouble(rand) * 0.2f, rand.nextDouble() * 0.1f, Mafs.nextPseudoDouble(rand) * 0.2f);
            remove();
            return;
        }

        if (!world.isRemote && ticksExisted > life || ticksExisted > 100)
        {
            remove();
            return;
        }

        super.tick();
        recalculateSize();
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        super.onImpact(result);
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.ENTITY)
        {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            if (entity instanceof LivingEntity) onEntityImpact((LivingEntity) entity);
        }
        else if (type == RayTraceResult.Type.BLOCK && !world.isRemote)
        {
            Direction dir = ((BlockRayTraceResult) result).getFace();
            life = ticksExisted + 20;
            setMotion(Vec3d.ZERO);

            Vec3d vec3d = new Vec3d(0, Mafs.nextPseudoDouble(rand) * 0.05d, Mafs.nextPseudoDouble(rand) * 0.05d).rotatePitch(dir.getYOffset() * (float) Math.toDegrees(360));

            accelerationX += vec3d.x;
            accelerationY += vec3d.y;
            accelerationZ += vec3d.z;
        }
    }

    public abstract void onEntityImpact(LivingEntity entity);

    @Override // Overriding because we have a better way of rendering fire. Mojang defines middle finger
    public boolean canRenderOnFire() { return false; }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) { return !isInvulnerableTo(source); }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        return super.getSize(poseIn);
    }

    @Override
    public boolean canBeCollidedWith() { return false; }

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
