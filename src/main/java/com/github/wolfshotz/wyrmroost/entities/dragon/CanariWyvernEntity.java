package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.*;
import com.github.wolfshotz.wyrmroost.entities.util.EntityDataEntry;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static net.minecraft.entity.ai.attributes.Attributes.*;

public class CanariWyvernEntity extends AbstractDragonEntity
{
    public static final Animation FLAP_WINGS_ANIMATION = new Animation(22);
    public static final Animation PREEN_ANIMATION = new Animation(36);
    public static final Animation THREAT_ANIMATION = new Animation(40);
    public static final Animation ATTACK_ANIMATION = new Animation(15);

    public PlayerEntity pissedOffTarget;

    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, level);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, true);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void initGoals()
    {
        super.initGoals();

        goalSelector.add(3, new MoveToHomeGoal(this));
        goalSelector.add(4, new AttackGoal());
        goalSelector.add(5, new ThreatenGoal());
        goalSelector.add(6, new WRFollowOwnerGoal(this));
        goalSelector.add(7, new DragonBreedGoal(this));
        goalSelector.add(8, new FlyerWanderGoal(this, 1));
        goalSelector.add(9, new LookAtGoal(this, LivingEntity.class, 8f));
        goalSelector.add(10, new LookRandomlyGoal(this));

        targetSelector.add(0, new OwnerHurtByTargetGoal(this));
        targetSelector.add(1, new OwnerHurtTargetGoal(this));
        targetSelector.add(2, new DefendHomeGoal(this));
        targetSelector.add(3, new HurtByTargetGoal(this));
    }

    @Override
    protected BodyController createBodyControl()
    {
        return new BodyController(this);
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        dataTracker.startTracking(FLYING, false);
    }

    @Override
    public void tickMovement()
    {
        super.tickMovement();

        if (!level.isClientSide && !isPissed() && !isSleeping() && !isFlying() && !isRiding() && noActiveAnimation())
        {
            double rand = getRandom().nextDouble();
            if (rand < 0.001) AnimationPacket.send(this, FLAP_WINGS_ANIMATION);
            else if (rand < 0.002) AnimationPacket.send(this, PREEN_ANIMATION);
        }

        if (getAnimation() == FLAP_WINGS_ANIMATION)
        {
            int tick = getAnimationTick();
            if (tick == 5 || tick == 12) playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 0.7f, 2, true);
            if (!level.isClientSide && tick == 9 && getRandom().nextDouble() <= 0.25)
                dropStack(new ItemStack(Items.FEATHER), 0.5f);
        }
        else if (getAnimation() == THREAT_ANIMATION && isPissed())
        {
            yRot = bodyYaw = headYaw = (float) Mafs.getAngle(CanariWyvernEntity.this, pissedOffTarget) - 270f;
        }
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        ActionResultType result = super.playerInteraction(player, hand, stack);
        if (result.isAccepted()) return result;

        if (!isTame() && isFoodItem(stack) && (isPissed() || player.isCreative() || isBaby()))
        {
            eat(stack);
            if (!level.isClientSide) tame(getRandom().nextDouble() < 0.2, player);
            return ActionResultType.success(level.isClientSide);
        }

        if (isOwner(player) && player.getPassengers().size() < 3 && !player.isSneaking() && !isLeashed())
        {
            setSitting(true);
            setFlying(false);
            clearAI();
            startRiding(player, true);
            return ActionResultType.success(level.isClientSide);
        }

        return ActionResultType.PASS;
    }

    @Override
    public boolean tryAttack(Entity entity)
    {
        if (super.tryAttack(entity) && entity instanceof LivingEntity)
        {
            int i = 5;
            switch (level.getDifficulty())
            {
                case HARD:
                    i = 15; break;
                case NORMAL:
                    i = 8; break;
                default:
                    break;
            }
            ((LivingEntity) entity).addStatusEffect(new EffectInstance(Effects.POISON, i * 20));
            return true;
        }
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source == DamageSource.MAGIC || super.isInvulnerableTo(source);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        super.addScreenInfo(screen);
        screen.addAction(StaffAction.TARGET);
    }

    @Override
    public boolean shouldSleep()
    {
        return !isPissed() && super.shouldSleep();
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
    public int determineVariant()
    {
        return getRandom().nextInt(5);
    }

    @Override
    public int getYawRotationSpeed()
    {
        return isFlying()? 12 : 75;
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.getItem() == Items.SWEET_BERRIES;
    }

    public boolean isPissed()
    {
        return pissedOffTarget != null;
    }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.SWAMP)
            event.getSpawns().spawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.CANARI_WYVERN.get(), 9, 2, 5));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(GENERIC_MAX_HEALTH, 12).add(GENERIC_MOVEMENT_SPEED, 0.2).add(GENERIC_FLYING_SPEED, 0.1).add(GENERIC_ATTACK_DAMAGE, 3);
    }

    public class ThreatenGoal extends Goal
    {
        public PlayerEntity target;

        public ThreatenGoal()
        {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
        }

        @Override
        public boolean canUse()
        {
            if (isTamed()) return false;
            if (isFlying()) return false;
            if (getTarget() != null) return false;
            if ((target = level.getClosestPlayer(getX(), getY(), getZ(), 12d, true)) == null)
                return false;
            return canTarget(target);
        }

        @Override
        public void tick()
        {
            double distFromTarget = squaredDistanceTo(target);
            if (distFromTarget > 30 && !isPissed())
            {
                if (getNavigation().isDone())
                {
                    Vector3d vec3d = RandomPositionGenerator.findTargetAwayFrom(CanariWyvernEntity.this, 16, 7, target.position());
                    if (vec3d != null) getNavigation().moveTo(vec3d.x, vec3d.y, vec3d.z, 1.5);
                }
            }
            else
            {
                getLookControl().setLookAt(target, 90, 90);
                if (!isPissed())
                {
                    pissedOffTarget = target;
                    AnimationPacket.send(CanariWyvernEntity.this, THREAT_ANIMATION);
                    clearAI();
                }

                if (distFromTarget < 6) setTarget(target);
            }
        }

        @Override
        public void stop()
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
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse()
        {
            LivingEntity target = getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canContinueToUse()
        {
            LivingEntity target = getTarget();
            return target != null && target.isAlive() && isInWalkTargetRange(target.blockPosition()) && EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL.test(target);
        }

        @Override
        public void tick()
        {
            LivingEntity target = getTarget();

            if ((++repathTimer >= 10 || getNavigation().isDone()) && getVisibilityCache().canSee(target))
            {
                repathTimer = 0;
                if (!isFlying()) setFlying(true);
                getNavigation().moveTo(target.getX(), target.getBoundingBox().maxY - 2, target.getZ(), 1);
                getLookControl().setLookAt(target, 90, 90);
            }

            if (--attackDelay <= 0 && squaredDistanceTo(target.position().add(0, target.getBoundingBox().getYLength(), 0)) <= 2.25 + target.getBbWidth())
            {
                attackDelay = 20 + getRandom().nextInt(10);
                AnimationPacket.send(CanariWyvernEntity.this, ATTACK_ANIMATION);
                tryAttack(target);
            }
        }

        @Override
        public void stop()
        {
            repathTimer = 10;
            attackDelay = 0;
        }
    }
}