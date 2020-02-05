package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ConfigData;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MoveTowardsHomePointGoal extends MoveTowardsRestrictionGoal
{
    private final AbstractDragonEntity dragon;
    private Vec3d movePos;

    public MoveTowardsHomePointGoal(AbstractDragonEntity creatureIn, double speedIn)
    {
        super(creatureIn, speedIn);
        this.dragon = creatureIn;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!dragon.isWithinHomeDistanceFromPosition())
        {
            BlockPos homePos = dragon.getHomePos().get();
            return (movePos = RandomPositionGenerator.findRandomTargetBlockTowards(dragon, ConfigData.homeRadius, 10, new Vec3d(homePos.getX(), homePos.getY(), homePos.getZ()))) != null;
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        dragon.getNavigator().tryMoveToXYZ(movePos.x, movePos.y, movePos.z, 1);
    }

    @Override
    public void tick()
    {
        if (dragon.getDistanceSq(new Vec3d(dragon.getHomePosition())) > ConfigData.homeRadius + 32)
            dragon.tryTeleportToPos(dragon.getHomePosition());
    }
}
