package WolfShotz.Wyrmroost.util.entityhelpers.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.LookController;

//TODO
public class DragonLookController extends LookController
{
    private AbstractDragonEntity dragon;
    
    public DragonLookController(AbstractDragonEntity dragon) {
        super(dragon);
        this.dragon = dragon;
    }
    
    @Override
    public void tick() {
        if (dragon.isSleeping()) { // Shouldnt be trying to look while asleep...
            isLooking = false;
            return;
        }
        
        if (!dragon.isSitting() && dragon.getRidingEntity() != null) { // Follow vanilla looking
            super.tick();
            return;
        }
    
        if (isLooking) {
            isLooking = false;
            mob.rotationYawHead = func_220675_a(mob.rotationYawHead, func_220678_h(), deltaLookYaw);
            mob.rotationPitch = func_220675_a(mob.rotationPitch, func_220677_g(), this.deltaLookPitch);
        }
    }
    
}
