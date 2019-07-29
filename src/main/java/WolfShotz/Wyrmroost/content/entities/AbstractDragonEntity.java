package WolfShotz.Wyrmroost.content.entities;

import WolfShotz.Wyrmroost.content.entities.ai.SleepGoal;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.math.Vec3d;
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
    private Animation animation = NO_ANIMATION;

    private List<String> immunes = new ArrayList<>();

    // Dragon Entity Data
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ASLEEP = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ALBINO = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);


    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
        setTamed(false);
    }

    @Override
    protected void registerGoals() {
        sitGoal = new SitGoal(this);
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new SleepGoal(this));
        goalSelector.addGoal(3, sitGoal);
    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(GENDER, getRNG().nextBoolean());
        dataManager.register(ASLEEP, false);
        dataManager.register(ALBINO, getAlbinoChances() != 0 && getRNG().nextInt(getAlbinoChances()) == 0);
        dataManager.register(SADDLED, false);

    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Gender", getGender());
        compound.putBoolean("Asleep", isAsleep());
        compound.putBoolean("Albino", isAlbino());
        compound.putBoolean("saddled", isSaddled());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setGender(compound.getBoolean("Gender"));
        setAsleep(compound.getBoolean("Asleep"));
        setAlbino(compound.getBoolean("Albino"));
        setSaddled(compound.getBoolean("saddled"));
    }

    /** Whether or not the dragonEntity is asleep */
    public boolean isAsleep() { return dataManager.get(ASLEEP); }
    public void setAsleep(boolean sleeping) { dataManager.set(ASLEEP, sleeping); }

    /** Gets the Gender of the dragonEntity.<P> true = Male | false = Female. Anything else is an abomination. */
    public boolean getGender() { return dataManager.get(GENDER); }
    public void setGender(boolean sex) { dataManager.set(GENDER, sex); }

    /** Whether or not this dragonEntity is albino. true == isAlbino, false == is not */
    public boolean isAlbino() { return dataManager.get(ALBINO); }
    public void setAlbino(boolean albino) { dataManager.set(ALBINO, albino); }
    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    public abstract int getAlbinoChances();

    /** Whether or not the drake is saddled */
    public boolean isSaddled() { return dataManager.get(SADDLED); }
    public void setSaddled(boolean saddled) { dataManager.set(SADDLED, saddled); }

    /** Whether or not the dragonEntity is pissed or not. */
    public boolean isAngry() { return (this.dataManager.get(TAMED) & 2) != 0; }
    public void setAngry(boolean angry) {
        byte b0 = this.dataManager.get(TAMED);

        if (angry) this.dataManager.set(TAMED, (byte) (b0 | 2));
        else this.dataManager.set(TAMED, (byte) (b0 & -3));
    }

    // ================================

    @Override
    public void livingTick() {
        super.livingTick();
        if (getAnimation().getDuration() > getAnimationTick()) setAnimation(NO_ANIMATION);
        if (isAsleep() && world.isDaytime()) setAsleep(false);
    }

    @Override
    public void travel(Vec3d vec3d) {
        if (isBeingRidden() && canBeSteered() && isSaddled()) {
            LivingEntity rider = (LivingEntity) getControllingPassenger();
            if (canPassengerSteer()) {
                float f = rider.moveForward, s = rider.moveStrafing;
                setSprinting(rider.isSprinting());
                setAIMoveSpeed((float) getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                Vec3d target = new Vec3d(s, vec3d.y, f);
                super.travel(target);
                setRotation(rotationYaw = rider.rotationYaw, rotationPitch);
//              setRotation(ModUtils.limitAngle(rotationYaw, ModUtils.calcAngle(target), 15), rotationPitch); TODO: Smooth Rotations

                return;
            }
        }
        super.travel(vec3d);
    }

    @Override
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() || isSleeping();
    }

    /** Set a damage source immunity */
    protected void setImmune(DamageSource source) { immunes.add(source.getDamageType()); }
    private boolean isImmune(DamageSource source) { return !immunes.isEmpty() && immunes.contains(source.getDamageType()); }
    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || isImmune(source); }

    /** Array Containing all of the dragons food items */
    protected abstract Item[] getFoodItems();
    protected boolean isFoodItem(ItemStack stack) { return Arrays.stream(getFoodItems()).anyMatch(item -> item == stack.getItem()); }

    @Override
    public boolean canPassengerSteer() { return true; }
    @Override
    public boolean canBeSteered() { return getControllingPassenger() instanceof LivingEntity; }
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
