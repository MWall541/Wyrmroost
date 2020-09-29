package WolfShotz.Wyrmroost.entities.dragon.helpers.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.util.math.MathHelper;

public class LessShitLookController extends LookController
{
    private boolean shouldResetPitch = true;
    private boolean skipLook = false;

    public LessShitLookController(MobEntity entity)
    {
        super(entity);
    }

    public void tick()
    {
        if (skipLook)
        {
            mob.rotationYawHead = clampedRotate(mob.rotationYawHead, mob.renderYawOffset, mob.getFaceRotSpeed());
            mob.rotationPitch = clampedRotate(mob.rotationPitch, 0, mob.getFaceRotSpeed());
            skipLook = false;
            isLooking = false;
            return;
        }

        if (shouldResetPitch) mob.rotationPitch = 0;

        if (isLooking)
        {
            mob.rotationYawHead = clampedRotate(mob.rotationYawHead, getTargetYaw(), deltaLookYaw);
            mob.rotationPitch = clampedRotate(mob.rotationPitch, getTargetPitch(), deltaLookPitch);
        }
        else mob.rotationYawHead = clampedRotate(mob.rotationYawHead, mob.renderYawOffset, mob.getFaceRotSpeed());

        if (!mob.getNavigator().noPath())
            mob.rotationYawHead = MathHelper.func_219800_b(mob.rotationYawHead, mob.renderYawOffset, mob.getFaceRotSpeed());
    }

    protected boolean func_220680_b()
    {
        return shouldResetPitch();
    }

    public boolean shouldResetPitch()
    {
        return shouldResetPitch;
    }

    public void setShouldResetPitch(boolean b)
    {
        shouldResetPitch = b;
    }

    public void skipLooking(boolean b) { skipLook = b; }
}
