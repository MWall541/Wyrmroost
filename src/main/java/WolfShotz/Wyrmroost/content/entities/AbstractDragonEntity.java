package WolfShotz.Wyrmroost.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity
{
    private List<String> immunes = new ArrayList<>();

    // Dragon Entity Data
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ASLEEP = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ALBINO = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);

    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
        setTamed(false);
    }

    @Override
    protected void registerGoals() {
        sitGoal = new SitGoal(this);
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, sitGoal);
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
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Gender", getGender());
        compound.putBoolean("Asleep", isAsleep());
        compound.putBoolean("Albino", isAlbino());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setGender(compound.getBoolean("Gender"));
        setSleeping(compound.getBoolean("Asleep"));
        setAlbino(compound.getBoolean("Albino"));
    }

    /** Whether or not the dragonEntity is asleep */
    public boolean isAsleep() { return dataManager.get(ASLEEP); }
    public void setSleeping(boolean sleeping) { dataManager.set(ASLEEP, sleeping); }

    /** Gets the Gender of the dragonEntity.<P> true = Male | false = Female. Anything else is an abomination. */
    public boolean getGender() { return dataManager.get(GENDER); }
    public void setGender(boolean sex) { dataManager.set(GENDER, sex); }

    /** Whether or not this dragonEntity is albino. true == isAlbino, false == is not */
    public boolean isAlbino() { return dataManager.get(ALBINO); }
    public void setAlbino(boolean albino) { dataManager.set(ALBINO, albino); }
    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    public abstract int getAlbinoChances();

    /** Whether or not the dragonEntity is pissed or not. */
    public boolean isAngry() { return (this.dataManager.get(TAMED) & 2) != 0; }
    public void setAngry(boolean angry) {
        byte b0 = this.dataManager.get(TAMED);

        if (angry) this.dataManager.set(TAMED, (byte) (b0 | 2));
        else this.dataManager.set(TAMED, (byte) (b0 & -3));
    }

    // ================================

    protected void setImmune(DamageSource source) { immunes.add(source.getDamageType()); }

    public boolean isImmune(DamageSource source) {
        if (immunes.isEmpty()) return false;
        return immunes.contains(source.getDamageType());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) { return super.isInvulnerableTo(source) || isImmune(source); }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!isSleeping() && !world.isDaytime() && !isAngry() && !hasPath()) setSleeping(true);
        else if (isSleeping() && world.isDaytime()) setSleeping(false);
    }

    /** @return false to prevent an entity that is mounted to this entity from displaying the 'sitting' animation. */
    @Override
    public boolean shouldRiderSit() { return true; }
}
