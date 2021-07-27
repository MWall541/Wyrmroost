package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
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
    private final TameableDragonEntity dragon;

    public FlyerMoveController(TameableDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    public void tick()
    {
        if (dragon.canBeControlledByRider())
        {
            operation = Action.WAIT;
            return;
        }

        if (operation == Action.MOVE_TO)
        {
            double x = wantedX - dragon.getX();
            double y = wantedY - dragon.getY();
            double z = wantedZ - dragon.getZ();
            double distSq = x * x + y * y + z * z;
            if (distSq < 2.5000003E-7)
            {
                dragon.setZza(0f);
                return;
            }
            if (y > dragon.getFlightThreshold() + 1) dragon.setFlying(true);

            float speed;

            if (dragon.isFlying())
            {
                speed = (float) (dragon.getAttributeValue(Attributes.FLYING_SPEED) * speedModifier);

                if (!dragon.getLookControl().isHasWanted())
                    dragon.getLookControl().setLookAt(wantedX, wantedY, wantedZ, dragon.getHeadRotSpeed(), 75);
                if (y != 0) dragon.setYya(y > 0? speed : -speed);
            }
            else
            {
                speed = (float) (dragon.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedModifier);
                BlockPos blockpos = dragon.blockPosition();
                BlockState state = dragon.level.getBlockState(blockpos);
                Block block = state.getBlock();
                VoxelShape voxelshape = state.getCollisionShape(dragon.level, blockpos);
                if (y > (double) dragon.maxUpStep && x * x + z * z < (double) Math.max(1.0F, dragon.getBbWidth()) || !voxelshape.isEmpty() && dragon.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY() && !block.is(BlockTags.DOORS) && !block.is(BlockTags.FENCES))
                {
                    dragon.getJumpControl().jump();
                    operation = MovementController.Action.JUMPING;
                }
            }
            dragon.yRot = rotlerp(dragon.yRot, (float) (MathHelper.atan2(z, x) * (180f / Mafs.PI)) - 90f, dragon.getYawRotationSpeed());
            dragon.setSpeed(speed);
            operation = Action.WAIT;
        }
        else
        {
            dragon.setSpeed(0);
            dragon.setXxa(0);
            dragon.setYya(0);
            dragon.setZza(0);
        }
    }
}
