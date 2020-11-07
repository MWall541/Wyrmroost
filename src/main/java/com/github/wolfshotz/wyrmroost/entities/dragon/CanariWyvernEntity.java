package com.github.wolfshotz.wyrmroost.entities.dragon;

import com.github.wolfshotz.wyrmroost.client.screen.StaffScreen;
import com.github.wolfshotz.wyrmroost.entities.dragon.helpers.ai.SleepController;
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
        super(dragon, world);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, true);
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
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
        targetSelector.addGoal(4, new HurtByTargetGoal(this));
    }

    @Override
    protected BodyController createBodyController()
    {
        return new BodyController(this);
    }

    @Override
    protected SleepController createSleepController()
    {
        return new SleepController(this).setHomeDefender().addSleepCondition(() -> !isPissed());
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
    public ActionResultType playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        ActionResultType result = super.playerInteraction(player, hand, stack);
        if (result.isSuccessOrConsume()) return result;

        if (!isTamed() && isFoodItem(stack) && (isPissed() || player.isCreative() || isChild()))
        {
            eat(stack);
            if (!world.isRemote) tame(getRNG().nextDouble() < 0.2, player);
            return ActionResultType.func_233537_a_(world.isRemote);
        }

        if (isOwner(player) && player.getPassengers().size() < 3 && !player.isSneaking())
        {
            setSit(true);
            setFlying(false);
            clearAI();
            startRiding(player, true);
            return ActionResultType.func_233537_a_(world.isRemote);
        }

        return ActionResultType.PASS;
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
    public int determineVariant() { return getRNG().nextInt(5); }

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

    public boolean isPissed() { return pissedOffTarget != null; }

    public static void setSpawnBiomes(BiomeLoadingEvent event)
    {
        if (event.getCategory() == Biome.Category.SWAMP)
            event.getSpawns().func_242575_a(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(WREntities.CANARI_WYVERN.get(), 9, 2, 5));
    }

    public static AttributeModifierMap.MutableAttribute getAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(MAX_HEALTH, 12)
                .createMutableAttribute(MOVEMENT_SPEED, 0.2)
                .createMutableAttribute(FLYING_SPEED, 0.1)
                .createMutableAttribute(ATTACK_DAMAGE, 3);
    }

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
                    Vector3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(CanariWyvernEntity.this, 16, 7, target.getPositionVec());
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
                getNavigator().tryMoveToXYZ(target.getPosX(), target.getBoundingBox().maxY - 2, target.getPosZ(), 1);
                getLookController().setLookPositionWithEntity(target, 90, 90);
            }

            if (--attackDelay <= 0 && getDistanceSq(target.getPositionVec().add(0, target.getBoundingBox().getYSize(), 0)) <= 2.25 + target.getWidth())
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