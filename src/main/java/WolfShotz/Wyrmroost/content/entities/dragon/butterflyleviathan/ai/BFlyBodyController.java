package WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ai;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.entityutils.DragonBodyController;
import net.minecraft.util.math.MathHelper;

public class BFlyBodyController extends DragonBodyController
{
    public BFlyBodyController(AbstractDragonEntity dragon)
    {
        super(dragon);
    }

    @Override
    public void updateRenderAngles()
    {
        double deltaX = dragon.posX - dragon.prevPosX;
        double deltaZ = dragon.posZ - dragon.prevPosZ;
        double dist = deltaX * deltaX + deltaZ * deltaZ;

        if (!dragon.isInWater() && dist > 0.0001)
        {
            super.updateRenderAngles();
            return;
        }

        dragon.renderYawOffset = dragon.rotationYaw;
        dragon.rotationYawHead = MathHelper.func_219800_b(dragon.rotationYawHead, dragon.renderYawOffset, (float) dragon.getHorizontalFaceSpeed());
    }
}
