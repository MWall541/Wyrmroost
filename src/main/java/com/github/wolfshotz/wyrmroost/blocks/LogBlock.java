package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class LogBlock extends RotatedPillarBlock
{
    private final Supplier<Block> stripped;

    public LogBlock(MaterialColor top, MaterialColor bark, Supplier<Block> stripped)
    {
        super(properties(top, bark));
        this.stripped = stripped;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType)
    {
        return toolType == ToolType.AXE?
                stripped.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) :
                super.getToolModifiedState(state, world, pos, player, stack, toolType);
    }

    public static AbstractBlock.Properties properties(MaterialColor top, MaterialColor bark)
    {
        return AbstractBlock.Properties
                .of(Material.WOOD, state -> (top == bark || state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y)? top : bark)
                .strength(2f)
                .harvestTool(ToolType.AXE)
                .sound(SoundType.WOOD);
    }
}
