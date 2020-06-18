package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class BreathWeaponEntity extends DamagingProjectileEntity
{
    public double life;
    public boolean hasCollided;
    public float growthRate = 1.05f;
    public float maxSize = 3.5f;

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
        this.life = 50;
    }

    @Override
    public void tick()
    {
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
        else if (type == RayTraceResult.Type.BLOCK && !world.isRemote && !noClip)
        {
            accelerationX += Mafs.nextPseudoDouble(rand) * 0.05d;
            accelerationY = 0;
            accelerationZ += Mafs.nextPseudoDouble(rand) * 0.05d;
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
        float size = Math.min(getWidth() * growthRate, maxSize);
        return EntitySize.flexible(size, size);
    }

    public abstract void onEntityImpact(LivingEntity entity);

    @Override // Overriding because we have a better way of rendering fire. Mojang defines middle finger
    public boolean canRenderOnFire() { return false; }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) { return !isInvulnerableTo(source); }

    @Override
    public boolean canBeCollidedWith() { return false; }

    @Override
    public IPacket<?> createSpawnPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}
