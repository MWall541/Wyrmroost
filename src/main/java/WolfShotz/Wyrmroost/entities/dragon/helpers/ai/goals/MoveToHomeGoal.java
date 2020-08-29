package WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class MoveToHomeGoal extends Goal
{
    private int time;
    private final AbstractDragonEntity dragon;

    public MoveToHomeGoal(AbstractDragonEntity creatureIn)
    {
        this.dragon = creatureIn;
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() { return !dragon.isWithinHomeDistanceCurrentPosition(); }

    @Override
    public void startExecuting() { dragon.clearAI(); }

    @Override
    public void resetTask() { this.time = 0; }

    @Override
    public void tick()
    {
        int sq = WRConfig.homeRadius * WRConfig.homeRadius;
        Vec3d home = new Vec3d(dragon.getHomePosition());
        final int TIME_UNTIL_TELEPORT = 600; // 30 seconds

        time++;
        if (dragon.getDistanceSq(home) > sq + 35 || time >= TIME_UNTIL_TELEPORT)
            dragon.trySafeTeleport(dragon.getHomePosition().up());
        else
        {
            Vec3d movePos = RandomPositionGenerator.findRandomTargetBlockTowards(dragon, WRConfig.homeRadius, 10, home);

            if (dragon.getNavigator().noPath() && movePos != null)
                dragon.getNavigator().tryMoveToXYZ(movePos.x, movePos.y, movePos.y, 1.1);
        }
    }
}
