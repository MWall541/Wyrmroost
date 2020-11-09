package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.SleepController;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class FogWraithEntity extends AbstractDragonEntity
{
    private static final DataParameter<Boolean> STEALTH = EntityDataManager.createKey(FogWraithEntity.class, DataSerializers.BOOLEAN);

    public static final Animation GRAB_AND_ATTACK_ANIMATION = new Animation(400);
    public static final Animation BITE_ANIMATION = new Animation(13);
    public static final Animation SCREECH_ANIMATION = new Animation(100);

    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1f);
    public final TickFloat stealthTimer = new TickFloat().setLimit(0, 0.85f);

    public FogWraithEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);
        registerDataEntry("IsStealth", EntityDataEntry.BOOLEAN, STEALTH, false);
    }

    @Override
    protected SleepController createSleepController()
    {
        return null;
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(9, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(FLYING, false);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();
        flightTimer.add(isFlying()? 0.1f : -0.1f);
        stealthTimer.add(isStealth()? 0.05f : -0.05f);

        Animation animation = getAnimation();
        int tick = getAnimationTick();

        if (animation == BITE_ANIMATION && tick == 5)
        {
            attackInBox(getOffsetBox(2.5f).grow(0.3));
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        if (super.attackEntityAsMob(entity) && entity instanceof LivingEntity)
        {
            int i = 5;
            switch (world.getDifficulty())
            {
                case HARD:
                    i = 12; break;
                case NORMAL:
                    i = 8; break;
                default:
                    break;
            }
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WITHER, i * 20, 1));
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.BLINDNESS, 500));
            return true;
        }
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * 1.3f;
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return false;
    }

    public boolean isStealth()
    {
        return dataManager.get(STEALTH);
    }

    public void setStealth(boolean b)
    {
        dataManager.set(STEALTH, b);
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {GRAB_AND_ATTACK_ANIMATION, BITE_ANIMATION, SCREECH_ANIMATION};
    }

//    public static AttributeModifierMap.MutableAttribute getAttributes()
//    {
//        return MobEntity.func_233666_p_()
//                .createMutableAttribute(MAX_HEALTH, 100)
//                .createMutableAttribute(MOVEMENT_SPEED, 0.31)
//                .createMutableAttribute(KNOCKBACK_RESISTANCE, 1)
//                .createMutableAttribute(FOLLOW_RANGE, 60)
//                .createMutableAttribute(ATTACK_KNOCKBACK, 2.25)
//                .createMutableAttribute(ATTACK_DAMAGE, 10)
//                .createMutableAttribute(FLYING_SPEED, 0.27)
//                .createMutableAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 4);
//    }
}
