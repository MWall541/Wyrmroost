package WolfShotz.Wyrmroost.content.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * Created by WolfShotz 7/10/19 - 21:36
 * This is where the magic happens. Here be our Dragons!
 */
public abstract class AbstractDragonEntity extends TameableEntity
{
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ASLEEP = EntityDataManager.createKey(AbstractDragonEntity.class, DataSerializers.BOOLEAN);

    public AbstractDragonEntity(EntityType<? extends AbstractDragonEntity> dragon, World world) {
        super(dragon, world);
        setTamed(false);
    }

    /** AI > Goals. smh */
    @Override
    protected void registerGoals() {
        sitGoal = new SitGoal(this);
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0d));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));
    }


    /* ========== Entity NBT ========== */

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(GENDER, true);
        this.dataManager.register(ASLEEP, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Gender", getGender());
        compound.putBoolean("Asleep", isAsleep());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setGender(compound.getBoolean("Gender"));
        setSleeping(compound.getBoolean("Asleep"));
    }

    /** Whether or not the dragonEntity is asleep */
    public boolean isAsleep() { return dataManager.get(ASLEEP); }
    public void setSleeping(boolean sleeping) { dataManager.set(ASLEEP, sleeping); }

    /** Gets the Gender of the dragonEntity. true = male | false = female. Anything else is an abomination. */
    public boolean getGender() { return dataManager.get(GENDER); }
    public void setGender(boolean sex) { dataManager.set(GENDER, sex); }

    /** Whether or not the dragonEntity is pissed or not. */
    public boolean isAngry() { return (this.dataManager.get(TAMED) & 2) != 0; }
    public void setAngry(boolean angry) {
        byte b0 = this.dataManager.get(TAMED);

        if (angry) this.dataManager.set(TAMED, (byte) (b0 | 2));
        else this.dataManager.set(TAMED, (byte) (b0 & -3));
    }

    /* ================================ */

    /** @return false to prevent an entity that is mounted to this entity from displaying the 'sitting' animation. */
    @Override
    public boolean shouldRiderSit() { return true; }
}
