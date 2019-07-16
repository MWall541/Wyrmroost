package WolfShotz.Wyrmroost.content.entities.minutus.goals;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.minutus.MinutusEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class BurrowGoal extends Goal
{
    private MinutusEntity minutus;
    private int delay = 60;
    private World world = Wyrmroost.proxy.getClientWorld();

    public BurrowGoal(MinutusEntity minutusIn) {
        this.minutus = minutusIn;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() { return !minutus.isBurrowed(); }

    @Override
    public boolean shouldContinueExecuting() { return shouldExecute(); }

    @Override
    public void tick() {
        --delay;
        BlockPos pos = minutus.getPosition();
        for (int x = 0; x < 4; ++x) world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos.down(1))), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0, 0);
        if (delay <= 0) {
//            minutus.setPosition(pos.getX() + 0.5, pos.getY() - 0.1, pos.getZ() + 0.5); //TODO: Perform animation with "illusion of movement"?
            minutus.setBurrowed(true);
        }
    }
}
