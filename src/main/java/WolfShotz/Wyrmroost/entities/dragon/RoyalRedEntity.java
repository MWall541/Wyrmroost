package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.WRConfig;
import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.client.sounds.BreathSound;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import WolfShotz.Wyrmroost.containers.util.SlotBuilder;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.LessShitLookController;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.*;
import WolfShotz.Wyrmroost.entities.projectile.breath.FireBreathEntity;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.DragonArmorItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.registry.WREntities;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.TickFloat;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;

import static net.minecraft.entity.ai.attributes.Attributes.*;


public class RoyalRedEntity extends AbstractDragonEntity
{
    public static final int ARMOR_SLOT = 0;

    public static final Animation ROAR_ANIMATION = new Animation(70);
    public static final Animation SLAP_ATTACK_ANIMATION = new Animation(30);
    public static final Animation BITE_ATTACK_ANIMATION = new Animation(15);

    public static final DataParameter<Boolean> BREATHING_FIRE = EntityDataManager.createKey(RoyalRedEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> KNOCKED_OUT = EntityDataManager.createKey(RoyalRedEntity.class, DataSerializers.BOOLEAN);

    private static final int MAX_KNOCKOUT_TIME = 3600; // 3 minutes

    public final TickFloat flightTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat sitTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat breathTimer = new TickFloat().setLimit(0, 1);
    public final TickFloat knockOutTimer = new TickFloat().setLimit(0, 1);
    private int knockOutTime = 0;

    public RoyalRedEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("KnockOutTime", EntityDataEntry.INTEGER, () -> knockOutTime, this::setKnockoutTime);

        setPathPriority(PathNodeType.DANGER_FIRE, 0);
        setPathPriority(PathNodeType.DAMAGE_FIRE, 0);

        setImmune(DamageSource.IN_WALL);

        ignoreFrustumCheck = WRConfig.disableFrustumCheck;
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(BREATHING_FIRE, false);
        dataManager.register(KNOCKED_OUT, false);
        dataManager.register(FLYING, false);
        dataManager.register(ARMOR, ItemStack.EMPTY);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new AttackGoal());
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this, 2));
        goalSelector.addGoal(9, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(5, new NonTamedTargetGoal<>(this, LivingEntity.class, true, e -> e.getType() == EntityType.PLAYER || e instanceof AnimalEntity));
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
    }

    @Override
    public DragonInvHandler createInv() { return new DragonInvHandler(this, 1); }

    @Override
    public void livingTick()
    {
        super.livingTick();
        flightTimer.add(isFlying()? 0.1f : -0.05f);
        sitTimer.add(func_233684_eK_()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.035f : -0.1f);
        breathTimer.add(isBreathingFire()? 0.15f : -0.2f);
        knockOutTimer.add(isKnockedOut()? 0.05f : -0.1f);

        if (!world.isRemote)
        {
            if (isBreathingFire() && getControllingPlayer() == null && getAttackTarget() == null)
                setBreathingFire(false);

            if (breathTimer.get() == 1) world.addEntity(new FireBreathEntity(this));

            if (noActiveAnimation() && !isKnockedOut() && !isSleeping() && !isBreathingFire() && !isChild() && getRNG().nextDouble() < 0.0004)
                AnimationPacket.send(this, ROAR_ANIMATION);

            if (isKnockedOut() && --knockOutTime <= 0) setKnockedOut(false);
        }

        Animation anim = getAnimation();
        int animTime = getAnimationTick();

        if (anim == ROAR_ANIMATION)
        {
            ((LessShitLookController) getLookController()).restore();
            for (LivingEntity entity : getEntitiesNearby(10, this::isOnSameTeam))
                entity.addPotionEffect(new EffectInstance(Effects.STRENGTH, 60));
        }
        else if (anim == SLAP_ATTACK_ANIMATION && (animTime == 10 || animTime == 15))
        {
            attackInFront(0.2);
            if (animTime == 10) playSound(WRSounds.ENTITY_ROYALRED_HURT.get(), 1, 1, true);
            rotationYaw = rotationYawHead;
        }
        else if (anim == BITE_ATTACK_ANIMATION && animTime == 4)
        {
            attackInFront(-0.3);
            playSound(WRSounds.ENTITY_ROYALRED_HURT.get(), 1, 1, true);
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (!isTamed() && isFoodItem(stack))
        {
            if (isChild() || player.isCreative())
            {
                eat(stack);
                tame(getRNG().nextDouble() < 0.1, player);
                setKnockedOut(false);
                return ActionResultType.func_233537_a_(world.isRemote);
            }

            if (isKnockedOut() && knockOutTime <= MAX_KNOCKOUT_TIME / 2)
            {
                if (!world.isRemote)
                {
                    // base taming chances on consciousness; the closer it is to waking up the better the chances
                    if (tame(getRNG().nextInt(knockOutTime) < MAX_KNOCKOUT_TIME * 0.2d, player))
                    {
                        setKnockedOut(false);
                        AnimationPacket.send(this, ROAR_ANIMATION);
                    }
                    else knockOutTime += 600; // add 30 seconds to knockout time
                    eat(stack);
                    player.swingArm(hand);
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
        if (isTamed() || isKnockedOut() || cause.getDamageType().equals(DamageSource.OUT_OF_WORLD.getDamageType()))
            super.onDeath(cause);
        else // knockout RR's instead of killing them
        {
            setHealth(getMaxHealth() * 0.25f); // reset to 25% health
            setKnockedOut(true);
        }
    }

    @Override
    public void handleSleep()
    {
        if (isSleeping())
        {
            if (world.isDaytime() && getRNG().nextInt(300) == 0) setSleeping(false);
        }
        else
        {
            if (--sleepCooldown > 0) return;
            if (world.isDaytime()) return;
            if (isKnockedOut()) return;
            if (!isIdling()) return;
            if (isTamed() && (!func_233684_eK_() || !isWithinHomeDistanceCurrentPosition())) return;
            setSleeping(getRNG().nextInt(300) == 0);
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (world.isRemote && key.equals(BREATHING_FIRE) && isBreathingFire())
            BreathSound.play(this);
        else super.notifyDataManagerChange(key);
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
        if (!world.isRemote)
            AnimationPacket.send(this, isFlying() || getRNG().nextBoolean()? BITE_ATTACK_ANIMATION : SLAP_ATTACK_ANIMATION);
    }

    @Override
    public Vector3d getApproximateMouthPos()
    {
        Vector3d position = getEyePosition(1).subtract(0, 1.3d, 0);
        double dist = (getWidth() / 2) + 3.5d;
        return position.add(getVectorForRotation(rotationPitch, rotationYawHead).mul(dist, dist, dist));
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        float heightFactor = isSleeping()? 0.5f : func_233684_eK_()? 0.9f : 1;
        return size.scale(1, heightFactor);
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
        if (backView) event.getInfo().movePosition(-8.5d, 3d, 0);
        else event.getInfo().movePosition(-5, -0.75, 0);
    }

    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || isKnockedOut(); }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return getHeight() * (isFlying()? 0.95f : 1.13f); }

    @Override
    protected boolean canBeRidden(Entity entity)
    {
        return !isKnockedOut() && entity instanceof LivingEntity && isOwner((LivingEntity) entity);
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) { return getPassengers().size() < 3; }

    @Override
    public Vector3d getPassengerPosOffset(Entity entity, int index) { return new Vector3d(0, getHeight() * 0.85f, index == 0? 0.5f : -1); }

    @Override
    public float getRenderScale() { return isChild()? 0.3f : isMale()? 0.8f : 1f; }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 8; }

    public boolean isBreathingFire() { return dataManager.get(BREATHING_FIRE); }

    public void setBreathingFire(boolean b) { if (!world.isRemote) dataManager.set(BREATHING_FIRE, b); }

    public boolean isKnockedOut() { return dataManager.get(KNOCKED_OUT); }

    public void setKnockedOut(boolean b)
    {
        dataManager.set(KNOCKED_OUT, b);
        if (!world.isRemote)
        {
            knockOutTime = b? MAX_KNOCKOUT_TIME : 0;
            if (b)
            {
                rotationYawHead = rotationYaw;
                clearAI();
                setFlying(false);
            }
        }
    }

    public void setKnockoutTime(int i)
    {
        knockOutTime = Math.max(0, i);
        if (i > 0 && !isKnockedOut()) dataManager.set(KNOCKED_OUT, true);
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier)
    {
        if (isKnockedOut()) return false;
        return super.onLivingFall(distance, damageMultiplier);
    }

    @Override
    public boolean canFly() { return super.canFly() && !isKnockedOut(); }

    @Override
    public boolean isImmuneToArrows() { return true; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return WRItems.WRTags.MEATS.getAllElements(); }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() { return WRSounds.ENTITY_ROYALRED_IDLE.get(); }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return WRSounds.ENTITY_ROYALRED_HURT.get(); }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() { return WRSounds.ENTITY_ROYALRED_DEATH.get(); }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, ROAR_ANIMATION, SLAP_ATTACK_ANIMATION, BITE_ATTACK_ANIMATION};
    }

    @Override
    public void setAnimation(Animation animation)
    {
        super.setAnimation(animation);
        if (animation == ROAR_ANIMATION) playSound(WRSounds.ENTITY_ROYALRED_ROAR.get(), 6, 1, true);
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.EXTREME_HILLS)
            event.getSpawns().func_242575_a(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.ROYAL_RED.get(), 1, 1, 1));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 100)
                .createMutableAttribute(MOVEMENT_SPEED, 0.22)
                .createMutableAttribute(KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(FOLLOW_RANGE, 60)
                .createMutableAttribute(ATTACK_KNOCKBACK, 2.25)
                .createMutableAttribute(ATTACK_DAMAGE, 10)
                .createMutableAttribute(FLYING_SPEED, 0.27)
                .createMutableAttribute(WREntities.Attributes.PROJECTILE_DAMAGE.get(), 4);
    }

    class AttackGoal extends Goal
    {
        public AttackGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldExecute()
        {
            LivingEntity target = getAttackTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            LivingEntity target = getAttackTarget();
            if (target != null && target.isAlive())
            {
                if (!isWithinHomeDistanceFromPosition(target.getPosition())) return false;
                return EntityPredicates.CAN_AI_TARGET.test(target);
            }
            return false;
        }

        @Override
        public void tick()
        {
            LivingEntity target = getAttackTarget();
            double distFromTarget = getDistanceSq(target);
            boolean isBreathingFire = isBreathingFire();
            boolean canSeeTarget = getEntitySenses().canSee(target);

            getLookController().setLookPositionWithEntity(target, 90, 30);

            boolean flag = distFromTarget > 200 && canSeeTarget && !isTamed();
            if (isBreathingFire != flag) setBreathingFire(isBreathingFire = flag);

            if (distFromTarget <= 24)
            {
                if (noActiveAnimation() && !isBreathingFire && canSeeTarget)
                {
                    renderYawOffset = rotationYaw = (float) Mafs.getAngle(RoyalRedEntity.this, target) + 90;
                    meleeAttack();
                }
            }
            else if (distFromTarget > 900) setFlying(true);

            if (getNavigator().noPath() || ticksExisted % 10 == 0)
                getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosY() + (isFlying()? 8 : 0), target.getPosZ(), !isFlying() && isBreathingFire? 0.8d : 1.2d);
        }
    }
}
