package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
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

import java.util.function.Supplier;

public class ThinLogBlock extends LogBlock implements IWaterLoggable
{
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape[] SHAPES = {
            Block.box(0, 4, 4, 16, 12, 12), // x
            Block.box(4, 0, 4, 12, 16, 12), // y
            Block.box(4, 4, 0, 12, 12, 16)  // z
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

    public static WoodGroup.Builder thinLogGroup(MaterialColor color, MaterialColor logColor)
    {
        return WoodGroup.builder(color, logColor)
                .log(stripped -> new ThinLogBlock(color, logColor, stripped))
                .strippedLog(() -> new ThinLogBlock(color, color, null))
                .wood(stripped -> new ThinLogBlock(logColor, logColor, stripped))
                .strippedWood(() -> new ThinLogBlock(color, color, null));
    }

    public static void setCutoutRendering(WoodGroup group)
    {
        RenderTypeLookup.setRenderLayer(group.getLog(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(group.getStrippedLog(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(group.getWood(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(group.getStrippedWood(), RenderType.cutout());
    }
}
