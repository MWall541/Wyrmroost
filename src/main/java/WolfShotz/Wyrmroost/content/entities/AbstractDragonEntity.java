package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.content.entities.ai.FlightMovementController;
import WolfShotz.Wyrmroost.content.entities.ai.FlightPathNavigator;
import WolfShotz.Wyrmroost.util.MathUtils;
import WolfShotz.Wyrmroost.util.NetworkUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatedEntity
{
    protected int animationTick;
    public int flyingThreshold = 3;
    public int hatchTimer; // Used in subclasses for hatching time
    protected List<String> immunes = new ArrayList<>();
    public boolean isSpecialAttacking = false;

    // Dragon Entity Animations
    public Animation animation = NO_ANIMATION;
    public static Animation SLEEP_ANIMATION;
    public static Animation WAKE_ANIMATION;

    // Dragon Entity Data
    public static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> ALBINO = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);


    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
        setTamed(false);

        moveController = new FlightMovementController(this);

        stepHeight = 1;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(3, sitGoal = new SitGoal(this));
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();

        dataManager.register(GENDER, getRNG().nextBoolean());
        dataManager.register(ALBINO, getAlbinoChances() != 0 && getRNG().nextInt(getAlbinoChances()) == 0);
        dataManager.register(SADDLED, false);
        dataManager.register(FLYING, false);
        dataManager.register(SLEEPING, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("gender", getGender());
        compound.putBoolean("albino", isAlbino());
        compound.putBoolean("saddled", isSaddled());
        compound.putBoolean("sleeping", isSleeping());
    
        super.writeAdditional(compound);
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        setGender(compound.getBoolean("gender"));
        setAlbino(compound.getBoolean("albino"));
        setSaddled(compound.getBoolean("saddled"));
        dataManager.set(SLEEPING, compound.getBoolean("sleeping")); // Use data manager: Setter method controls animation
    
        super.readAdditional(compound);
    }

    /**
     * Gets the Gender of the dragonEntity. <P>
     * true = Male | false = Female. Anything else is an abomination.
     */
    public boolean getGender() { return dataManager.get(GENDER); }
    public void setGender(boolean sex) { dataManager.set(GENDER, sex); }

    /**
     * Whether or not this dragonEntity is albino. true == isAlbino, false == is not
     */
    public boolean isAlbino() { return dataManager.get(ALBINO); }
    public void setAlbino(boolean albino) { dataManager.set(ALBINO, albino); }
    /**
     * Set The chances this dragon can be an albino.
     * Set it to 0 to have no chance.
     * Lower values have greater chances
     */
    public abstract int getAlbinoChances();

    /**
     * Whether or not the dragon is saddled
     */
    public boolean isSaddled() { return dataManager.get(SADDLED); }
    public void setSaddled(boolean saddled) { dataManager.set(SADDLED, saddled); }

    /**
     * Whether or not the dragon is flying
     */
    public boolean isFlying() { return dataManager.get(FLYING); }
    /**
     * Whether or not the dragon can fly.
     * For ground entities, return false
     */
    public boolean canFly() { return !isChild(); }
    public void setFlying(boolean fly) {
        if (canFly() && fly) {
            dataManager.set(FLYING, true);
            switchPathController(true);
            liftOff();
        } else {
            dataManager.set(FLYING, false);
            switchPathController(false);
        }
    }
    
    /**
     * Whether or not the dragon is sleeping.
     */
    @Override
    public boolean isSleeping() { return dataManager.get(SLEEPING); }
    /**
     * Sleep setter for dragon.
     * If we have a sleep animation, then play it.
     */
    public void setSleeping(boolean sleep) {
        dataManager.set(SLEEPING, sleep);
        
        if (SLEEP_ANIMATION != null && WAKE_ANIMATION != null)
            NetworkUtils.sendAnimationPacket(this, sleep? SLEEP_ANIMATION : WAKE_ANIMATION);
    
        recalculateSize(); // Change the hitbox for sitting / sleeping
    }
    
    public void setSit(boolean sitting) {
        if (!world.isRemote) {
            sitGoal.setSitting(sitting);
            isJumping = false;
            navigator.clearPath();
            setAttackTarget(null);
        }
        
        super.setSitting(sitting);
    
        recalculateSize(); // Change the hitbox for sitting / sleeping
    }

    /**
     *  Whether or not the dragonEntity is pissed or not.
     */
    public boolean isAngry() { return (dataManager.get(TAMED) & 2) != 0; }
    public void setAngry(boolean angry) {
        byte b0 = dataManager.get(TAMED);

        if (angry) dataManager.set(TAMED, (byte) (b0 | 2));
        else dataManager.set(TAMED, (byte) (b0 & -3));
    }

    // ================================

    /**
     * Called frequently so the entity can update its state every tick as required.
     */
    @Override
    public void livingTick() {
        if (MathUtils.getAltitude(this) > flyingThreshold && canFly() && !isFlying()) setFlying(true);
        if (MathUtils.getAltitude(this) <= flyingThreshold - 1 && isFlying()) setFlying(false);
        
        if (!world.isRemote) {
    
            // world time is always day on client, so we need to sync sleeping from server to client with sleep getter...
            if (!world.isDaytime() && !isSleeping() && getAttackTarget() == null && getNavigator().noPath() && !isFlying() && !isBeingRidden() && getRNG().nextInt(500) == 0)
                setSleeping(true);
            if (world.isDaytime() && isSleeping() && getRNG().nextInt(150) == 0)
                setSleeping(false);
        }

        super.livingTick();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
    
        if (getAnimation() != NO_ANIMATION) {
            ++animationTick;
            if (animationTick >= animation.getDuration()) setAnimation(NO_ANIMATION);
        }
    }

    public void switchPathController(boolean flying) {
        if (flying) {
            moveController = new FlightMovementController(this);
            navigator = new FlightPathNavigator(this, world);
        } else {
            moveController = new MovementController(this);
            navigator = new GroundPathNavigator(this, world);
        }
    }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        if (stack.getItem() == Items.STICK) setSleeping(!isSleeping());
        
        if (stack.getItem() == Items.NAME_TAG) {
            stack.interactWithEntity(player, this, hand);
    
            return true;
        }
    
        if (isBreedingItem(stack) && isTamed()) {
            if (getGrowingAge() == 0 && canBreed()) {
                consumeItemFromStack(player, stack);
                setInLove(player);
                return true;
            }
        
            if (isChild()) {
                consumeItemFromStack(player, stack);
                ageUp((int)((float)(-getGrowingAge() / 20) * 0.1F), true);
                return true;
            }
        }
        
        return false;
    }
    
    public void attackInFront(int range, boolean single) {
        attackInFront((int) (getSize(getPose()).width / 2) + 1, (int) (getSize(getPose()).height / 2), range, single);
    }
    
    public void attackInFront(int offsetX, int offsetY, int range, boolean single) {
        AxisAlignedBB aabb = new AxisAlignedBB(getPosition().offset(getHorizontalFacing(), offsetX).up(offsetY)).grow(range);
        Predicate<LivingEntity> filter = mob -> getPassengers().stream().noneMatch(passenger -> passenger == mob);
        List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb, filter);
        
        if (entities.isEmpty()) return;
        if (entities.size() == 1) attackEntityAsMob(entities.get(0));
        
        if (single) {
            LivingEntity singleEntity = entities.stream().min((entity1, entity2) -> Float.compare(entity1.getDistance(this), entity2.getDistance(this))).get();
            attackEntityAsMob(singleEntity);
        }
        else entities.forEach(this::attackEntityAsMob);
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
/*        if (entityIn instanceof TameableEntity) {
            TameableEntity entity = (TameableEntity) entityIn;
    
            if (entity.getOwner() == getOwner()) return false;
        }
        */
        return super.attackEntityAsMob(entityIn);
    }
    
    /**
     * Should the dragon attack
     * @param targetted
     * @param owner
     */
    @Override
    public boolean shouldAttackEntity(LivingEntity targetted, LivingEntity owner) {
        if (!isTamed()) return true;
        if (targetted instanceof TameableEntity) {
            TameableEntity target = (TameableEntity) targetted;
    
            return target.getOwner() != owner;
        }
        
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isSleeping()) setSleeping(false);
        
        return super.attackEntityFrom(source, amount);
    }
    
    /**
     * Public access version of {@link Entity#setRotation}
     */
    public void setRotation(float yaw, float pitch) {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }
    
    public void eat(@Nullable ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            heal(Math.max((int) getMaxHealth() / 5, 6));
            stack.shrink(1);
            playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 1f);
            for (int i = 0; i < 6; ++i) {
                double x = posX + getRNG().nextInt(2) - 1;
                double y = posY + getRNG().nextDouble();
                double z = posZ + getRNG().nextInt(2) - 1;
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
    }
    
    /**
     * Tame the dragon to the tamer if true
     * else, play the failed tame effects
     */
    public void tame(boolean tame, @Nullable PlayerEntity tamer) {
        if (!world.isRemote && !isTamed()) {
            if (tame && tamer != null && !ForgeEventFactory.onAnimalTame(this, tamer)) {
                setTamedBy(tamer);
                navigator.clearPath();
                setAttackTarget(null);
                setHealth(getMaxHealth());
                playTameEffect(true);
                world.setEntityState(this, (byte) 7);
            } else {
                playTameEffect(false);
                world.setEntityState(this, (byte) 6);
            }
        }
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    protected abstract Item[] getFoodItems();
    
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        if (getFoodItems().length == 0 || getFoodItems() == null) return false;
        return Arrays.asList(getFoodItems()).contains(stack.getItem());
    }
    
    /**
     * Set a damage source immunity
     */
    protected void setImmune(DamageSource source) { immunes.add(source.getDamageType()); }
    
    /**
     * Whether or not the dragon is immune to the source or not
     */
    private boolean isImmune(DamageSource source) { return !immunes.isEmpty() && immunes.contains(source.getDamageType()); }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || isImmune(source); }
    
    @Override
    public boolean canPassengerSteer() { return getControllingPassenger() != null && canBeSteered(); }
    
    @Override
    public boolean canBeSteered() { return getControllingPassenger() instanceof LivingEntity && isSaddled(); }
    
    @Nullable
    public Entity getControllingPassenger() { return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0); }
    
    /**
     * Perform a one-shot attack
     */
    public void performGenericAttack() {}
    
    /**
     * Perform a continuous special attack, e.g. Fire breathing
     *
     * @param shouldContinue True = continue attacking | False = interrupt / stop attack
     */
    public void performSpecialAttack(boolean shouldContinue) {}
    
    @Override
    protected float getJumpUpwardsMotion() { return canFly() ? 1f : super.getJumpUpwardsMotion(); }
    
    public void liftOff() { if (canFly()) jump(); }
    
    @Override
    public void fall(float distance, float damageMultiplier) { if (!canFly()) super.fall(distance, damageMultiplier); }
    
    /**
     * Children are handled through eggs, so this is a no-go
     */
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) { return null; }
    
    // ================================
    //        Entity Animation
    // ================================
    @Override
    public int getAnimationTick() { return animationTick; }

    @Override
    public void setAnimationTick(int tick) { animationTick = tick; }

    @Override
    public Animation getAnimation() { return animation; }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
        setAnimationTick(0);
    }
    
    public boolean hasActiveAnimation() { return getAnimation() != NO_ANIMATION && getAnimationTick() != 0; }
    
    // ================================

}
