package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.model.entity.CanariWyvernModel;
import com.github.wolfshotz.wyrmroost.containers.DragonStaffContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.goals.*;
import com.github.wolfshotz.wyrmroost.entities.util.EntitySerializer;
import com.github.wolfshotz.wyrmroost.items.book.action.BookActions;
import com.github.wolfshotz.wyrmroost.network.packets.AnimationPacket;
import com.github.wolfshotz.wyrmroost.registry.WREntities;
import com.github.wolfshotz.wyrmroost.registry.WRSounds;
import com.github.wolfshotz.wyrmroost.util.Mafs;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.LogicalAnimation;
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

public class CanariWyvernEntity extends TameableDragonEntity
{
    private static final EntitySerializer<CanariWyvernEntity> SERIALIZER = TameableDragonEntity.SERIALIZER.concat(b -> b
            .track(EntitySerializer.BOOL, "Gender", TameableDragonEntity::isMale, TameableDragonEntity::setGender)
            .track(EntitySerializer.INT, "Variant", TameableDragonEntity::getVariant, TameableDragonEntity::setVariant)
            .track(EntitySerializer.BOOL, "Sleeping", TameableDragonEntity::isSleeping, TameableDragonEntity::setSleeping));

    public static final Animation FLAP_WINGS_ANIMATION = LogicalAnimation.create(22, CanariWyvernEntity::flapWingsAnimation, () -> CanariWyvernModel::flapWingsAnimation);
    public static final Animation PREEN_ANIMATION = LogicalAnimation.create(36, null, () -> CanariWyvernModel::preenAnimation);
    public static final Animation THREAT_ANIMATION = LogicalAnimation.create(40, CanariWyvernEntity::threatAnimation, () -> CanariWyvernModel::threatAnimation);
    public static final Animation ATTACK_ANIMATION = LogicalAnimation.create(15, null, () -> CanariWyvernModel::attackAnimation);

    public PlayerEntity pissedOffTarget;

    public CanariWyvernEntity(EntityType<? extends TameableDragonEntity> dragon, World level)
    {
        super(dragon, level);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, new MoveToHomeGoal(this));
        goalSelector.addGoal(4, new AttackGoal());
        goalSelector.addGoal(5, new ThreatenGoal());
        goalSelector.addGoal(6, new WRFollowOwnerGoal(this));
        goalSelector.addGoal(7, new DragonBreedGoal(this));
        goalSelector.addGoal(8, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 8f));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(2, new DefendHomeGoal(this));
        targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    protected BodyController createBodyControl()
    {
        return new BodyController(this);
    }

    @Override
    public EntitySerializer<CanariWyvernEntity> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(FLYING, false);
        entityData.define(GENDER, false);
        entityData.define(SLEEPING, false);
        entityData.define(VARIANT, 0);
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if (!level.isClientSide && !isPissed() && !isSleeping() && !isFlying() && !isRiding() && noAnimations())
        {
            double rand = getRandom().nextDouble();
            if (rand < 0.001) AnimationPacket.send(this, FLAP_WINGS_ANIMATION);
            else if (rand < 0.002) AnimationPacket.send(this, PREEN_ANIMATION);
        }
    }

    public void flapWingsAnimation(int time)
    {
        if (time == 5 || time == 12) playSound(SoundEvents.PHANTOM_FLAP, 0.7f, 2, true);
        if (!level.isClientSide && time == 9 && getRandom().nextDouble() <= 0.25)
            spawnAtLocation(new ItemStack(Items.FEATHER), 0.5f);
    }

    public void threatAnimation(int time)
    {
        if (isPissed())
            yRot = yBodyRot = yHeadRot = (float) Mafs.getAngle(CanariWyvernEntity.this, pissedOffTarget) - 270f;
    }

    @Override
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        ActionResultType result = super.playerInteraction(player, hand, stack);
        if (result.consumesAction()) return result;

        if (!isTame() && isFood(stack) && (isPissed() || player.isCreative() || isHatchling()))
        {
            eat(stack);
            if (!level.isClientSide) tame(getRandom().nextDouble() < 0.2, player);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        if (isOwnedBy(player) && player.getPassengers().size() < 3 && !player.isShiftKeyDown() && !isLeashed())
        {
            setOrderedToSit(true);
            setFlying(false);
            clearAI();
            startRiding(player, true);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }

        return ActionResultType.PASS;
    }

    @Override
    public boolean doHurtTarget(Entity entity)
    {
        if (super.doHurtTarget(entity) && entity instanceof LivingEntity)
        {
            int i = 5;
            switch (level.getDifficulty())
            {
                case HARD:
                    i = 15;
                    break;
                case NORMAL:
                    i = 8;
                    break;
                default:
                    break;
            }
            ((LivingEntity) entity).addEffect(new EffectInstance(Effects.POISON, i * 20));
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
    public void applyStaffInfo(DragonStaffContainer container)
    {
        super.applyStaffInfo(container);
        container.addStaffActions(BookActions.TARGET);
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
        return new Animation[]{NO_ANIMATION, FLAP_WINGS_ANIMATION, PREEN_ANIMATION, THREAT_ANIMATION, ATTACK_ANIMATION};
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
    public boolean isFood(ItemStack stack)
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
            event.getSpawns().addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.CANARI_WYVERN.get(), 9, 2, 5));
    }

    public static AttributeModifierMap.MutableAttribute getAttributeMap()
    {
        return MobEntity.createMobAttributes()
                .add(MAX_HEALTH, 12)
                .add(MOVEMENT_SPEED, 0.2)
                .add(FLYING_SPEED, 0.1)
                .add(ATTACK_DAMAGE, 3);
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
            if (isTame()) return false;
            if (isFlying()) return false;
            if (getTarget() != null) return false;
            if ((target = level.getNearestPlayer(getX(), getY(), getZ(), 12d, true)) == null)
                return false;
            return canAttack(target);
        }

        @Override
        public void tick()
        {
            double distFromTarget = distanceToSqr(target);
            if (distFromTarget > 30 && !isPissed())
            {
                if (getNavigation().isDone())
                {
                    Vector3d vec3d = RandomPositionGenerator.getPosAvoid(CanariWyvernEntity.this, 16, 7, target.position());
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
            return target != null && target.isAlive() && isWithinRestriction(target.blockPosition()) && EntityPredicates.ATTACK_ALLOWED.test(target);
        }

        @Override
        public void tick()
        {
            LivingEntity target = getTarget();

            if ((++repathTimer >= 10 || getNavigation().isDone()) && getSensing().canSee(target))
            {
                repathTimer = 0;
                if (!isFlying()) setFlying(true);
                getNavigation().moveTo(target.getX(), target.getBoundingBox().maxY - 2, target.getZ(), 1);
                getLookControl().setLookAt(target, 90, 90);
            }

            if (--attackDelay <= 0 && distanceToSqr(target.position().add(0, target.getBoundingBox().getYsize(), 0)) <= 2.25 + target.getBbWidth())
            {
                attackDelay = 20 + getRandom().nextInt(10);
                AnimationPacket.send(CanariWyvernEntity.this, ATTACK_ANIMATION);
                doHurtTarget(target);
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