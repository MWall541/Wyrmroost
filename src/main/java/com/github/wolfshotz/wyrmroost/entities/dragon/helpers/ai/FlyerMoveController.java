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

    public FlyerMoveController(AbstractDragonEntity mob)
    {
        super(mob);
        this.dragon = mob;
    }

    public void tick()
    {
        if (dragon.canPassengerSteer())
        {
            action = Action.WAIT;
            return;
        }

        if (action == Action.MOVE_TO)
        {
            double x = posX - dragon.getPosX();
            double y = posY - dragon.getPosY();
            double z = posZ - dragon.getPosZ();
            double distSq = x * x + y * y + z * z;
            if (distSq < 2.5000003E-7)
            {
                dragon.setMoveForward(0f);
                return;
            }
            if (y > dragon.getFlightThreshold() + 1) dragon.setFlying(true);

            float speed;

            if (dragon.isFlying())
            {
                if (!dragon.getLookController().getIsLooking())
                    dragon.getLookController().setLookPosition(posX, posY, posZ, dragon.getHorizontalFaceSpeed(), 75);

                speed = (float) (dragon.getAttributeValue(Attributes.FLYING_SPEED) * this.speed) / 0.225f;
                if (y != 0) dragon.setMoveVertical(y > 0? speed : -speed);
            }
            else
            {
                speed = (float) (this.speed * dragon.getAttributeValue(Attributes.MOVEMENT_SPEED));
                BlockPos blockpos = mob.getPosition();
                BlockState blockstate = mob.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                VoxelShape voxelshape = blockstate.getCollisionShape(mob.world, blockpos);
                if (y > (double)mob.stepHeight && x * x + z * z < (double)Math.max(1.0F, mob.getWidth()) || !voxelshape.isEmpty() && mob.getPosY() < voxelshape.getEnd(Direction.Axis.Y) + (double)blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES)) {
                    mob.getJumpController().setJumping();
                    action = MovementController.Action.JUMPING;
                }
            }
            dragon.rotationYaw = limitAngle(dragon.rotationYaw, (float) (MathHelper.atan2(z, x) * (180f / Mafs.PI)) - 90f, dragon.getYawRotationSpeed());
            dragon.setAIMoveSpeed(speed);
            action = Action.WAIT;
        }
        else
        {
            dragon.setAIMoveSpeed(0);
            dragon.setMoveStrafing(0);
            dragon.setMoveVertical(0);
            dragon.setMoveForward(0);
        }
    }
}
