package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
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
        if (action == MovementController.Action.STRAFE)
        {
            float f = (float) mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
            float f1 = (float) speed * f;
            float f2 = moveForward;
            float f3 = moveStrafe;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F)
            {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(mob.rotationYaw * ((float) Math.PI / 180F));
            float f6 = MathHelper.cos(mob.rotationYaw * ((float) Math.PI / 180F));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigator pathnavigator = mob.getNavigator();
            if (pathnavigator != null)
            {
                NodeProcessor nodeprocessor = pathnavigator.getNodeProcessor();
                if (nodeprocessor != null && nodeprocessor.getPathNodeType(mob.world, MathHelper.floor(mob.getPosX() + (double) f7), MathHelper.floor(mob.getPosY()), MathHelper.floor(mob.getPosZ() + (double) f8)) != PathNodeType.WALKABLE)
                {
                    moveForward = 1.0F;
                    moveStrafe = 0.0F;
                    f1 = f;
                }
            }

            mob.setAIMoveSpeed(f1);
            mob.setMoveForward(moveForward);
            mob.setMoveStrafing(moveStrafe);
            action = MovementController.Action.WAIT;
        }
        else if (action == MovementController.Action.MOVE_TO)
        {
            action = MovementController.Action.WAIT;
            double lengthX = posX - mob.getPosX();
            double lengthY = posY - mob.getPosY();
            double lengthZ = posZ - mob.getPosZ();
            double lengthSq = lengthX * lengthX + lengthY * lengthY + lengthZ * lengthZ;
            if (lengthSq < (double) 2.5000003E-7F)
            {
                mob.setMoveForward(0);
                return;
            }

            IAttribute attribute = dragon.isFlying()? SharedMonsterAttributes.FLYING_SPEED : SharedMonsterAttributes.MOVEMENT_SPEED;
            float speed = (float) (this.speed * mob.getAttribute(attribute).getValue());
            if (!dragon.isFlying())
            {
                float rot = (float) (MathHelper.atan2(lengthZ, lengthX) * (double) (180F / (float) Math.PI)) - 90.0F;
                mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, rot, 90f);
            }
            mob.setAIMoveSpeed(speed);
            mob.setMoveVertical(lengthY > 0? speed : -speed);

            // handle jumping
            BlockPos blockpos = new BlockPos(mob);
            BlockState blockstate = mob.world.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            VoxelShape voxelshape = blockstate.getCollisionShape(mob.world, blockpos);
            if (lengthY > (double) mob.stepHeight && lengthX * lengthX + lengthZ * lengthZ < (double) Math.max(1.0F, mob.getWidth()) || !voxelshape.isEmpty() && mob.getPosY() < voxelshape.getEnd(Direction.Axis.Y) + (double) blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES))
            {
                mob.getJumpController().setJumping();
                action = MovementController.Action.JUMPING;
            }
        }
        else if (action == MovementController.Action.JUMPING)
        {
            mob.setAIMoveSpeed((float) (speed * mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
            if (mob.onGround)
            {
                action = MovementController.Action.WAIT;
            }
        }
        else
        {
            mob.setMoveForward(0.0F);
        }

    }
}
