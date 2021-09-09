package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CanisRootBlock extends CropsBlock
{
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 0, 0, 16, 6, 16),
            Block.box(0, 0, 0, 16, 13, 16),
            Block.box(0, 0, 0, 16, 14, 16)
    };

    public CanisRootBlock()
    {
        super(WRBlocks.plant().randomTicks().instabreak().sound(SoundType.CROP));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(AGE);
    }

    @Override
    public IntegerProperty getAgeProperty()
    {
        return AGE;
    }

    @Override
    public int getMaxAge()
    {
        return 3;
    }

    @Override
    protected IItemProvider getBaseSeedId()
    {
        return WRBlocks.CANIS_ROOT.get().asItem();
    }

    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext ctx)
    {
        return SHAPE_BY_AGE[state.getValue(getAgeProperty())];
    }

    public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random)
    {
        if (random.nextInt(3) != 0) super.randomTick(state, level, pos, random);
    }
}
