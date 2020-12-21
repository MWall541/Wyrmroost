package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.projectile.WindGustEntity;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class AlpineEntity extends AbstractDragonEntity
{
    public static final Animation ROAR_ANIMATION = new Animation(84);
    public static final Animation WIND_GUST_ANIMATION = new Animation(25);
    public static final Animation BITE_ANIMATION = new Animation(10);

    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);

    public AlpineEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.1d, true));
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this));
        goalSelector.addGoal(8, new FlyerWanderGoal(this, 1, 0.01f));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 10));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(0, new HurtByTargetGoal(this));
        targetSelector.addGoal(1, new NonTamedTargetGoal<>(this, BeeEntity.class, false, e -> ((BeeEntity) e).hasNectar()));
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

        sitTimer.add(func_233684_eK_() || isSleeping()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.1f : -0.1f);
        flightTimer.add(isFlying()? 0.1f : -0.05f);

        if (!world.isRemote && noActiveAnimation() && !isSleeping() && !isChild() && getRNG().nextDouble() < 0.0005)
            AnimationPacket.send(this, ROAR_ANIMATION);

        Animation animation = getAnimation();
        int tick = getAnimationTick();

        if (animation == ROAR_ANIMATION)
        {
            if (tick == 0) playSound(WRSounds.ENTITY_ALPINE_ROAR.get(), 3f, 1f);
            else if (tick == 25)
            {
                for (LivingEntity entity : getEntitiesNearby(20, e -> e.getType() == WREntities.ALPINE.get()))
                {
                    AlpineEntity alpine = ((AlpineEntity) entity);
                    if (alpine.noActiveAnimation() && alpine.isIdling() && !alpine.isSleeping())
                        alpine.setAnimation(ROAR_ANIMATION);
                }
            }
        }
        else if (animation == WIND_GUST_ANIMATION)
        {
            if (tick == 0) setMotion(getMotion().add(0, -0.35, 0));
            if (tick == 4)
            {
                if (!world.isRemote) world.addEntity(new WindGustEntity(this));
                setMotion(getMotion().add(getLookVec().inverse().mul(1.5, 0, 1.5).add(0, 1, 0)));
                playSound(WRSounds.WING_FLAP.get(), 3, 1f, true);
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity enemy)
    {
        boolean flag = super.attackEntityAsMob(enemy);

        if (!isTamed() && flag && !enemy.isAlive() && enemy.getType() == EntityType.BEE)
        {
            BeeEntity bee = (BeeEntity) enemy;
            if (bee.hasNectar() && bee.getLeashed())
            {
                Entity holder = bee.getLeashHolder();
                if (holder instanceof PlayerEntity) tame(true, (PlayerEntity) holder);
            }
        }
        return flag;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        Entity attacker = source.getImmediateSource();
        if (attacker != null && attacker.getType() == EntityType.BEE)
        {
            setAttackTarget((BeeEntity) attacker);
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        return size.scale(1, func_233684_eK_() || isSleeping()? 0.7f : 1);
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindPacket.MOUNT_KEY2 && pressed && noActiveAnimation() && isFlying())
            setAnimation(WIND_GUST_ANIMATION);
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().movePosition(-5d, 0.75d, 0);
        else event.getInfo().movePosition(-3, 0.3, 0);
    }

    @Override
    protected void jump()
    {
        super.jump();
        if (!world.isRemote)
            world.addEntity(new WindGustEntity(this, getPositionVec().add(0, 7, 0), getVectorForRotation(90, rotationYaw)));
    }

    @Override
    protected float getJumpUpwardsMotion()
    {
        if (canFly()) return (getHeight() * getJumpFactor());
        else return super.getJumpUpwardsMotion();
    }

    @Override
    public void swingArm(Hand hand)
    {
        setAnimation(BITE_ANIMATION);
        playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1, true);
        super.swingArm(hand);
    }

    @Override
    public int determineVariant()
    {
        return getRNG().nextInt(6);
    }

    @Override
    protected boolean canBeRidden(Entity entity)
    {
        return !isChild() && entity instanceof LivingEntity && isOwner((LivingEntity) entity);
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return ModUtils.equalsAny(stack.getItem(), Items.HONEYCOMB, Items.HONEY_BOTTLE);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * (isFlying()? 0.8f : 1.25f);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_ALPINE_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_ALPINE_ROAR.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_ALPINE_DEATH.get();
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {ROAR_ANIMATION, WIND_GUST_ANIMATION, BITE_ANIMATION};
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.EXTREME_HILLS)
            event.getSpawns().func_242575_a(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ALPINE.get(), 2, 1, 4));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 40)
                .createMutableAttribute(MOVEMENT_SPEED, 0.22)
                .createMutableAttribute(KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(ATTACK_DAMAGE, 3)
                .createMutableAttribute(FLYING_SPEED, 0.185f)
                .createMutableAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 1);
    }
}
