package WolfShotz.Wyrmroost.content.entities.helper.ai;

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
        if (dragon.isSleeping()) return; // Shouldnt be trying to look while asleep...
        
        if (!dragon.isSitting()) { // Follow vanilla looking
            super.tick();
            return;
        }
    
        if (isLooking) {
            isLooking = false;
            float newYaw = func_220675_a(mob.rotationYawHead, func_220678_h(), deltaLookYaw);
            mob.rotationYawHead = newYaw;
            mob.rotationPitch = func_220675_a(mob.rotationPitch, func_220677_g(), this.deltaLookPitch);
        }
    }
    
}
