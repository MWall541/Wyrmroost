package WolfShotz.Wyrmroost.content.entities.dragon.minutus.goals;

import WolfShotz.Wyrmroost.content.entities.dragon.minutus.MinutusEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BurrowGoal extends Goal
{
    private MinutusEntity minutus;
    private int burrowTicks = 30;

    public BurrowGoal(MinutusEntity minutusIn) {
        this.minutus = minutusIn;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() { return !minutus.isBurrowed() && belowIsSand(); }

    @Override
    public boolean shouldContinueExecuting() { return shouldExecute(); }

    @Override
    public void resetTask() { burrowTicks = 30; }

    @Override
    public void tick() {
        if (--burrowTicks <= 0) {
            minutus.setBurrowed(true);
            burrowTicks = 30;
        }
    }

    private boolean belowIsSand() { return minutus.world.getBlockState(minutus.getPosition().down(1)).getMaterial() == Material.SAND; }

    public int getBurrowTicks() { return burrowTicks; }
}
