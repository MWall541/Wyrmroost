package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class MoveToHomeGoal extends Goal
{
    private int time;
    private final TameableDragonEntity dragon;

    public MoveToHomeGoal(TameableDragonEntity creatureIn)
    {
        this.dragon = creatureIn;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        return !dragon.isWithinRestriction();
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
        int sq = WRConfig.HOME_RADIUS.get() * WRConfig.HOME_RADIUS.get();
        Vector3d home = Vector3d.atLowerCornerOf(dragon.getRestrictCenter());
        final int TIME_UNTIL_TELEPORT = 600; // 30 seconds

        time++;
        if (dragon.distanceToSqr(home) > sq + 35 || time >= TIME_UNTIL_TELEPORT)
            dragon.trySafeTeleport(dragon.getRestrictCenter().above());
        else
        {
            Vector3d movePos;
            if (dragon.getNavigation().isDone() && (movePos = RandomPositionGenerator.getPosTowards(dragon, WRConfig.HOME_RADIUS.get(), 10, home)) != null)
                dragon.getNavigation().moveTo(movePos.x, movePos.y, movePos.y, 1.1);
        }
    }
}
