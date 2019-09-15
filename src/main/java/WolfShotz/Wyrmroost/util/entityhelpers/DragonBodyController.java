package WolfShotz.Wyrmroost.util.entityhelpers;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.BodyController;

/**
 * Created by WolfShotz - 8/26/19 - 16:12
 *
 * Class Responsible for disallowing rotation while sitting, sleeping, etc
 */
public class DragonBodyController extends BodyController
{
    private AbstractDragonEntity dragon;
    
    public DragonBodyController(AbstractDragonEntity dragon) {
        super(dragon);
        this.dragon = dragon;
    }
    
    @Override
    public void updateRenderAngles() {
        // No rotations while sitting or sleeping
        if (dragon.isSitting() || dragon.isSleeping()) return;
        super.updateRenderAngles();
    }
}
