package WolfShotz.Wyrmroost.content.entities.ai.goals;

import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.owdrake.OWDrakeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

/**
 * Class Responsible for eating grass at a positional offset rather than directly below the entity. (Useful for larger mobs)
 * Due to many fields being private, alot of this was taken from {@link net.minecraft.entity.ai.goal.EatGrassGoal EatGrassGoal}
 */
public class GrazeGoal extends Goal
{
    private static final Predicate<BlockState> IS_GRASS = BlockStateMatcher.forBlock(Blocks.GRASS);
    private final AbstractDragonEntity grassEaterEntity;
    private final World entityWorld;
    private int eatingGrassTimer, blockPosOffset;
    private boolean eaten;

    public GrazeGoal(AbstractDragonEntity grassEaterEntityIn, int blockPosOffset) {
        this.grassEaterEntity = grassEaterEntityIn;
        this.entityWorld = grassEaterEntityIn.world;
        this.blockPosOffset = blockPosOffset;
        this.eaten = false;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public GrazeGoal(AbstractDragonEntity grassEaterEntityIn) { this(grassEaterEntityIn, 0); }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (grassEaterEntity.getRNG().nextInt(grassEaterEntity.isChild() ? 50 : 1000) != 0) return false;
        else {
            BlockPos blockpos = new BlockPos(grassEaterEntity);
            BlockPos offsetPos = blockpos.offset(grassEaterEntity.getHorizontalFacing(), blockPosOffset).down();
            return IS_GRASS.test(entityWorld.getBlockState(new BlockPos(grassEaterEntity))) || entityWorld.getBlockState(offsetPos).getBlock() == Blocks.GRASS_BLOCK;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        eatingGrassTimer = 40;
        entityWorld.setEntityState(grassEaterEntity, (byte)10);
        grassEaterEntity.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() { return eatingGrassTimer > 0; }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        eatingGrassTimer = Math.max(0, eatingGrassTimer - 1);
        if (eatingGrassTimer == 4) {
            BlockPos blockpos = new BlockPos(grassEaterEntity).offset(grassEaterEntity.getHorizontalFacing(), blockPosOffset);
            if (IS_GRASS.test(entityWorld.getBlockState(blockpos))) {
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entityWorld, grassEaterEntity))
                    entityWorld.destroyBlock(blockpos, false);

                eaten = true;
                grassEaterEntity.eatGrassBonus();

            } else {
                BlockPos blockpos1 = blockpos.down();
                if (entityWorld.getBlockState(blockpos1).getBlock() == Blocks.GRASS_BLOCK) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entityWorld, grassEaterEntity)) {
                        entityWorld.playEvent(2001, blockpos1, Block.getStateId(Blocks.GRASS_BLOCK.getDefaultState()));
                        entityWorld.setBlockState(blockpos1, Blocks.DIRT.getDefaultState(), 2);
                    }

                    eaten = true;
                    grassEaterEntity.eatGrassBonus();
                }
            }
        }
    }

    public boolean hasEaten() { return eaten; }

    public void setEaten(boolean eaten) { this.eaten = eaten; }
}
