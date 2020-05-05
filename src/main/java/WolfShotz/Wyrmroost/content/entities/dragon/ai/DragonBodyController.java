package WolfShotz.Wyrmroost.content.entities.dragon.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.*;
import net.minecraft.entity.ai.controller.*;
import net.minecraft.util.math.*;

/**
 * Created by WolfShotz - 8/26/19 - 16:12
 * <p>
 * Class Responsible for disallowing rotation while sitting, sleeping, etc
 */
public class DragonBodyController extends BodyController
{
    public AbstractDragonEntity dragon;

    public DragonBodyController(AbstractDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void updateRenderAngles()
    {
        // No rotations while sitting or sleeping
        if (dragon.isSleeping()) return;

        if (dragon.isSitting())
        {
            dragon.renderYawOffset = dragon.rotationYaw;
            dragon.rotationYawHead = MathHelper.func_219800_b(dragon.rotationYawHead, dragon.renderYawOffset, (float) dragon.getHorizontalFaceSpeed());

            return;
        }

        super.updateRenderAngles();
    }
}
