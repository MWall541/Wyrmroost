package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.util.math.MathHelper;

/**
 * Created by com.github.WolfShotz - 8/26/19 - 16:12
 * <p>
 * Disallows rotations while sitting, sleeping, and helps control yaw while controlling
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
        // No body rotations while sitting or sleeping
        if (dragon.isSleeping()) return;

        // Clamp the head rotation to 70 degrees while sitting
        if (dragon.isInSittingPose())
        {
            clampHeadRotation(70f);
            return;
        }

        // clamp head to 120 degrees, rotate body according to head
        if (dragon.canBeControlledByRider() || dragon.isFlying())
        {
            clampHeadRotation(120f);
            dragon.renderYawOffset = dragon.rotationYaw = MathHelper.wrapDegrees(MathHelper.func_219800_b(dragon.rotationYawHead, dragon.renderYawOffset, dragon.getYawRotationSpeed()));
            return;
        }

        super.updateRenderAngles();
    }

    public void clampHeadRotation(float clampDeg)
    {
        dragon.rotationYawHead = MathHelper.func_219800_b(dragon.rotationYawHead, dragon.renderYawOffset, clampDeg);
    }
}
