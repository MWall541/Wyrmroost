package WolfShotz.Wyrmroost.content.entities.minutus.goals;

import WolfShotz.Wyrmroost.content.entities.minutus.MinutusEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.EnumSet;

public class BurrowGoal extends Goal
{
    private MinutusEntity minutus;
    private int delay = 30;

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
    public void resetTask() { delay = 30; }

    @Override
    public void tick() {
        if (--delay <= 0) {
            minutus.setBurrowed(true);
            delay = 30;
        }
        BlockPos pos = minutus.getPosition();
        World world = DistExecutor.runForDist(() -> ModUtils::getClientWorld, () -> ModUtils::getServerWorld);
        for (int x = 0; x < 4; ++x)
            world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(pos.down(1))), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0, 0);
    }

    private boolean belowIsSand() { return minutus.world.getBlockState(minutus.getPosition().down(1)).getMaterial() == Material.SAND; }
}
