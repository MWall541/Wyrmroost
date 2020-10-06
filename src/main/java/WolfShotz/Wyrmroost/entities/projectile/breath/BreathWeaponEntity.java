package WolfShotz.Wyrmroost.entities.projectile.breath;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.projectile.DragonProjectileEntity;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

public class BreathWeaponEntity extends DragonProjectileEntity
{
    public BreathWeaponEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    public BreathWeaponEntity(EntityType<? extends DragonProjectileEntity> type, AbstractDragonEntity shooter)
    {
        super(type, shooter);
    }

    @Override
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

            switch (dir.getAxis())
            {
                case X:
                    accelerationX = 0;
                    break;
                case Y:
                    accelerationY = 0;
                    break;
                case Z:
                    accelerationZ = 0;
                    break;
            }

            setMotion(Vec3d.ZERO);

            if (!hasCollided)
            {
                life = ticksExisted + 20;
                this.hasCollided = true;
            }
        }
    }
}
