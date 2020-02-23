package WolfShotz.Wyrmroost.content.blocks;

import WolfShotz.Wyrmroost.registry.WRBlocks;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.PlantType;

import java.util.Random;

public class CinisRootBlock extends BushBlock implements IGrowable
{
    public static final IntegerProperty GROWTH = BlockStateProperties.AGE_0_1;
    private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    public CinisRootBlock()
    {
        super(ModUtils.blockBuilder(Material.PLANTS).hardnessAndResistance(0).doesNotBlockMovement().tickRandomly().sound(SoundType.PLANT));
        setDefaultState(getStateContainer().getBaseState().with(GROWTH, 0));
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        if (random.nextInt(7) == 0 && canGrow(worldIn, pos, state, false))
            grow(worldIn, random, pos, state);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (state.get(GROWTH) != 1) return false;
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(WRBlocks.CINIS_ROOT.get()));
        worldIn.setBlockState(pos, getDefaultState(), 4);
        return true;
    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
    {
        return state.get(GROWTH) == 0;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
        return state.get(GROWTH) == 0;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, BlockState state)
    {
        worldIn.setBlockState(pos, state.cycle(GROWTH), 4);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(GROWTH);
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) { return PlantType.Desert; }
}
