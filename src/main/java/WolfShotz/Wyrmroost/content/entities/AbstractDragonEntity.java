package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.content.entities.ai.FlightMovementController;
import WolfShotz.Wyrmroost.util.MathUtils;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity implements IAnimatedEntity
{
    protected int animationTick;
    public int hatchTimer; // Used in subclasses for hatching time
    protected List<String> immunes = new ArrayList<>();
    public boolean isSpecialAttacking = false;

    // Dragon Entity Animations
    public Animation animation = NO_ANIMATION;

    // Dragon Entity Data
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
//    private static final DataParameter<Boolean> ASLEEP = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ALBINO = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);


    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
        setTamed(false);

        moveController = new FlightMovementController(this);

        stepHeight = 1;

        if (canFly()) {
            setImmune(DamageSource.FALL);
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
//        goalSelector.addGoal(2, new SleepGoal(this));
        goalSelector.addGoal(3, sitGoal = new SitGoal(this));
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();

        dataManager.register(GENDER, getRNG().nextBoolean());
//        dataManager.register(ASLEEP, false);
        dataManager.register(ALBINO, getAlbinoChances() != 0 && getRNG().nextInt(getAlbinoChances()) == 0);
        dataManager.register(SADDLED, false);
        dataManager.register(FLYING, false);

    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("gender", getGender());
//        compound.putBoolea("asleep", isAsleep());
        compound.putBoolean("albino", isAlbino());
        compound.putBoolean("saddled", isSaddled());
    
        super.writeAdditional(compound);
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        setGender(compound.getBoolean("gender"));
//        setAsleep(compound.getBoolean("Asleep"));
        setAlbino(compound.getBoolean("albino"));
        setSaddled(compound.getBoolean("saddled"));
    
        super.readAdditional(compound);
    }

    /**
     * Whether or not the dragonEntity is asleep
     * TODO
     */
//    public boolean isAsleep() { return dataManager.get(ASLEEP); }
//    public void setAsleep(boolean sleeping) { dataManager.set(ASLEEP, sleeping); }

    /**
     * Gets the Gender of the dragonEntity.
     * <P>
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
        if (canFly()) {
            dataManager.set(FLYING, fly);
            if (fly) jump();
        }
    }

    public void setSit(boolean sitting) {
        if (!world.isRemote) {
            sitGoal.setSitting(sitting);
            isJumping = false;
            navigator.clearPath();
            setAttackTarget(null);
        }
        
        super.setSitting(sitting);
    
        recalculateSize(); // Change the hitbox for sitting
    }

    /**
     * Tame the dragon to the tamer if true
     * else, play the failed tame effects
     */
    public void tame(boolean tame, PlayerEntity tamer) {
        if (!world.isRemote && !isTamed()) {
            if (tame && !ForgeEventFactory.onAnimalTame(this, tamer)) {
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
//        boolean shouldFly = canFly() && getAltitude() > 2;
//        if (shouldFly != isFlying()) setFlying(true);
        if ((onGround || MathUtils.getAltitude(this) <= 2) && isFlying()) setFlying(false);

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
            if (world.isRemote && animationTick >= animation.getDuration()) setAnimation(NO_ANIMATION);
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
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
    
    public Vec3d getApproximateThroatPos() {
        Vec3d vec3d = getPositionVec();
        Vec3d vec3d1 = getLookVec();
        Vec3d vec3d2 = vec3d.add(vec3d1.x, vec3d1.y, vec3d1.z);
        Vec3d vec3d3 = vec3d2.rotateYaw((float) (Math.toRadians(-renderYawOffset) + Math.PI));
        
        return vec3d2;
    }
    
    public void attackInFront(int range, boolean single) {
        attackInFront((int) (getSize(getPose()).width / 2) + 1, (int) (getSize(getPose()).height / 2), range, single);
    }
    
    public void attackInFront(int offsetX, int offsetY, int range, boolean single) {
        AxisAlignedBB aabb = new AxisAlignedBB(getPosition().offset(getHorizontalFacing(), offsetX).up(offsetY)).grow(range);
        List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb, filter -> filter != this);
        
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
        if (entityIn instanceof TameableEntity) {
            TameableEntity entity = (TameableEntity) entityIn;
            
            if (entity.getOwner() == getOwner()) return false;
        }
        
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
        
        return false;
    }
    
    /**
     * Array Containing all of the dragons food items
     */
    protected abstract Item[] getFoodItems();
    
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        if (getFoodItems().length == 0 || getFoodItems() == null) return false;
        return Arrays.stream(getFoodItems()).anyMatch(element -> element == stack.getItem());
    }
    
    public void eat(@Nullable ItemStack stack) { eat(stack, Math.max((int) getMaxHealth() / 5, 6)); }
    
    public void eat(@Nullable ItemStack stack, int healAmount) {
        heal(healAmount);
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
    protected float getJumpUpwardsMotion() { return canFly() ? 1.5f : super.getJumpUpwardsMotion(); }
    
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
    
    public boolean hasActiveAnimation() { return getAnimation() != NO_ANIMATION; }
    
    // ================================

}
