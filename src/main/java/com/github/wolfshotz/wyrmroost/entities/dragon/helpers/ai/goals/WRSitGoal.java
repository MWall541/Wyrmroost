package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class WRSitGoal extends SitGoal
{
    private final AbstractDragonEntity dragon;

    public WRSitGoal(AbstractDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    public boolean shouldExecute()
    {
        if (!dragon.isTamed()) return false;
        if (dragon.isInWaterOrBubbleColumn() && dragon.getCreatureAttribute() != CreatureAttribute.WATER) return false;
        if (!dragon.isOnGround()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null) return true;
        return (dragon.getDistanceSq(owner) > 144d || owner.getRevengeTarget() == null) && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting()
    {
        dragon.getNavigator().clearPath();
    }

    @Override
    public void tick()
    {
        if (dragon.isFlying()) // get to ground first
        {
            if (dragon.getNavigator().noPath())
            {
                BlockPos pos = findLandingPos();
                dragon.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.05);
            }
        }
        else dragon.func_233686_v_(true);
    }

    private BlockPos findLandingPos()
    {
        Random rand = dragon.getRNG();

        // get current entity position
        BlockPos.Mutable ground = dragon.world.getHeight(Heightmap.Type.WORLD_SURFACE, dragon.getPosition()).toMutable();

        // make sure the y value is suitable
        if (ground.getY() <= 0 || ground.getY() > dragon.getPosY() || !dragon.world.getBlockState(ground.down()).getMaterial().isSolid())
            ground.setY((int) dragon.getPosY() - 5);

        // add some variance
        int followRange = MathHelper.floor(dragon.getAttributeValue(Attributes.FOLLOW_RANGE));
        int ox = followRange - rand.nextInt(followRange) * 2;
        int oz = followRange - rand.nextInt(followRange) * 2;
        ground.setX(ox);
        ground.setZ(oz);

        return ground;
    }
}
