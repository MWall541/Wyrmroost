package WolfShotz.Wyrmroost.content.entities.minutus;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.minutus.goals.AttackAboveGoal;
import WolfShotz.Wyrmroost.content.entities.minutus.goals.BurrowGoal;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MinutusEntity extends AbstractDragonEntity
{
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(MinutusEntity.class, DataSerializers.BOOLEAN);

    public MinutusEntity(EntityType<? extends MinutusEntity> minutus, World world) {
        super(minutus, world);

        setImmune(DamageSource.IN_WALL);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new BurrowGoal(this));
        goalSelector.addGoal(3, new AttackAboveGoal(this));

    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4d);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4d);

    }

    // ================================
    //           Entity NBT
    // ================================
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(BURROWED, false);
    }

    /** Save Game */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Burrowed", isBurrowed());
    }

    /** Load Game */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setBurrowed(compound.getBoolean("Burrowed"));
    }

    public boolean isBurrowed() { return dataManager.get(BURROWED); }
    public void setBurrowed(boolean burrow) { dataManager.set(BURROWED, burrow); }

    /** Set The chances this dragon can be an albino. Set it to 0 to have no chance */
    @Override
    public int getAlbinoChances() { return 25; }
    // ================================

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) { return null; }
}
