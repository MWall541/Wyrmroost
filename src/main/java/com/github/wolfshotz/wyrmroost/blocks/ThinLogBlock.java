package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.registry.WRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ThinLogBlock extends LogBlock
{
    public static final VoxelShape SHAPE_X = Block.box(0, 2, 2, 16, 14, 14);
    public static final VoxelShape SHAPE_Y = Block.box(2, 0, 2, 14, 16, 14);
    public static final VoxelShape SHAPE_Z = Block.box(2, 2, 0, 14, 14, 16);
    private static final VoxelShape[] SHAPE_LOOKUP = {SHAPE_X, SHAPE_Y, SHAPE_Z};

    public ThinLogBlock(MaterialColor top, MaterialColor bark, Supplier<Block> stripped)
    {
        super(top, bark, stripped);
    }

    public ThinLogBlock(Properties props, Supplier<Block> stripped)
    {
        super(props, stripped);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext ctx)
    {
        return SHAPE_LOOKUP[state.getValue(LogBlock.AXIS).ordinal()];
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
