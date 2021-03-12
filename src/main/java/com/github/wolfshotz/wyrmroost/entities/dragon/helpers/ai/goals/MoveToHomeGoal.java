package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class MoveToHomeGoal extends Goal
{
    private int time;
    private final AbstractDragonEntity dragon;

    public MoveToHomeGoal(AbstractDragonEntity creatureIn)
    {
        this.dragon = creatureIn;
        setControls(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canStart()
    {
        return !dragon.isInWalkTargetRange();
    }

    @Override
    public void start()
    {
        dragon.clearAI();
    }

    @Override
    public void stop()
    {
        this.time = 0;
    }

    @Override
    public void tick()
    {
        int sq = WRConfig.homeRadius * WRConfig.homeRadius;
        Vector3d home = Vector3d.of(dragon.getPositionTarget());
        final int TIME_UNTIL_TELEPORT = 600; // 30 seconds

        time++;
        if (dragon.squaredDistanceTo(home) > sq + 35 || time >= TIME_UNTIL_TELEPORT)
            dragon.trySafeTeleport(dragon.getPositionTarget().up());
        else
        {
            Vector3d movePos;
            if (dragon.getNavigation().isIdle() && (movePos = RandomPositionGenerator.findTargetTowards(dragon, WRConfig.homeRadius, 10, home)) != null)
                dragon.getNavigation().startMovingTo(movePos.x, movePos.y, movePos.y, 1.1);
        }
    }
}
