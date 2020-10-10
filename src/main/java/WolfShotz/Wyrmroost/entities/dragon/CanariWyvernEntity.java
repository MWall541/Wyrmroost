package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.*;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import WolfShotz.Wyrmroost.registry.WRSounds;
import WolfShotz.Wyrmroost.util.Mafs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class CanariWyvernEntity extends AbstractDragonEntity
{
    public static final Animation FLAP_WINGS_ANIMATION = new Animation(22);
    public static final Animation PREEN_ANIMATION = new Animation(36);
    public static final Animation THREAT_ANIMATION = new Animation(40);
    public static final Animation ATTACK_ANIMATION = new Animation(15);

    public PlayerEntity pissedOffTarget;

    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, true);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(12);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.2);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(3);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.2);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, new MoveToHomeGoal(this));
        goalSelector.addGoal(4, new AttackGoal());
        goalSelector.addGoal(5, new ThreatenGoal());
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this, 0));
        goalSelector.addGoal(8, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 8f));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(2, new DefendHomeGoal(this));
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
    }

    @Override
    protected BodyController createBodyController() { return new BodyController(this); }

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

        if (!world.isRemote && !isPissed() && !isSleeping() && !isFlying() && !isRiding() && noActiveAnimation())
        {
            double rand = getRNG().nextDouble();
            if (rand < 0.001) AnimationPacket.send(this, FLAP_WINGS_ANIMATION);
            else if (rand < 0.002) AnimationPacket.send(this, PREEN_ANIMATION);
        }

        if (getAnimation() == FLAP_WINGS_ANIMATION)
        {
            int tick = getAnimationTick();
            if (tick == 5 || tick == 12) playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 0.7f, 2, true);
            if (!world.isRemote && tick == 9 && getRNG().nextDouble() <= 0.25) entityDropItem(new ItemStack(Items.FEATHER), 0.5f);
        }
        else if (getAnimation() == THREAT_ANIMATION && isPissed())
        {
            rotationYaw = renderYawOffset = rotationYawHead = (float) Mafs.getAngle(CanariWyvernEntity.this, pissedOffTarget) - 270f;
        }
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (!isTamed() && isFoodItem(stack) && (isPissed() || player.isCreative() || isChild()))
        {
            eat(stack);
            if (!world.isRemote) tame(getRNG().nextDouble() < 0.2, player);
            return true;
        }

        if (isOwner(player) && player.getPassengers().size() < 3 && !player.isSneaking())
        {
            setSit(true);
            setFlying(false);
            clearAI();
            startRiding(player, true);
            return true;
        }

        return super.playerInteraction(player, hand, stack);
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
                    i = 15; break;
                case NORMAL:
                    i = 8; break;
                default:
                    break;
            }
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, i * 20));
            return true;
        }
        return false;
    }

    @Override
    public void handleSleep()
    {
        if (isSleeping())
        {
            if (world.isDaytime() && getRNG().nextInt(150) == 0) setSleeping(false);
        }
        else
        {
            if (--sleepCooldown > 0) return;
            if (isPissed()) return;
            if (isFlying()) return;
            if (world.isDaytime()) return;
            if (isTamed() && (!isSitting() || !isWithinHomeDistanceCurrentPosition())) return;
            if (!isIdling()) return;
            if (getRNG().nextInt(300) == 0) setSleeping(true);
        }
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        super.addScreenInfo(screen);
        screen.addAction(StaffAction.TARGET);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return WRSounds.ENTITY_CANARI_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return WRSounds.ENTITY_CANARI_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return WRSounds.ENTITY_CANARI_DEATH.get();
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, FLAP_WINGS_ANIMATION, PREEN_ANIMATION, THREAT_ANIMATION, ATTACK_ANIMATION};
    }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(5); }

    @Override
    public int getHorizontalFaceSpeed() { return isFlying()? 12 : 75; }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return Collections.singleton(Items.SWEET_BERRIES); }

    public boolean isPissed() { return pissedOffTarget != null; }

    public class ThreatenGoal extends Goal
    {
        public PlayerEntity target;

        public ThreatenGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
        }

        @Override
        public boolean shouldExecute()
        {
            if (isTamed()) return false;
            if (isFlying()) return false;
            if (getAttackTarget() != null) return false;
            if ((target = world.getClosestPlayer(getPosX(), getPosY(), getPosZ(), 12d, true)) == null)
                return false;
            return canAttack(target);
        }

        @Override
        public void tick()
        {
            double distFromTarget = getDistanceSq(target);
            if (distFromTarget > 30 && !isPissed())
            {
                if (getNavigator().noPath())
                {
                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(CanariWyvernEntity.this, 16, 7, target.getPositionVec());
                    if (vec3d != null) getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.5);
                }
            }
            else
            {
                getLookController().setLookPositionWithEntity(target, 90, 90);
                if (!isPissed())
                {
                    pissedOffTarget = target;
                    AnimationPacket.send(CanariWyvernEntity.this, THREAT_ANIMATION);
                    clearAI();
                }

                if (distFromTarget < 6) setAttackTarget(target);
            }
        }

        @Override
        public void resetTask()
        {
             target = null;
             pissedOffTarget = null;
        }
    }

    public class AttackGoal extends Goal
    {
        private int repathTimer = 10;
        private int attackDelay = 0;

        public AttackGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
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
            return target != null && target.isAlive() && isWithinHomeDistanceFromPosition(target.getPosition()) && EntityPredicates.CAN_AI_TARGET.test(target);
        }

        @Override
        public void tick()
        {
            LivingEntity target = getAttackTarget();

            if ((++repathTimer >= 10 || getNavigator().noPath()) && getEntitySenses().canSee(target))
            {
                repathTimer = 0;
                if (!isFlying()) setFlying(true);
                getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosYEye(), target.getPosZ(), 1);
                getLookController().setLookPositionWithEntity(target, 90, 90);
            }

            if (--attackDelay <= 0 && getDistanceSq(target) <= 2.25 + target.getWidth())
            {
                attackDelay = 20 + getRNG().nextInt(10);
                AnimationPacket.send(CanariWyvernEntity.this, ATTACK_ANIMATION);
                attackEntityAsMob(target);
            }
        }

        @Override
        public void resetTask()
        {
            repathTimer = 10;
            attackDelay = 0;
        }
    }
}