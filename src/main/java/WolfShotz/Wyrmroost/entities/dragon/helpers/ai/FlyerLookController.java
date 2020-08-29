package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.LookController;

public class FlyerLookController extends LookController
{
    private final AbstractDragonEntity dragon;

    public FlyerLookController(AbstractDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void tick()
    {
        if (dragon.isFlying() && isLooking)
        {
            isLooking = false;
            mob.rotationYawHead = clampedRotate(mob.rotationYawHead, getTargetYaw(), deltaLookYaw);
            mob.rotationPitch = clampedRotate(mob.rotationPitch, getTargetPitch(), deltaLookPitch);
        }
        else super.tick();
    }
}
