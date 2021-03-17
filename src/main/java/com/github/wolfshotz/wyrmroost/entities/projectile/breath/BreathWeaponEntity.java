package com.github.wolfshotz.wyrmroost.entities.projectile.breath;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.projectile.DragonProjectileEntity;
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
        super(type, shooter, shooter.getApproximateMouthPos(), Vector3d.fromPolar(shooter.pitch, shooter.headYaw));
        this.growthRate = 1.025f;
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
//        BlockState state = world.getBlockState(pos);
//        state.onProjectileCollision(world, state, result, this); todo.. somehow

        if (!world.isClientSide && !noClip && !world.getBlockState(pos).getCollisionShape(world, pos).equals(VoxelShapes.empty()))
        {
            setVelocity(acceleration.multiply(-Math.abs(direction.getOffsetX()) + 1, -Math.abs(direction.getOffsetY()) + 1, -Math.abs(direction.getOffsetZ()) + 1));

            if (!hasCollided)
            {
                life = age + 20;
                this.hasCollided = true;
            }
        }
    }

    @Override
    protected float getMotionFactor()
    {
        return 0.7f;
    }

    @Override
    protected EffectType getEffectType()
    {
        return EffectType.COLLIDING;
    }
}
