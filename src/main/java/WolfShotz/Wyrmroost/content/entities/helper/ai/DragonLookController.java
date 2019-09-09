package WolfShotz.Wyrmroost.content.entities.helper.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.LookController;

//TODO
public class DragonLookController extends LookController
{
    /** Casting, we <i>know</i> our mob is apart of our dragon base */
    private AbstractDragonEntity dragon;
    
    public DragonLookController(AbstractDragonEntity dragon) {
        super(dragon);
        this.dragon = dragon;
    }
    
    @Override
    public void tick() {
        if (!dragon.isSleeping())
            super.tick();
    }
    
    
}
