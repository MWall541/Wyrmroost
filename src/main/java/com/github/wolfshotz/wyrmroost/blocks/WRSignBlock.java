package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.blocks.tile.WRSignTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class WRSignBlock extends StandingSignBlock
{
    public WRSignBlock(Properties properties, WoodType type)
    {
        super(properties, type);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new WRSignTileEntity();
    }

    public static class Wall extends WallSignBlock
    {
        public Wall(Properties properties, WoodType type)
        {
            super(properties, type);
        }

        @Override
        public boolean hasTileEntity(BlockState state)
        {
            return true;
        }

        @Override
        public TileEntity createNewTileEntity(IBlockReader worldIn)
        {
            return new WRSignTileEntity();
        }

    }
}
