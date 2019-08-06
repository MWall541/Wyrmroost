package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.content.entities.ai.FlightMovementController;
import WolfShotz.Wyrmroost.util.ModUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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
    private int animationTick;
    private List<String> immunes = new ArrayList<>();

    // Dragon Entity Animations
    private Animation animation = NO_ANIMATION;

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
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.2f, 14, 4));
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
        super.writeAdditional(compound);
        compound.putBoolean("gender", getGender());
//        compound.putBoolea("asleep", isAsleep());
        compound.putBoolean("albino", isAlbino());
        compound.putBoolean("saddled", isSaddled());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setGender(compound.getBoolean("gender"));
//        setAsleep(compound.getBoolean("Asleep"));
        setAlbino(compound.getBoolean("albino"));
        setSaddled(compound.getBoolean("saddled"));
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

    public void setSitting(boolean sitting) {
        if (!world.isRemote) {
            sitGoal.setSitting(sitting);
            isJumping = false;
            navigator.clearPath();
            setAttackTarget(null);
        }

        super.setSitting(sitting);
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
        if ((onGround || ModUtils.getAltitude(this) <= 2) && isFlying()) setFlying(false);

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
    protected float getJumpUpwardsMotion() { return canFly() ? 1.5f : super.getJumpUpwardsMotion(); }

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

    /**
     * Array Containing all of the dragons food items
     */
    protected abstract Item[] getFoodItems();
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        if (getFoodItems().length == 0 || getFoodItems() == null) return false;
        return Arrays.stream(getFoodItems()).anyMatch(element -> element == stack.getItem());
    }

    @Override
    public boolean canPassengerSteer() { return getControllingPassenger() != null && canBeSteered(); }
    @Override
    public boolean canBeSteered() { return getControllingPassenger() instanceof LivingEntity && isSaddled(); }
    @Nullable
    public Entity getControllingPassenger() { return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0); }

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

    // ================================

}
