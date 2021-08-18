package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.util.math.MathHelper;

/**
 * Created by com.github.WolfShotz - 8/26/19 - 16:12
 * <p>
 * Disallows rotations while sitting, sleeping, and helps control yaw while controlling
 */
public class DragonBodyController extends BodyController
{
    public TameableDragonEntity dragon;

    public DragonBodyController(TameableDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void clientTick()
    {
        // animate limbs when rotating
        float deg = Math.min(Math.abs(dragon.yRot - dragon.yBodyRot) * 0.05f, 1f);
        dragon.animationSpeed += deg * (1 - dragon.animationSpeed * 2);

        // sync the body to the yRot; no reason to have any other random rotations.
        dragon.yBodyRot = dragon.yRot;

        // clamp head rotations so necks don't fucking turn inside out
        dragon.yHeadRot = MathHelper.rotateIfNecessary(dragon.yHeadRot, dragon.yBodyRot, dragon.getMaxHeadYRot());
    }
}
