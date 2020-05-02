package WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.canariwyvern.CanariWyvernEntity;
import WolfShotz.Wyrmroost.util.network.NetworkUtils;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.player.PlayerEntity;

public class CanariAvoidGoal extends AvoidEntityGoal<PlayerEntity>
{
    public PlayerEntity prevTarget;

    public CanariAvoidGoal(CanariWyvernEntity entityIn)
    {
        super(entityIn, PlayerEntity.class, 6, 1, 1.5);
    }

    @Override
    public boolean shouldExecute()
    {
        boolean should = super.shouldExecute();
        prevTarget = avoidTarget;
        return should;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        NetworkUtils.sendAnimationPacket((CanariWyvernEntity) entity, CanariWyvernEntity.THREAT_ANIMATION);
    }
}
