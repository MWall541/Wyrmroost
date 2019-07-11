package WolfShotz.Wyrmroost.content.entities.owdrake;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class OverworldDrake extends AbstractDragonEntity
{
    public OverworldDrake(EntityType<? extends OverworldDrake> drake, World world) {
        super(drake, world);
    }

    @Override
    protected void registerGoals() { super.registerGoals(); }

    @Override
    protected void registerAttributes() {
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.208f);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!isSleeping() && !world.isDaytime() && !isAngry() && !hasPath()) setSleeping(true);
        else if (isSleeping() && world.isDaytime()) setSleeping(false);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) { return this; }
}
