package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class CanariLeavesBlock extends LeavesBlock implements IGrowable
{
    public static final BooleanProperty BERRIES = BooleanProperty.create("berries");

    public CanariLeavesBlock()
    {
        super(ModUtils.blockBuilder(Material.LEAVES).hardnessAndResistance(0.2f).sound(SoundType.PLANT).tickRandomly());

        setDefaultState(stateContainer.getBaseState()
                .with(DISTANCE, 7)
                .with(PERSISTENT, false)
                .with(BERRIES, false));
    }

    @Override
    public boolean ticksRandomly(BlockState state) { return true; }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
    {
        super.randomTick(state, worldIn, pos, random);

        if (!state.get(BERRIES) && state.get(DISTANCE) < 7 && random.nextInt(10) == 0)
            worldIn.setBlockState(pos, state.cycle(BERRIES), Constants.BlockFlags.DEFAULT);
    }

    //    @Override
//    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
//    {
//        if (!state.get(BERRIES)) return false;
//        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(WRItems.CANARI_CHERRY.get()));
//        worldIn.setBlockState(pos, state.cycle(BERRIES), Constants.BlockFlags.DEFAULT);
//        return true;
//    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        return !state.get(BERRIES);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
        return !state.get(BERRIES);
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
    {
        worldIn.setBlockState(pos, state.cycle(BERRIES));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(BERRIES);
    }
}
