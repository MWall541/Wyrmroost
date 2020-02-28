package WolfShotz.Wyrmroost.util.entityutils.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.QuikMaths;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Random;

public class FlightWanderGoal extends Goal
{
    public AbstractDragonEntity dragon;
    boolean nightTempted;
    
    public FlightWanderGoal(AbstractDragonEntity dragon)
    {
        this.dragon = dragon;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }
    
    @Override
    public boolean shouldExecute()
    {
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;
        if (!dragon.isFlying()) return false;
        if (dragon.getAttackTarget() != null) return false;
        
        if (!dragon.getMoveHelper().isUpdating()) return true;

        MovementController moveController = dragon.getMoveHelper();
        double euclid = QuikMaths.getSpaceDistSq(dragon.posX, moveController.getX(), dragon.posY, moveController.getY(), dragon.posZ, moveController.getZ());
        
        return euclid < 1 || euclid > 3068d;
    }
    
    @Override
    public void startExecuting()
    {
        Random rand = dragon.getRNG();

        double x = dragon.posX + QuikMaths.nextPseudoDouble(rand) * 24d;
        double y = dragon.posY + -QuikMaths.nextPseudoDouble(rand) * 16.0F;
        double z = dragon.posZ + QuikMaths.nextPseudoDouble(rand) * 24.0F;
        dragon.getMoveHelper().setMoveTo(x, y, z, dragon.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());
    }
    
    @Override
    public boolean shouldContinueExecuting()
    {
        return false;
    }
}
