package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ThinLogBlock extends LogBlock implements IWaterLoggable
{
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape[] SHAPES = {
            Block.box(0, 2, 2, 16, 14, 14), // x
            Block.box(2, 0, 2, 14, 16, 14), // y
            Block.box(2, 2, 0, 14, 14, 16)  // z
    };

    public ThinLogBlock(Properties props, Supplier<Block> stripped)
    {
        super(props, stripped);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    public ThinLogBlock(MaterialColor top, MaterialColor bark, Supplier<Block> stripped)
    {
        this(properties(top, bark), stripped);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext ctx)
    {
        return SHAPES[state.getValue(LogBlock.AXIS).ordinal()];
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED)? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx)
    {
        return super.getStateForPlacement(ctx).setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    public static class Group extends WRBlocks.WoodGroup
    {
        public Group(String name, MaterialColor color, MaterialColor logColor)
        {
            super(name, color, logColor);
        }

        @Override
        protected RegistryObject<Block> applyLog(String name, MaterialColor color, MaterialColor logColor, boolean stripped, boolean wood)
        {
            return WRBlocks.register(name, () -> new ThinLogBlock(color, logColor, stripped? null : wood? self().strippedWood : self().strippedLog), WRBlocks.extend().flammability(5, 5).render(() -> RenderType::cutout));
        }
    }
}
