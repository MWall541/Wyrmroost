package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.LessShitLookController;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.*;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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

    public boolean isPissed = false;

    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        moveController = new MoveController();

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
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.3);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();

        goalSelector.addGoal(3, new MoveToHomeGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 2, false));
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
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, e -> getDistanceSq(e) <= 9));
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

        if (!world.isRemote && !isPissed && !isSleeping() && !isFlying() && !isRiding() && noActiveAnimation())
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
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (!isTamed() && isFoodItem(stack))
        {
            eat(stack);
            if (!world.isRemote) tame(getRNG().nextDouble() < 0.2, player);
            return true;
        }

        if (isOwner(player) && player.getPassengers().size() < 3)
        {
            if (!world.isRemote)
            {
                setSitting(true);
                setFlying(false);
                clearAI();
                startRiding(player, true);
            }
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
            if (isPissed) return;
            if (world.isDaytime()) return;
            if (isTamed() && (!isSitting() || !isWithinHomeDistanceCurrentPosition())) return;
            if (!isIdling()) return;
            if (getRNG().nextInt(300) == 0) setSleeping(true);
        }
    }

    @Override
    public void swingArm(Hand hand)
    {
        super.swingArm(hand);
        setAnimation(ATTACK_ANIMATION);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        super.addScreenInfo(screen);
        screen.addAction(StaffAction.TARGET);
    }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, FLAP_WINGS_ANIMATION, PREEN_ANIMATION, THREAT_ANIMATION, ATTACK_ANIMATION};
    }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(5); }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return Collections.singleton(Items.SWEET_BERRIES); }

    private class MoveController extends MovementController
    {
        private MoveController()
        {
            super(CanariWyvernEntity.this);
        }

        public void tick()
        {
            if (action == Action.MOVE_TO)
            {
                action = Action.WAIT;

                double x = posX - getPosX();
                double y = posY - getPosY();
                double z = posZ - getPosZ();
                double distSq = x * x + y * y + z * z;
                if (distSq < 2.5000003E-7)
                {
                    setMoveForward(0f);
                    return;
                }

                if (distSq > 16) setFlying(true);

                if (isFlying())
                {
                    rotationYawHead = limitAngle(rotationYawHead, (float) Math.toDegrees(MathHelper.atan2(z, x)) - 90f, getHorizontalFaceSpeed() * 3);
                    rotationYaw = limitAngle(rotationYaw, rotationYawHead, getHorizontalFaceSpeed());
                    ((LessShitLookController) getLookController()).freeze();
                    float speed = (float) this.speed * getTravelSpeed();
                    setAIMoveSpeed(speed);
                    setMoveVertical(y > 0? speed : -speed);
                }
                else super.tick();
            }
            else
            {
                setAIMoveSpeed(0);
                setMoveStrafing(0);
                setMoveVertical(0);
                setMoveForward(0);
            }
        }
    }

    public class ThreatenGoal extends Goal
    {
        public PlayerEntity target;

        public ThreatenGoal()
        {
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean shouldExecute()
        {
            return !isTamed() && getAttackTarget() == null && (target = world.getClosestPlayer(CanariWyvernEntity.this, 12d)) != null;
        }

        @Override
        public void tick()
        {
            double distFromTarget = getDistanceSq(target);
            if (distFromTarget > 15 && !isPissed)
            {
                if (getNavigator().noPath())
                {
                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(CanariWyvernEntity.this, 16, 7, target.getPositionVec());
                    if (vec3d != null) getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1);
                }
            }
            else
            {
                getLookController().setLookPositionWithEntity(target, 90, 90);
                if (!isPissed)
                {
                    isPissed = true;
                    AnimationPacket.send(CanariWyvernEntity.this, THREAT_ANIMATION);
                    clearAI();
                }
            }
        }

        @Override
        public void resetTask()
        {
             target = null;
             isPissed = false;
        }
    }
}