package com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals;

import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
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
    private final TameableDragonEntity dragon;

    public WRSitGoal(TameableDragonEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    public boolean canUse()
    {
        if (!dragon.isTame()) return false;
        if (dragon.isInWaterOrBubble() && dragon.getMobType() != CreatureAttribute.WATER) return false;
        if (!dragon.isOnGround() && !dragon.isFlying()) return false;
        LivingEntity owner = dragon.getOwner();
        if (owner == null) return true;
        return (dragon.distanceToSqr(owner) > 144d || owner.getLastHurtByMob() == null) && super.canUse();
    }

    @Override
    public void tick()
    {
        if (dragon.isFlying()) // get to ground first
        {
            if (dragon.getNavigation().isDone())
            {
                BlockPos pos = findLandingPos();
                dragon.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.05);
            }
        }
        else dragon.setOrderedToSit(true);
    }

    private BlockPos findLandingPos()
    {
        Random rand = dragon.getRandom();

        // get current entity position
        BlockPos.Mutable ground = dragon.level.getHeightmapPos(Heightmap.Type.WORLD_SURFACE, dragon.blockPosition()).mutable();

        // make sure the y value is suitable
        if (ground.getY() <= 0 || ground.getY() > dragon.getY() || !dragon.level.getBlockState(ground.below()).getMaterial().isSolid())
            ground.setY((int) dragon.getY() - 5);

        // add some variance
        int followRange = MathHelper.floor(dragon.getAttributeValue(Attributes.FOLLOW_RANGE));
        int ox = followRange - dragon.getRandom().nextInt(followRange) * 2;
        int oz = followRange - dragon.getRandom().nextInt(followRange) * 2;
        ground.setX(ox);
        ground.setZ(oz);

        return ground;
    }
}
