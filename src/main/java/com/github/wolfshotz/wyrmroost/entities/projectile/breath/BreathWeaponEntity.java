package com.github.wolfshotz.wyrmroost.entities.projectile.breath;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.projectile.DragonProjectileEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BreathWeaponEntity extends DragonProjectileEntity
{
    public BreathWeaponEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    public BreathWeaponEntity(EntityType<? extends DragonProjectileEntity> type, AbstractDragonEntity shooter)
    {
        super(type, shooter, shooter.getApproximateMouthPos(), Vector3d.fromPitchYaw(shooter.rotationPitch, shooter.rotationYawHead));
        this.growthRate = 1.025f;
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
//        BlockState state = world.getBlockState(pos);
//        state.onProjectileCollision(world, state, result, this); todo.. somehow

        if (!world.isRemote && !noClip && !world.getBlockState(pos).getCollisionShape(world, pos).equals(VoxelShapes.empty()))
        {
            acceleration.add(Mafs.nextDouble(rand) * 0.05, Mafs.nextDouble(rand) * 0.05, Mafs.nextDouble(rand) * 0.05);

            Vector3d motion = new Vector3d(1, 1, 0);
            if (direction.getAxis().getPlane() == Direction.Plane.VERTICAL) motion = motion.rotatePitch(-0.5f * Mafs.PI);
            else motion = motion.rotateYaw(direction.getHorizontalAngle() / 180f * Mafs.PI);

            setMotion(acceleration.mul(motion));

            if (!hasCollided)
            {
                life = ticksExisted + 20;
                this.hasCollided = true;
            }
        }
    }

    @Override
    protected float getMotionFactor() { return 0.7f; }

    @Override
    protected EffectType getEffectType() { return EffectType.COLLIDING; }
}
