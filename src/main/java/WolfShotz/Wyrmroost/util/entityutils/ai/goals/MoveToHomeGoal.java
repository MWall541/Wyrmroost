package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ConfigData;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class MoveToHomeGoal extends Goal
{
    private final AbstractDragonEntity dragon;

    public MoveToHomeGoal(AbstractDragonEntity creatureIn)
    {
        this.dragon = creatureIn;
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() { return !dragon.isWithinHomeDistanceCurrentPosition(); }

    @Override
    public boolean shouldContinueExecuting()
    {
        return super.shouldContinueExecuting();
    }

    @Override
    public void tick()
    {
        BlockPos homePos = dragon.getHomePos().get();
        Vec3d movePos = RandomPositionGenerator.findRandomTargetBlockTowards(dragon, ConfigData.homeRadius / 2, 10, new Vec3d(homePos.getX(), homePos.getY(), homePos.getZ()));
        if (dragon.getNavigator().noPath() && movePos != null)
            dragon.getNavigator().tryMoveToXYZ(movePos.x, movePos.y, movePos.z, 1.25);

        if (dragon.getDistanceSq(new Vec3d(dragon.getHomePosition())) > ConfigData.homeRadius + ConfigData.homeRadius || movePos == null) // OK TOO FAR!!
            dragon.tryTeleportToPos(dragon.getHomePosition());
    }
}
