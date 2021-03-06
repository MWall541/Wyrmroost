package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.WRConfig;
import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.client.sound.BreathSound;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.containers.util.SlotBuilder;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.DragonInvHandler;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.LessShitLookController;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.*;
import com.github.wolfshotz.wyrmroost.entities.projectile.breath.FireBreathEntity;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.items.DragonArmorItem;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.network.packets.KeybindPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.TickFloat;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static net.minecraft.entity.ai.attributes.Attributes.*;


public class RoyalRedEntity extends AbstractDragonEntity
{
    public static final int ARMOR_SLOT = 0;

    public static final Animation ROAR_ANIMATION = new Animation(70);
    public static final Animation SLAP_ATTACK_ANIMATION = new Animation(30);
    public static final Animation BITE_ATTACK_ANIMATION = new Animation(15);

    public static final DataParameter<Boolean> BREATHING_FIRE = EntityDataManager.registerData(RoyalRedEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> KNOCKED_OUT = EntityDataManager.registerData(RoyalRedEntity.class, DataSerializers.BOOLEAN);

    private static final int MAX_KNOCKOUT_TIME = 3600; // 3 minutes

    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat breathTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat knockOutTimer = new TickFloat().setLimit(0, 1);
    private int knockOutTime = 0;

    public RoyalRedEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);
        ignoreCameraFrustum = WRConfig.disableFrustumCheck;

        setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0);
        setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRandom().nextBoolean());
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
        registerDataEntry("KnockOutTime", EntityDataEntry.INTEGER, () -> knockOutTime, this::setKnockoutTime);
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();

        dataTracker.startTracking(BREATHING_FIRE, false);
        dataTracker.startTracking(KNOCKED_OUT, false);
        dataTracker.startTracking(FLYING, false);
        dataTracker.startTracking(ARMOR, ItemStack.EMPTY);
    }

    @Override
    protected void initGoals()
    {
        super.initGoals();

        goalSelector.add(4, new MoveToHomeGoal(this));
        goalSelector.add(5, new AttackGoal());
        goalSelector.add(6, new WRFollowOwnerGoal(this));
        goalSelector.add(7, new DragonBreedGoal(this));
        goalSelector.add(9, new FlyerWanderGoal(this, 1));
        goalSelector.add(10, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.add(11, new LookRandomlyGoal(this));

        targetSelector.add(1, new OwnerHurtByTargetGoal(this));
        targetSelector.add(2, new OwnerHurtTargetGoal(this));
        targetSelector.add(3, new DefendHomeGoal(this));
        targetSelector.add(4, new HurtByTargetGoal(this));
        targetSelector.add(5, new NonTamedTargetGoal<>(this, LivingEntity.class, false, e -> e.getType() == EntityType.PLAYER || e instanceof AnimalEntity));
    }

    @Override
    public DragonInvHandler createInv()
    {
        return new DragonInvHandler(this, 1);
    }

    @Override
    public void tickMovement()
    {
        super.tickMovement();
        flightTimer.add(isFlying()? 0.1f : -0.05f);
        sitTimer.add(isInSittingPose()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.035f : -0.1f);
        breathTimer.add(isBreathingFire()? 0.15f : -0.2f);
        knockOutTimer.add(isKnockedOut()? 0.05f : -0.1f);

        if (!world.isClient)
        {
            if (isBreathingFire() && getControllingPlayer() == null && getTarget() == null)
                setBreathingFire(false);

            if (breathTimer.get() == 1) world.spawnEntity(new FireBreathEntity(this));

            if (noActiveAnimation() && !isKnockedOut() && !isSleeping() && !isBreathingFire() && !isBaby() && getRandom().nextDouble() < 0.0004)
                AnimationPacket.send(this, ROAR_ANIMATION);

            if (isKnockedOut() && --knockOutTime <= 0) setKnockedOut(false);
        }

        Animation anim = getAnimation();
        int animTime = getAnimationTick();

        if (anim == ROAR_ANIMATION)
        {
            if (animTime == 0) playSound(WRSounds.ENTITY_ROYALRED_ROAR.get(), 6, 1, true);
            ((LessShitLookController) getLookControl()).restore();
            for (LivingEntity entity : getEntitiesNearby(10, this::isOnSameTeam))
                entity.addStatusEffect(new EffectInstance(Effects.STRENGTH, 60));
        }
        else if (anim == SLAP_ATTACK_ANIMATION && (animTime == 7 || animTime == 12))
        {
            attackInBox(getOffsetBox(getWidth()).expand(0.2), 50);
            if (animTime == 7) playSound(WRSounds.ENTITY_ROYALRED_HURT.get(), 1, 1, true);
            yaw = headYaw;
        }
        else if (anim == BITE_ATTACK_ANIMATION && animTime == 4)
        {
            attackInBox(getOffsetBox(getWidth()).expand(-0.3), 100);
            playSound(WRSounds.ENTITY_ROYALRED_HURT.get(), 1, 1, true);
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (!isTamed() && isFoodItem(stack))
        {
            if (isBaby() || player.isCreative())
            {
                eat(stack);
                tame(getRandom().nextDouble() < 0.1, player);
                setKnockedOut(false);
                return ActionResultType.success(world.isClient);
            }

            if (isKnockedOut() && knockOutTime <= MAX_KNOCKOUT_TIME / 2)
            {
                if (!world.isClient)
                {
                    // base taming chances on consciousness; the closer it is to waking up the better the chances
                    if (tame(getRandom().nextInt(knockOutTime) < MAX_KNOCKOUT_TIME * 0.2d, player))
                    {
                        setKnockedOut(false);
                        AnimationPacket.send(this, ROAR_ANIMATION);
                    }
                    else knockOutTime += 600; // add 30 seconds to knockout time
                    eat(stack);
                    player.swingHand(hand);
                    return ActionResultType.SUCCESS;
                }
                else return ActionResultType.CONSUME;
            }
        }

        return super.playerInteraction(player, hand, stack);
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        if (isTamed() || isKnockedOut() || cause.getName().equals(DamageSource.OUT_OF_WORLD.getName()))
            super.onDeath(cause);
        else // knockout RR's instead of killing them
        {
            setHealth(getMaxHealth() * 0.25f); // reset to 25% health
            setKnockedOut(true);
        }
    }

    @Override
    public void onTrackedDataSet(DataParameter<?> key)
    {
        if (world.isClient && key.equals(BREATHING_FIRE) && isBreathingFire())
            BreathSound.play(this);
        else super.onTrackedDataSet(key);
    }

    @Override
    public void onInvContentsChanged(int slot, ItemStack stack, boolean onLoad)
    {
        if (slot == ARMOR_SLOT) setArmor(stack);
    }

    @Override
    public void addContainerInfo(DragonInvContainer container)
    {
        super.addContainerInfo(container);
        container.addSlot(new SlotBuilder(container.inventory, ARMOR_SLOT).only(DragonArmorItem.class));
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (!noActiveAnimation()) return;

        if (key == KeybindPacket.MOUNT_KEY1 && pressed && !isBreathingFire())
        {
            if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) setAnimation(ROAR_ANIMATION);
            else meleeAttack();
        }

        if (key == KeybindPacket.MOUNT_KEY2) setBreathingFire(pressed);
    }

    public void meleeAttack()
    {
        if (!world.isClient)
            AnimationPacket.send(this, isFlying() || getRandom().nextBoolean()? BITE_ATTACK_ANIMATION : SLAP_ATTACK_ANIMATION);
    }

    @Override
    public Vector3d getApproximateMouthPos()
    {
        Vector3d rotVector = getRotationVector(pitch, bodyYaw);
        Vector3d position = getCameraPosVec(1).subtract(0, 1.3d, 0);
        position = position.add(rotVector).multiply(getWidth() / 2); // base of neck
        return position.add(rotVector.multiply(2.75));
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getDimensions().scaled(getScaleFactor());
        float heightFactor = isSleeping()? 0.5f : isInSittingPose()? 0.9f : 1;
        return size.scaled(1, heightFactor);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        screen.addAction(StaffAction.INVENTORY);
        screen.addAction(StaffAction.TARGET);
        super.addScreenInfo(screen);
    }

    @Override
    public void setMountCameraAngles(boolean backView, EntityViewRenderEvent.CameraSetup event)
    {
        if (backView) event.getInfo().moveBy(-8.5d, 3d, 0);
        else event.getInfo().moveBy(-5, -0.75, 0);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source == DamageSource.IN_WALL || super.isInvulnerableTo(source);
    }

    @Override
    public int determineVariant()
    {
        return getRandom().nextDouble() < 0.03? -1 : 0;
    }

    @Override
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked() || isKnockedOut();
    }

    @Override
    protected float getActiveEyeHeight(Pose poseIn, EntitySize sizeIn)
    {
        return getHeight() * (isFlying()? 0.95f : 1.13f);
    }

    @Override
    protected boolean canBeRidden(Entity entity)
    {
        return !isBaby() && !isKnockedOut() && isTamed();
    }

    @Override
    protected boolean canAddPassenger(Entity passenger)
    {
        return getPassengerList().size() < 3;
    }

    @Override
    public Vector3d getPassengerPosOffset(Entity entity, int index)
    {
        return new Vector3d(0, getHeight() * 0.85f, index == 0? 0.5f : -1);
    }

    @Override
    public float getScaleFactor()
    {
        return isBaby()? 0.3f : isMale()? 0.8f : 1f;
    }

    @Override
    public int getYawRotationSpeed()
    {
        return isFlying()? 5 : 7;
    }

    public boolean isBreathingFire()
    {
        return dataTracker.get(BREATHING_FIRE);
    }

    public void setBreathingFire(boolean b)
    {
        if (!world.isClient) dataTracker.set(BREATHING_FIRE, b);
    }

    public boolean isKnockedOut()
    {
        return dataTracker.get(KNOCKED_OUT);
    }

    public void setKnockedOut(boolean b)
    {
        dataTracker.set(KNOCKED_OUT, b);
        if (!world.isClient)
        {
            knockOutTime = b? MAX_KNOCKOUT_TIME : 0;
            if (b)
            {
                headYaw = yaw;
                clearAI();
                setFlying(false);
            }
        }
    }

    public void setKnockoutTime(int i)
    {
        knockOutTime = Math.max(0, i);
        if (i > 0 && !isKnockedOut()) dataTracker.set(KNOCKED_OUT, true);
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        if (isKnockedOut()) return false;
        return super.onLivingFall(distance, damageMultiplier);
    }

    @Override
    public boolean canFly()
    {
        return super.canFly() && !isKnockedOut();
    }

    @Override
    public boolean isImmuneToArrows()
    {
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem().isFood() && stack.getItem().getFoodComponent().isMeat();
    }

    @Override
    public boolean shouldRender(double x, double y, double z)
    {
        return true;
    }

    @Override
    public boolean shouldSleep()
    {
        return !isKnockedOut() && super.shouldSleep();
    }

    @Override
    public boolean defendsHome()
    {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_ROYALRED_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_ROYALRED_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_ROYALRED_DEATH.get();
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, ROAR_ANIMATION, SLAP_ATTACK_ANIMATION, BITE_ATTACK_ANIMATION};
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.EXTREME_HILLS)
            event.getSpawns().spawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ROYAL_RED.get(), 1, 1, 1));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void applyAttributes()
    {
        if (!isMale())
        {
            // base female attributes
            getAttributeInstance(GENERIC_MAX_HEALTH).setBaseValue(130);
            getAttributeInstance(GENERIC_MOVEMENT_SPEED).setBaseValue(0.22);
            getAttributeInstance(GENERIC_ATTACK_KNOCKBACK).setBaseValue(4);
            getAttributeInstance(GENERIC_FLYING_SPEED).setBaseValue(0.121);
        }
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        // base male attributes
        return MobEntity.createMobAttributes()
                .add(GENERIC_MAX_HEALTH, 120)
                .add(GENERIC_MOVEMENT_SPEED, 0.2275)
                .add(GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(GENERIC_FOLLOW_RANGE, 60)
                .add(GENERIC_ATTACK_KNOCKBACK, 3)
                .add(GENERIC_ATTACK_DAMAGE, 12)
                .add(GENERIC_FLYING_SPEED, 0.125)
                .add(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 4);
    }

    class AttackGoal extends Goal
    {
        public AttackGoal()
        {
            setControls(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canStart()
        {
            LivingEntity target = getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean shouldContinue()
        {
            LivingEntity target = getTarget();
            if (target != null && target.isAlive())
            {
                if (!isWithinHomeDistanceFromPosition(target.getBlockPos())) return false;
                return EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL.test(target);
            }
            return false;
        }

        @Override
        public void tick()
        {
            LivingEntity target = getTarget();
            double distFromTarget = squaredDistanceTo(target);
            double degrees = Math.atan2(target.getZ() - getZ(), target.getX() - getX()) * (180 / Math.PI) - 90;
            boolean isBreathingFire = isBreathingFire();
            boolean canSeeTarget = getVisibilityCache().canSee(target);

            getLookControl().lookAt(target, 90, 90);

            double headAngle = Math.abs(MathHelper.wrapDegrees(degrees - headYaw));
            boolean shouldBreatheFire = !isAtHome() && (distFromTarget > 100 || target.getY() - getY() > 3 || isFlying()) && headAngle < 30;
            if (isBreathingFire != shouldBreatheFire) setBreathingFire(isBreathingFire = shouldBreatheFire);

            if (getRandom().nextDouble() < 0.001 || distFromTarget > 900) setFlying(true);
            else if (distFromTarget <= 24 && noActiveAnimation() && !isBreathingFire && canSeeTarget)
            {
                bodyYaw = yaw = (float) Mafs.getAngle(RoyalRedEntity.this, target) + 90;
                meleeAttack();
            }

            if (getNavigation().isIdle() || age % 10 == 0)
            {
                boolean isFlyingTarget = target instanceof AbstractDragonEntity && ((AbstractDragonEntity) target).isFlying();
                double y = target.getY() + (!isFlyingTarget && getRandom().nextDouble() > 0.1? 8 : 0);
                getNavigation().startMovingTo(target.getX(), y, target.getZ(), !isFlying() && isBreathingFire? 0.8d : 1.3d);
            }
        }
    }
}
