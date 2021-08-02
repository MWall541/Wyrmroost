package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.client.model.entity.AlpineModel;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.WRFollowOwnerGoal;
import com.github.wolfshotz.wyrmroost.entities.projectile.WindGustEntity;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializer;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindHandler;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
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

public class AlpineEntity extends TameableDragonEntity
{
    public static final EntitySerializer<AlpineEntity> SERIALIZER = TameableDragonEntity.SERIALIZER.concat(b -> b
            .track(EntitySerializer.BOOL, "Sleeping", TameableDragonEntity::isSleeping, TameableDragonEntity::setSleeping)
            .track(EntitySerializer.INT, "Variant", TameableDragonEntity::getVariant, TameableDragonEntity::setVariant));

    public static final Animation<AlpineEntity, AlpineModel> ROAR_ANIMATION = Animation.create(84, AlpineEntity::roarAnimation, () -> AlpineModel::roarAnimation);
    public static final Animation<AlpineEntity, AlpineModel> WIND_GUST_ANIMATION = Animation.create(25, AlpineEntity::windGustAnimation, () -> AlpineModel::windGustAnimation);
    public static final Animation<AlpineEntity, AlpineModel> BITE_ANIMATION = Animation.create(10, null, () -> AlpineModel::biteAnimation);
    public static final Animation<?, ?>[] ANIMATIONS = new Animation[]{ROAR_ANIMATION, WIND_GUST_ANIMATION, BITE_ANIMATION};

    public final LerpedFloat sitTimer = LerpedFloat.unit();
    public final LerpedFloat flightTimer = LerpedFloat.unit();

    public AlpineEntity(EntityType<? extends TameableDragonEntity> dragon, World level)
    {
        super(dragon, level);
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
    public EntitySerializer<? extends TameableDragonEntity> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(FLYING, false);
        entityData.define(SLEEPING, false);
        entityData.define(VARIANT, 0);
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        sitTimer.add(isInSittingPose() || isSleeping()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.1f : -0.1f);
        flightTimer.add(isFlying()? 0.1f : -0.05f);

        if (!level.isClientSide && noAnimations() && !isSleeping() && !isBaby() && getRandom().nextDouble() < 0.0005)
            AnimationPacket.send(this, ROAR_ANIMATION);
    }

    public void roarAnimation(int time)
    {
        if (time == 0) playSound(WRSounds.ENTITY_ALPINE_ROAR.get(), 3f, 1f);
        else if (time == 25)
        {
            for (LivingEntity entity : getEntitiesNearby(20, e -> e.getType() == WREntities.ALPINE.get()))
            {
                AlpineEntity alpine = ((AlpineEntity) entity);
                if (alpine.noAnimations() && alpine.isIdling() && !alpine.isSleeping())
                    alpine.setAnimation(ROAR_ANIMATION);
            }
        }
    }

    public void windGustAnimation(int time)
    {
        if (time == 0) setDeltaMovement(getDeltaMovement().add(0, -0.35, 0));
        if (time == 4)
        {
            if (!level.isClientSide) level.addFreshEntity(new WindGustEntity(this));
            setDeltaMovement(getDeltaMovement().add(getLookAngle().reverse().multiply(1.5, 0, 1.5).add(0, 1, 0)));
            playSound(WRSounds.WING_FLAP.get(), 3, 1f, true);
        }
    }

    @Override
    public boolean doHurtTarget(Entity enemy)
    {
        boolean flag = super.doHurtTarget(enemy);

        if (!isTame() && flag && !enemy.isAlive() && enemy.getType() == EntityType.BEE)
        {
            BeeEntity bee = (BeeEntity) enemy;
            if (bee.hasNectar() && bee.isLeashed())
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
        Entity attacker = source.getDirectEntity();
        if (attacker != null && attacker.getType() == EntityType.BEE)
        {
            setTarget((BeeEntity) attacker);
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scale(getScale());
        return size.scale(1, isInSittingPose() || isSleeping()? 0.7f : 1);
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (key == KeybindHandler.ALT_MOUNT_KEY && pressed && noAnimations() && isFlying())
            setAnimation(WIND_GUST_ANIMATION);
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView)
            event.getInfo().move(ClientEvents.getViewCollision(-5d, this), 0.75d, 0);
        else
            event.getInfo().move(ClientEvents.getViewCollision(-3, this), 0.3, 0);
    }

    @Override
    protected void jumpFromGround()
    {
        super.jumpFromGround();
        if (!level.isClientSide)
            level.addFreshEntity(new WindGustEntity(this, position().add(0, 7, 0), calculateViewVector(90, yRot)));
    }

    @Override
    protected float getJumpPower()
    {
        if (canFly()) return (getBbHeight() * getBlockJumpFactor());
        else return super.getJumpPower();
    }

    @Override
    public void swing(Hand hand)
    {
        setAnimation(BITE_ANIMATION);
        playSound(SoundEvents.GENERIC_EAT, 1, 1, true);
        super.swing(hand);
    }

    @Override
    public int determineVariant()
    {
        return getRandom().nextInt(6);
    }

    @Override
    protected boolean canAddPassenger(Entity entity)
    {
        return !isBaby() && entity instanceof LivingEntity && isOwnedBy((LivingEntity) entity);
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
    public Animation<?, ?>[] getAnimations()
    {
        return ANIMATIONS;
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.EXTREME_HILLS)
            event.getSpawns().addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ALPINE.get(), 2, 1, 4));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 40)
                .add(MOVEMENT_SPEED, 0.22)
                .add(KNOCKBACK_RESISTANCE, 1)
                .add(ATTACK_DAMAGE, 3)
                .add(FLYING_SPEED, 0.185f)
                .add(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 1);
    }
}
