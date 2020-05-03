package WolfShotz.Wyrmroost.util.entityutils.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.LookController;

public class DragonLookController extends LookController
{
    private AbstractDragonEntity dragon;
    
    public DragonLookController(AbstractDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }
    
    @Override
    public void tick()
    {
        if (dragon.isSleeping())
        { // Shouldnt be trying to look while asleep...
            isLooking = false;
            return;
        }

        if (dragon.getRidingEntity() != null)
        { // Follow vanilla looking
            super.tick();
            return;
        }

        if (isLooking)
        {
            isLooking = false;
            mob.rotationYawHead = clampedRotate(mob.rotationYawHead, getTargetYaw(), deltaLookYaw);
            mob.rotationPitch = clampedRotate(mob.rotationPitch, getTargetPitch(), deltaLookPitch);
        }
    }
    
}
