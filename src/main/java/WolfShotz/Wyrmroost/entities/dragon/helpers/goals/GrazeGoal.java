package WolfShotz.Wyrmroost.entities.dragon.helpers.goals;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.network.packets.AnimationPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

import static WolfShotz.Wyrmroost.entities.util.animation.IAnimatedEntity.NO_ANIMATION;

/**
 * Class Responsible for eating grass at a positional offset rather than directly below the entity. (Useful for larger mobs)
 * Due to many fields being private, alot of this was taken from {@link net.minecraft.entity.ai.goal.EatGrassGoal EatGrassGoal}
 */
public class GrazeGoal extends Goal
{
    private static final Predicate<BlockState> IS_GRASS = BlockStateMatcher.forBlock(Blocks.GRASS);
    private final AbstractDragonEntity herbivore;
    private final World world;
    private final int blockPosOffset;
    private final Animation animation;
    private int eatingGrassTimer;

    public GrazeGoal(AbstractDragonEntity herbivoreIn, int blockPosOffset, Animation animation)
    {
        this.herbivore = herbivoreIn;
        this.world = herbivoreIn.world;
        this.blockPosOffset = blockPosOffset;
        this.animation = animation;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public GrazeGoal(AbstractDragonEntity herbivoreIn, Animation animation)
    {
        this(herbivoreIn, 0, animation);
    }

    public GrazeGoal(AbstractDragonEntity herbivoreIn)
    {
        this(herbivoreIn, 0, NO_ANIMATION);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (herbivore.getAttackTarget() != null) return false;
        if (herbivore.isBeingRidden()) return false;
        if (herbivore.getRNG().nextInt(herbivore.isChild()? 50 : 1000) != 0) return false;
        
        BlockPos blockpos = new BlockPos(herbivore);
        int blockPosOffset = herbivore.isChild()? this.blockPosOffset / 2 : this.blockPosOffset;
        BlockPos offsetPos = blockpos.offset(herbivore.getHorizontalFacing(), blockPosOffset).down();

        return IS_GRASS.test(world.getBlockState(new BlockPos(herbivore))) || world.getBlockState(offsetPos).getBlock() == Blocks.GRASS_BLOCK;
    }

    /**
     * Execute a base shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        eatingGrassTimer = 40;
        world.setEntityState(herbivore, (byte) 10);
        herbivore.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another base
     */
    public void resetTask()
    {
        eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return eatingGrassTimer > 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        eatingGrassTimer = Math.max(0, eatingGrassTimer - 1);
        if (eatingGrassTimer == 4)
        {
            BlockPos blockpos = new BlockPos(herbivore).offset(herbivore.getHorizontalFacing(), blockPosOffset);
            if (IS_GRASS.test(world.getBlockState(blockpos)))
            {
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, herbivore))
                    world.destroyBlock(blockpos, false);

                herbivore.eatGrassBonus();
                AnimationPacket.send(herbivore, animation);

            }
            else
            {
                BlockPos blockpos1 = blockpos.down();
                if (world.getBlockState(blockpos1).getBlock() == Blocks.GRASS_BLOCK)
                {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, herbivore))
                    {
                        world.playEvent(2001, blockpos1, Block.getStateId(Blocks.GRASS_BLOCK.getDefaultState()));
                        world.setBlockState(blockpos1, Blocks.DIRT.getDefaultState(), 2);
                    }

                    herbivore.eatGrassBonus();
                    AnimationPacket.send(herbivore, animation);
                }
            }
        }
    }
}
