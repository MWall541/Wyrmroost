package com.github.wolfshotz.wyrmroost.entities.projectile.breath;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import com.github.wolfshotz.wyrmroost.entities.projectile.DragonProjectileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BreathWeaponEntity extends DragonProjectileEntity
{
    public BreathWeaponEntity(EntityType<?> type, World level)
    {
        super(type, level);
    }

    public BreathWeaponEntity(EntityType<? extends DragonProjectileEntity> type, TameableDragonEntity shooter)
    {
        super(type, shooter, shooter.getApproximateMouthPos(), Vector3d.directionFromRotation(shooter.xRot, shooter.yHeadRot));
        this.growthRate = 1.025f;
    }

    @Override
    public void onBlockImpact(BlockPos pos, Direction direction)
    {
//        BlockState state = world.getBlockState(pos);
//        state.onProjectileCollision(world, state, result, this); todo.. somehow

        if (!level.isClientSide && !noPhysics && !level.getBlockState(pos).getCollisionShape(level, pos).equals(VoxelShapes.empty()))
        {
            setDeltaMovement(acceleration.multiply(-Math.abs(direction.getStepX()) + 1, -Math.abs(direction.getStepY()) + 1, -Math.abs(direction.getStepZ()) + 1));

            if (!hasCollided)
            {
                life = tickCount + 20;
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
