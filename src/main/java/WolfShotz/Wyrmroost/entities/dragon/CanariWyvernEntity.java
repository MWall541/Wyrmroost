package WolfShotz.Wyrmroost.entities.dragon;

import WolfShotz.Wyrmroost.client.screen.StaffScreen;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DefendHomeGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.DragonBreedGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.FlyerWanderGoal;
import WolfShotz.Wyrmroost.entities.dragon.helpers.ai.goals.MoveToHomeGoal;
import WolfShotz.Wyrmroost.entities.util.CommonGoalWrappers;
import WolfShotz.Wyrmroost.entities.util.EntityDataEntry;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;

import static net.minecraft.entity.SharedMonsterAttributes.*;

public class CanariWyvernEntity extends AbstractDragonEntity
{
    public static final Animation FLAP_WINGS_ANIMATION = new Animation(22);
    public static final Animation PREEN_ANIMATION = new Animation(36);
    public static final Animation THREAT_ANIMATION = new Animation(40);
    public static final Animation ATTACK_ANIMATION = new Animation(15);

    public CanariWyvernEntity(EntityType<? extends AbstractDragonEntity> dragon, World world)
    {
        super(dragon, world);

        lookController = new LookController(this);

        setImmune(DamageSource.MAGIC);

        registerDataEntry("Gender", EntityDataEntry.BOOLEAN, GENDER, getRNG().nextBoolean());
        registerDataEntry("Sleeping", EntityDataEntry.BOOLEAN, SLEEPING, false);
        registerDataEntry("Variant", EntityDataEntry.INTEGER, VARIANT, 0);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        goalSelector.addGoal(3, new MoveToHomeGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, true));
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 7, 1, 4, true));
        goalSelector.addGoal(6, new DragonBreedGoal(this, 0));
        goalSelector.addGoal(7, new AvoidGoal());
        goalSelector.addGoal(7, new FlyerWanderGoal(this, 1));
        goalSelector.addGoal(8, new LookAtGoal(this, LivingEntity.class, 5f));
        goalSelector.addGoal(9, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(3, new DefendHomeGoal(this));
        targetSelector.addGoal(5, CommonGoalWrappers.nonTamedTarget(this, PlayerEntity.class, false));
        targetSelector.addGoal(4, new HurtByTargetGoal(this)
        {
            @Override
            public boolean shouldExecute() { return !isChild() && super.shouldExecute(); }
        });
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) { return new FlyingPathNavigator(this, worldIn); }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();

        getAttribute(MAX_HEALTH).setBaseValue(16d);
        getAttribute(MOVEMENT_SPEED).setBaseValue(0.2d);
        getAttribute(FOLLOW_RANGE).setBaseValue(2);
        getAttributes().registerAttribute(ATTACK_DAMAGE).setBaseValue(3d);
        getAttributes().registerAttribute(FLYING_SPEED).setBaseValue(0.3);
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

        if (!isSleeping() && !isFlying() && !isRiding() && noActiveAnimation())
        {
            if (getRNG().nextInt(650) == 0) setAnimation(FLAP_WINGS_ANIMATION);
            else if (getRNG().nextInt(350) == 0) setAnimation(PREEN_ANIMATION);
        }

        if (getAnimation() == FLAP_WINGS_ANIMATION)
        {
            int tick = getAnimationTick();
            if (tick == 5 || tick == 12) playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 0.7f, 2, true);
            if (tick == 9 && getRNG().nextInt(25) == 0)
                entityDropItem(new ItemStack(Items.FEATHER), 0.5f);
        }
    }

    @Override
    public boolean playerInteraction(PlayerEntity player, Hand hand, ItemStack stack)
    {
        if (super.playerInteraction(player, hand, stack)) return true;

        if (!isTamed() && isFoodItem(stack))
        {
            eat(stack);
            tame(getRNG().nextInt(5) == 0, player);
            return true;
        }

        if (isOwner(player) && player.getPassengers().size() < 3)
        {
            setSitting(true);
            setFlying(false);
            clearAI();
            startRiding(player, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        boolean should = super.attackEntityAsMob(entity);
        if (should && entity instanceof LivingEntity)
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 200));
        return should;
    }

    @Override
    public EntitySize getSize(Pose poseIn)
    {
        EntitySize size = getType().getSize().scale(getRenderScale());
        if (isSitting() || isSleeping()) size = size.scale(1, 0.95f);
        return size;
    }

    @Override
    public void swingArm(Hand hand)
    {
        super.swingArm(hand);
        AnimationPacket.send(this, ATTACK_ANIMATION);
    }

    @Override
    public void addScreenInfo(StaffScreen screen)
    {
        super.addScreenInfo(screen);
        screen.addAction(StaffAction.TARGET);
    }

    @Override
    public int getVariantForSpawn() { return getRNG().nextInt(5); }

    @Override
    public Collection<? extends IItemProvider> getFoodItems() { return Collections.singleton(Items.SWEET_BERRIES); }

    @Override
    public Animation[] getAnimations()
    {
        return new Animation[] {NO_ANIMATION, FLAP_WINGS_ANIMATION, PREEN_ANIMATION, THREAT_ANIMATION, ATTACK_ANIMATION};
    }

    class AvoidGoal extends AvoidEntityGoal<PlayerEntity>
    {
        public PlayerEntity prevTarget;

        public AvoidGoal() { super(CanariWyvernEntity.this, PlayerEntity.class, 6, 1, 1.5); }

        @Override
        public boolean shouldExecute()
        {
            boolean should = super.shouldExecute();
            prevTarget = avoidTarget;
            return should;
        }

        @Override
        public void startExecuting()
        {
            super.startExecuting();
            AnimationPacket.send(CanariWyvernEntity.this, THREAT_ANIMATION);
        }
    }
}
