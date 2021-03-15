package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;

public class FlyerMoveController extends MovementController
{
    private final AbstractDragonEntity dragon;

    public FlyerMoveController(AbstractDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    public void tick()
    {
        if (dragon.canBeControlledByRider())
        {
            state = Action.WAIT;
            return;
        }

        if (state == Action.MOVE_TO)
        {
            double x = targetX - dragon.getX();
            double y = targetY - dragon.getY();
            double z = targetZ - dragon.getZ();
            double distSq = x * x + y * y + z * z;
            if (distSq < 2.5000003E-7)
            {
                dragon.setForwardSpeed(0f);
                return;
            }
            if (y > dragon.getFlightThreshold() + 1) dragon.setFlying(true);

            float speed;

            if (dragon.isFlying())
            {
                if (!dragon.getLookControl().isActive())
                    dragon.getLookControl().lookAt(targetX, targetY, targetZ, dragon.getLookYawSpeed(), 75);

                speed = (float) (dragon.getAttributeValue(Attributes.GENERIC_FLYING_SPEED) * this.speed) / 0.225f;
                if (y != 0) dragon.setUpwardSpeed(y > 0? speed : -speed);
            }
            else
            {
                speed = (float) (this.speed * dragon.getAttributeValue(Attributes.GENERIC_MOVEMENT_SPEED));
                BlockPos blockpos = dragon.getBlockPos();
                BlockState blockstate = dragon.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                VoxelShape voxelshape = blockstate.getCollisionShape(dragon.world, blockpos);
                if (y > (double) dragon.stepHeight && x * x + z * z < (double) Math.max(1.0F, dragon.getWidth()) || !voxelshape.isEmpty() && dragon.getY() < voxelshape.getMax(Direction.Axis.Y) + (double) blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES))
                {
                    dragon.getJumpControl().setActive();
                    state = MovementController.Action.JUMPING;
                }
            }
            dragon.yaw = changeAngle(dragon.yaw, (float) (MathHelper.atan2(z, x) * (180f / Mafs.PI)) - 90f, dragon.getYawRotationSpeed());
            dragon.setMovementSpeed(speed);
            state = Action.WAIT;
        }
        else
        {
            dragon.setMovementSpeed(0);
            dragon.setSidewaysSpeed(0);
            dragon.setUpwardSpeed(0);
            dragon.setForwardSpeed(0);
        }
    }
}
