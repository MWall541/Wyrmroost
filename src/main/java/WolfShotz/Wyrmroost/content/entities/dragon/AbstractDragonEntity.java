package WolfShotz.Wyrmroost.content.entities.dragon;

import WolfShotz.Wyrmroost.event.SetupItems;
import WolfShotz.Wyrmroost.event.SetupSounds;
import WolfShotz.Wyrmroost.util.entityhelpers.DragonBodyController;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.DragonLookController;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.FlightMovementController;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.FlightWanderGoal;
import WolfShotz.Wyrmroost.util.entityhelpers.ai.goals.SleepGoal;
import WolfShotz.Wyrmroost.util.utils.MathUtils;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import WolfShotz.Wyrmroost.util.utils.NetworkUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.BodyController;
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
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatedEntity
{
    public int hatchTimer, sleepTimeout;
    public int shouldFlyThreshold = 3;
    public final int randomFlyChance = 1000; // Default to random chance
    @OnlyIn(Dist.CLIENT) public int glowTicks;
    public List<String> immunes = new ArrayList<>();
    public boolean isSpecialAttacking = false;
    public Random syncRand = new Random(6045323340150860495L); // Use a seed to sync between server and client
    public FlightWanderGoal aiFlyWander;
    
    // Dragon Entity Animations
    public int animationTick;
    public Animation animation = NO_ANIMATION;
    public static Animation SLEEP_ANIMATION;
    public static Animation WAKE_ANIMATION;
    
    // Dragon Entity Data
    public static final DataParameter<Boolean> GENDER       = createBoolean();
    public static final DataParameter<Boolean> SPECIAL      = createBoolean();
    public static final DataParameter<Boolean> SADDLED      = createBoolean();
    public static final DataParameter<Boolean> FLYING       = createBoolean();
    public static final DataParameter<Boolean> SLEEPING     = createBoolean();
    public static final DataParameter<Boolean> ARMORED      = createBoolean();
    public static final DataParameter<Integer> VARIANT      = createInt();
    
    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
        
        setTamed(false);
        
        moveController = new FlightMovementController(this);
        lookController = new DragonLookController(this);
        
        stepHeight = 1;
    }
    
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, sitGoal = new SitGoal(this));
        goalSelector.addGoal(2, new SleepGoal(this));
    }
    
    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        
        dataManager.register(GENDER, getRNG().nextBoolean());
        dataManager.register(SPECIAL, getSpecialChances() != 0 && getRNG().nextInt(getSpecialChances()) == 0);
        dataManager.register(FLYING, false);
        dataManager.register(SLEEPING, false);
    }
    
    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("gender", getGender());
        compound.putBoolean("special", isSpecial());
        compound.putBoolean("sleeping", isSleeping());
        
        super.writeAdditional(compound);
    }
    
    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        setGender(compound.getBoolean("gender"));
        setSpecial(compound.getBoolean("special"));
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
     * Whether or not this dragonEntity is albino. true == isSpecial, false == is not
     */
    public boolean isSpecial() { return dataManager.get(SPECIAL); }
    public void setSpecial(boolean albino) { dataManager.set(SPECIAL, albino); }
    public int getSpecialChances() { return rand.nextInt(400) + 100; }
    
    /**
     * Whether or not the dragon is flying
     */
    public boolean isFlying() { return dataManager.get(FLYING); }
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
     * Whether or not the dragon is saddled
     */
    public boolean isSaddled() { return dataManager.get(SADDLED); }
    public void setSaddled(boolean saddled) {
        dataManager.set(SADDLED, saddled);
        if (saddled) playSound(SoundEvents.ENTITY_HORSE_SADDLE, 1f, 1f);
    }
    
    /**
     * Get the variant of the dragon (if it has them)
     */
    public int getVariant() { return dataManager.get(VARIANT); }
    public void setVariant(int variant) { dataManager.set(VARIANT, variant); }
    
    /**
     * Whether or not the dragon is armored
     */
    public boolean isArmored() { return dataManager.get(ARMORED); }
    public void setArmored(boolean armored) { dataManager.set(ARMORED, armored); }
    
    /**
     * Whether or not the dragon is sleeping.
     */
    @Override
    public boolean isSleeping() { return dataManager.get(SLEEPING); }
    public void setSleeping(boolean sleep) { // If we have a sleep animation, then play it.
        if (isSleeping() == sleep) return;
        
        dataManager.set(SLEEPING, sleep);
        
        isJumping = false;
        navigator.clearPath();
        setAttackTarget(null);
        
        if (SLEEP_ANIMATION != null && WAKE_ANIMATION != null && noActiveAnimation())
            NetworkUtils.sendAnimationPacket(this, sleep? SLEEP_ANIMATION : WAKE_ANIMATION);
        if (!sleep) sleepTimeout = 350;
        
        recalculateSize(); // Change the hitbox for sitting / sleeping
    }
    
    public void setSit(boolean sitting) {
        if (isSitting() == sitting) return;
        
        if (!world.isRemote) {
            sitGoal.setSitting(sitting);
            isJumping = false;
            navigator.clearPath();
            setAttackTarget(null);
        }
        if (isSleeping()) setSleeping(false);
        
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
        boolean shouldFly = (MathUtils.getAltitude(this) > shouldFlyThreshold) && canFly();
        if (shouldFly != isFlying()) setFlying(shouldFly);
        
        if (!isFlying() && syncRand.nextInt(randomFlyChance) == 0 && canFly()) setFlying(true);
        
        if (!world.isRemote) { // Server Only Stuffs
            // world time is always day on client, so we need to sync sleeping from server to client with sleep getter...
            if (sleepTimeout > 0) --sleepTimeout;
            if (!world.isDaytime() && !isSleeping() && sleepTimeout <= 0 && getAttackTarget() == null && getNavigator().noPath() && !isFlying() && !isBeingRidden() && getRNG().nextInt(300) == 0) {
                if (isTamed()) {
                    if (isSitting()) setSleeping(true);
                } else setSleeping(true);
            }
            if (world.isDaytime() && isSleeping() && (rand.nextInt(150) == 0 || isInWaterOrBubbleColumn()))
                setSleeping(false);
            
        } else { // Client Only Stuffs
            if (isSpecial()) doSpecialEffects();
            
            if (glowTicks > 0) {
                if (glowTicks % 4 == 0) setGlowing(true);
                else setGlowing(false);
                --glowTicks;
            }
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
//        if (flying) navigator = new FlightPathNavigator(this, world);
//        else navigator = new DragonGroundPathNavigator(this, world);
    }
    
    /**
     * Needed because the field is private >.>
     */
    @Override
    protected BodyController createBodyController() { return new DragonBodyController(this); }
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        if (stack.getItem() == Items.NAME_TAG) {
            stack.interactWithEntity(player, this, hand);
            
            return true;
        }
        
        if (isBreedingItem(stack) && isTamed()) {
            if (getGrowingAge() == 0 && canBreed()) {
                eat(stack);
                setInLove(player);
                
                return true;
            }
            
            if (isChild()) {
                ageUp((int)((float)(-getGrowingAge() / 20) * 0.1F), true);
                eat(stack);
                if (isSleeping()) setSleeping(false);
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Method called to "call" ur dragon.
     * If its flying, try to descend down.
     * If its sitting, try to stand.
     *
     * boolean to make sure we can perform this. if not, ignore it
     * @param player NULLABLE: pass null for server, not needed there.
     */
    public boolean callDragon(@Nullable PlayerEntity player) {
        boolean result = false;
        if (isFlying()) {
            if (world.isRemote) result = true;
            else if (aiFlyWander != null) {
                aiFlyWander.setDescending();
                result = true;
            }
        }
        else if (isSitting()) {
            setSit(false);
            result = true;
        }
        
        if (world.isRemote && result && player != null) {
            player.playSound(SetupSounds.CALL_WHISTLE, 1, 1);
            glowTicks = 8;
        }
        
        return result;
    }
    
    public void attackInFront(int range) {
        AxisAlignedBB aabb = new AxisAlignedBB(getPosition().offset(getHorizontalFacing(), range + range)).grow(range, 0, range);
        List<LivingEntity> livingEntities = world.getEntitiesWithinAABB(LivingEntity.class, aabb, found -> found != this && getPassengers().stream().noneMatch(found::equals));
        
        if (livingEntities.isEmpty()) return;
        
        livingEntities.forEach(this::attackEntityAsMob);
    }
    
    @Override // Dont damage owners other pets!
    public boolean attackEntityAsMob(Entity entity) {
        if (entity == getOwner()) return false;
        if (entity instanceof TameableEntity && ((TameableEntity) entity).getOwner() == getOwner())
            return false;
        
        return super.attackEntityAsMob(entity);
    }
    
    /**
     * Should the dragon attack
     */
    @Override // We shouldnt be targetting pets...
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (!isTamed()) return true;
        if (target instanceof TameableEntity) return ((TameableEntity) target).getOwner() != owner;
        
        return true;
    }
    
    /**
     * Called when the entity is attacked by something
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isSleeping()) setSleeping(false);
        if (isSitting()) setSit(false);
        
        return super.attackEntityFrom(source, amount);
    }
    
    /**
     * Effects to play when the dragon is 'Special'
     * Default to white sparkles around the body.
     * Can be overriden to do custom effects.
     */
    @OnlyIn(Dist.CLIENT)
    public void doSpecialEffects() {
        if (ticksExisted % 25 == 0) {
            double x = posX + getWidth() * (getRNG().nextGaussian() * 0.5d);
            double y = posY + getHeight() * (getRNG().nextDouble());
            double z = posZ + getWidth() * (getRNG().nextGaussian() * 0.5d);
            world.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05f, 0);
        }
    }
    
    @Override
    protected void spawnDrops(DamageSource src) {
        if (isSaddled()) entityDropItem(Items.SADDLE);
        super.spawnDrops(src);
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
//            Item item = stack.getItem();
//            List<Pair<EffectInstance, Float>> effects;
            
            stack.shrink(1);
            if (getHealth() < getMaxHealth()) heal(Math.max((int) getMaxHealth() / 5, 6));
//            if (item.isFood()) {
//                effects = stack.getItem().getFood().getEffects();
//                if (!effects.isEmpty() && effects.stream().noneMatch(e -> e.getLeft() == null)) // Apply food effects if it has any
//                    effects.forEach(e -> addPotionEffect(e.getLeft()));
//            }
            playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 1f);
            if (world.isRemote) {
                Vec3d mouth = getApproximateMouthPos();
                
                for (int i = 0; i < 11; ++i) {
                    Vec3d vec3d1 = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) rand.nextFloat() - 0.5D) * 0.1D);
                    vec3d1 = vec3d1.rotatePitch(-rotationPitch * (MathUtils.PI / 180f));
                    vec3d1 = vec3d1.rotateYaw(-rotationYaw * (MathUtils.PI / 180f));
                    world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), mouth.x, mouth.y, mouth.z, vec3d1.x, vec3d1.y, vec3d1.z);
                }
            }
        }
    }
    
    /**
     * Tame the dragon to the tamer if true
     * else, play the failed tame effects
     */
    public boolean tame(boolean tame, @Nullable PlayerEntity tamer) {
        if (!world.isRemote && !isTamed()) {
            if (tame && tamer != null && !ForgeEventFactory.onAnimalTame(this, tamer)) {
                setTamedBy(tamer);
                navigator.clearPath();
                setAttackTarget(null);
                setHealth(getMaxHealth());
                playTameEffect(true);
                world.setEntityState(this, (byte) 7);
                
                return true;
            } else {
                playTameEffect(false);
                world.setEntityState(this, (byte) 6);
            }
        }
        
        return false;
    }
    
    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
        
        if (world.isRemote) {
            for (int i = 0; i < getWidth() * 5; ++i) {
                double x = posX + (getRNG().nextGaussian() * getWidth()) / 1.5d;
                double y = posY + getRNG().nextDouble() * (getRNG().nextDouble() + 2d);
                double z = posZ + (getRNG().nextGaussian() * getWidth()) / 1.5d;
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
    }
    
    /**
     * A universal position for the position of the mouth on the dragon.
     * This is prone to be inaccurate, but can serve good enough for most things
     * If a more accurate position is needed, best to override and adjust accordingly.
     * @return An approximate position of the mouth of the dragon
     */
    public Vec3d getApproximateMouthPos() {
        return MathUtils.rotateYaw(renderYawOffset, 0, (getWidth() / 2) + 0.5d).add(posX, posY + getEyeHeight() - 0.15d, posZ);
    }
    
    /**
     * Is the stack passed an item that is interactable with dragons?
     */
    public boolean isInteractItem(ItemStack stack) {
        Item item = stack.getItem();
        
        return item == SetupItems.soulCrystal ||
                       item == Items.NAME_TAG ||
                       isBreedingItem(stack);
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    protected abstract Item[] getFoodItems();
    
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        if (getFoodItems() == null || getFoodItems().length == 0) return false;
        return Arrays.asList(getFoodItems()).contains(stack.getItem());
    }
    
    public List<Entity> getEntitiesNearby(double radius) {
        return world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(radius), found -> found != this && getPassengers().stream().noneMatch(found::equals));
    }
    
    public List<Entity> getEntitiesNearby(double radius, Entity instanceExclusion) {
        return world.getEntitiesInAABBexcluding(instanceExclusion, getBoundingBox().grow(radius), found -> getPassengers().stream().noneMatch(found::equals));
    }
    
    @Override
    public void playAmbientSound() { if (!isSleeping()) super.playAmbientSound(); }
    
    /**
     * Set a damage source immunity
     */
    public void setImmune(DamageSource source) { immunes.add(source.getDamageType()); }
    
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
    @Override
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
    
    /**
     * Whether or not the dragon can fly.
     * For ground entities, return false
     */
    public boolean canFly() {
        boolean isSafe = true;
        
        for (int i = 1; i < (shouldFlyThreshold / 2.5f) + 1; ++i) {
            if (world.getBlockState(getPosition().up((int) getHeight() + i)).getMaterial().blocksMovement()) {
                isSafe = false;
                break;
            }
        }
        
        return !isChild() && !getLeashed() && !isSleeping() && !isSitting() && isSafe;
    }
    
    @Override
    protected float getJumpUpwardsMotion() { return canFly()? 1.2f : super.getJumpUpwardsMotion(); }
    
    public void liftOff() { if (canFly()) jump(); }
    
    @Override // Disable falling calculations if we can fly (fall damage etc.)
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
        setAnimationTick(0);
        this.animation = animation;
    }
    
    public boolean noActiveAnimation() { return getAnimation() != NO_ANIMATION || getAnimationTick() != 0; }
    
    // ================================
    
    /**
     * Create a boolean data Parameter
     */
    public static DataParameter<Boolean> createBoolean() { return EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN); }
    public static DataParameter<Boolean> createBoolean(Class<? extends Entity> clazz) { return EntityDataManager.createKey(clazz, DataSerializers.BOOLEAN); }
    
    /**
     * Create an integer data parameter
     * @return
     */
    public static DataParameter<Integer> createInt() { return EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.VARINT); }
    public static DataParameter<Integer> createInt(Class<? extends Entity> clazz) { return EntityDataManager.createKey(clazz, DataSerializers.VARINT); }
}
