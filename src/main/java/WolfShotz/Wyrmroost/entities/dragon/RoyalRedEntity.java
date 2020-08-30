package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.projectile.breath.FireBreathEntity;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.network.packets.KeybindPacket;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumSet;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class RoyalRedEntity extends AbstractDragonEntity
{
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
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(100d); // 50 hearts
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.22d);
        getAttribute(KNOCKBACK_RESISTANCE).setBaseValue(1); // no knockback
        getAttribute(FOLLOW_RANGE).setBaseValue(60d);
        getAttribute(ATTACK_KNOCKBACK).setBaseValue(2.25d); // normal * 2.25
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(10d); // 5 hearts
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.06d);
        getAttributes().registerAttribute(PROJECTILE_DAMAGE).setBaseValue(4d); // 2 hearts
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        dataManager.register(BREATHING_FIRE, false);
        dataManager.register(KNOCKED_OUT, false);
        dataManager.register(FLYING, false);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(4, new MoveToHomeGoal(this));
        goalSelector.addGoal(5, new AttackGoal());
        goalSelector.addGoal(6, CommonGoalWrappers.followOwner(this, 1.2d, 12f, 3f));
        goalSelector.addGoal(7, new DragonBreedGoal(this, true));
        goalSelector.addGoal(9, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 10f));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, LivingEntity.class, false, true, e -> e instanceof PlayerEntity || e instanceof AnimalEntity));
        targetSelector.addGoal(4, new HurtByTargetGoal(this)
        {
            @Override
            public boolean shouldExecute() { return super.shouldExecute() && !isChild(); }
        });
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch)
    {
        super.playSound(soundIn, volume, pitch);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();
        flightTimer.add(isFlying()? 0.1f : -0.05f);
        sitTimer.add(isSitting()? 0.1f : -0.1f);
        sleepTimer.add(isSleeping()? 0.035f : -0.1f);
        breathTimer.add(isBreathingFire()? 0.15f : -0.2f);
        knockOutTimer.add(isKnockedOut()? 0.05f : -0.1f);

        if (!world.isRemote)
        {
            if (isBreathingFire() && getControllingPlayer() == null && getAttackTarget() == null)
                setBreathingFire(false);

            if (breathTimer.get() == 1)
            {
                world.addEntity(new FireBreathEntity(this));
//                if (ticksExisted % 10 == 0) playSound(WRSounds.ENTITY_ROYALRED_BREATH.get(), 1, 0.5f);
            }

            if (noActiveAnimation() && !isKnockedOut() && !isSleeping() && !isBreathingFire() && !isChild() && getRNG().nextInt(2250) == 0)
                AnimationPacket.send(this, ROAR_ANIMATION);

            if (isKnockedOut() && --knockOutTime <= 0) setKnockedOut(false);
        }

        Animation anim = getAnimation();
        int animTime = getAnimationTick();

        if (anim == ROAR_ANIMATION)
        {
            for (LivingEntity entity : getEntitiesNearby(10, this::isOnSameTeam))
                entity.addPotionEffect(new EffectInstance(Effects.STRENGTH, 60));
        }
        else if (anim == SLAP_ATTACK_ANIMATION && (animTime == 10 || animTime == 15))
        {
            attackInFront(0.2);
            if (animTime == 10) playSound(WRSounds.ENTITY_ROYALRED_HURT.get(), 1, 1, true);
        }
        else if (anim == BITE_ATTACK_ANIMATION && animTime == 4)
        {
            attackInFront(-0.3);
            playSound(WRSounds.ENTITY_ROYALRED_HURT.get(), 1, 1, true);
        }
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (!isTamed() && isKnockedOut() && isFoodItem(stack))
        {
            if (!world.isRemote)
            {
                if (knockOutTime <= (MAX_KNOCKOUT_TIME / 2))
                {
                    // base taming chances on consciousness; the closer it is to waking up the better the chances
                    if (tame(getRNG().nextInt(knockOutTime) < MAX_KNOCKOUT_TIME * 0.25d, player))
                    {
                        setKnockedOut(false);
                        AnimationPacket.send(this, ROAR_ANIMATION);
                    }
                    else knockOutTime += 600; // add 30 seconds to knockout time
                    eat(stack);
                    return true;
                }
            }
            else
                return true; //client is not aware of the knockout timer. todo in 1.16: take advantage of ActionResultType
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
            if (isTamed() && (!isSitting() || !isWithinHomeDistanceCurrentPosition())) return;
            setSleeping(getRNG().nextInt(300) == 0);
        }
    }

    @Override
    public void recievePassengerKeybind(int key, int mods, boolean pressed)
    {
        if (!noActiveAnimation()) return;

        if (key == KeybindPacket.MOUNT_KEY1 && pressed)
        {
            if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) setAnimation(ROAR_ANIMATION);
            else meleeAttack();
        }

        if (key == KeybindPacket.MOUNT_KEY2) setBreathingFire(pressed);
    }

    public void meleeAttack()
    {
        if (world.isRemote) return;
        AnimationPacket.send(this, isFlying() || getRNG().nextBoolean()? BITE_ATTACK_ANIMATION : SLAP_ATTACK_ANIMATION);
    }

    @Override
    public Vec3d getApproximateMouthPos()
    {
        Vec3d position = getEyePosition(1).subtract(0, 1.3d, 0);
        double dist = (getWidth() / 2) + 3.5d;
        return position.add(getVectorForRotation(rotationPitch, rotationYawHead).mul(dist, dist, dist));
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        float heightFactor = isSleeping()? 0.5f : isSitting()? 0.9f : 1;
        return size.scale(1, heightFactor);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        super.addScreenInfo(screen);
        screen.addAction(StaffAction.TARGET);
    }

    @Override
    protected boolean isMovementBlocked() { return super.isMovementBlocked() || isKnockedOut(); }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return getHeight() * (isFlying()? 0.95f : 1.13f); }

    @Override
    protected boolean canBeRidden(Entity entity) { return entity instanceof LivingEntity && isOwner((LivingEntity) entity); }

    @Override
    protected boolean canFitPassenger(Entity passenger) { return getPassengers().size() < 3; }

    @Override
    public Vec3d getPassengerPosOffset(Entity entity, int index) { return new Vec3d(0, getHeight() * 0.95f, index == 0? 0.5f : -1); }

    @Override
    public float getRenderScale() { return isChild()? 0.3f : isMale()? 0.8f : 1f; }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 5 : 8; }

    public boolean isBreathingFire() { return dataManager.get(BREATHING_FIRE); }

    public void setBreathingFire(boolean b) { dataManager.set(BREATHING_FIRE, b); }

    public boolean isKnockedOut() { return dataManager.get(KNOCKED_OUT); }

    public void setKnockedOut(boolean b)
    {
        dataManager.set(KNOCKED_OUT, b);
        if (!world.isRemote)
        {
            knockOutTime = b? MAX_KNOCKOUT_TIME : 0;
            if (b)
            {
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
    public boolean shouldFly() { return super.shouldFly() && !isKnockedOut(); }

    @Override
    public boolean isImmuneToArrows() { return true; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return WRItems.Tags.MEATS.getAllElements(); }

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
    public Animation[] getAnimations() { return new Animation[] {NO_ANIMATION, ROAR_ANIMATION, SLAP_ATTACK_ANIMATION, BITE_ATTACK_ANIMATION}; }

    @Override
    public void setAnimation(Animation animation)
    {
        super.setAnimation(animation);
        if (animation == ROAR_ANIMATION) playSound(WRSounds.ENTITY_ROYALRED_ROAR.get(), 6, 1);
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
        public void resetTask() { setAttackTarget(null); }

        @Override
        public void tick()
        {
            if (isFlying()) tickFlightAttacking();
            else tickGroundAttacking();
        }

        private void tickGroundAttacking()
        {
            LivingEntity target = getAttackTarget();
            double distFromTarget = getDistanceSq(target);
            boolean isBreathingFire = isBreathingFire();
            boolean canSeeTarget = getEntitySenses().canSee(target);

            getLookController().setLookPositionWithEntity(target, 90, 30);

            boolean flag = distFromTarget > 225 && canSeeTarget;
            if (isBreathingFire != flag) setBreathingFire(isBreathingFire = flag);

            if (distFromTarget <= 16)
            {
                if (noActiveAnimation() && !isBreathingFire && canSeeTarget) meleeAttack();
            }
            else if (distFromTarget > 900)
            {
                Vec3d vec3d = RandomPositionGenerator.findAirTarget(RoyalRedEntity.this, 20, (int) ((target.getPosY() - getPosY()) + 20), getLook(0), Mafs.PI / 2f, 5, 2);
                if (vec3d != null)
                {
                    setFlying(true);
                    setBreathingFire(false);
                    getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1);
                    return;
                }
            }
            getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosY(), target.getPosZ(), isBreathingFire? 0.8d : 1.2d);
        }

        private void tickFlightAttacking()
        {
            LivingEntity target = getAttackTarget();
            boolean isBreathingFire = isBreathingFire();
            boolean canSeeTarget = getEntitySenses().canSee(target);
            BlockPos navPos = getNavigator().getTargetPos();
            boolean distanceFlag = false;

            if (navPos != null)
            {
                double distToNav = getPosition().distanceSq(navPos);
                double targetToNav = target.getPosition().distanceSq(navPos);
                if (distanceFlag = distToNav - targetToNav > 4)
                    getLookController().setLookPositionWithEntity(target, 90, 90);
                else getLookController().setLookPosition(navPos.getX(), target.getPosY(), navPos.getZ(), 90, 90);
            }

            boolean breathFireFlag = canSeeTarget && distanceFlag;
            if (isBreathingFire != breathFireFlag) setBreathingFire(breathFireFlag);

            Vec3d vec3d = new Vec3d(target.getPosX() - getPosX(), 0, target.getPosZ() - target.getPosZ());
            vec3d.rotateYaw((float) (Mafs.nextDouble(getRNG()) * 5f)); // offset a little
            double length = vec3d.length();
            final double size = 30;
            if (getNavigator().noPath())
                getNavigator().tryMoveToXYZ(vec3d.x / length * size + target.getPosX(), getPosY() + Mafs.nextDouble(getRNG()) * 2, vec3d.z / length * size + target.getPosZ(), 3);
        }
    }
}
