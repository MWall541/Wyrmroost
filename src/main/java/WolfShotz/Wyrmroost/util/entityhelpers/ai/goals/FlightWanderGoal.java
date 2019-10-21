package WolfShotz.Wyrmroost.util.entityhelpers.ai.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.FlightMovementController;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.EnumSet;
import java.util.Random;

public class FlightWanderGoal extends Goal
{
    public AbstractDragonEntity dragon;
    boolean nightTempted;
    
    public FlightWanderGoal(AbstractDragonEntity dragon) {
        this.dragon = dragon;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }
    
    @Override
    public boolean shouldExecute() {
        if (dragon.isSleeping()) return false;
        if (!dragon.getPassengers().isEmpty()) return false;
        if (!dragon.isFlying()) return false;
        if (dragon.getAttackTarget() != null) return false;
        
        if (!dragon.getMoveHelper().isUpdating()) return true;
        
        MovementController moveController = dragon.getMoveHelper();
        double euclid = MathUtils.getSpaceDistSq(dragon.posX, moveController.getX(), dragon.posY, moveController.getY(), dragon.posZ, moveController.getZ());
        
        return euclid < 1 || euclid > 3068d;
    }
    
    @Override
    public void startExecuting() {
        Random rand = dragon.getRNG();
        
        double x = dragon.posX + MathUtils.nextPseudoDouble(rand) * 24d;
        double y = dragon.posY + -MathUtils.nextPseudoDouble(rand) * 16.0F;
        double z = dragon.posZ + MathUtils.nextPseudoDouble(rand) * 24.0F;
        dragon.getMoveHelper().setMoveTo(x, y, z, dragon.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());
    }
    
    @Override
    public boolean shouldContinueExecuting() { return false; }
}
