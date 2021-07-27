package com.github.wolfshotz.wyrmroost.blocks;

import com.github.wolfshotz.wyrmroost.blocks.tile.WRSignBlockEntity;
import com.github.wolfshotz.wyrmroost.registry.WRBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

public class WRSignBlock extends StandingSignBlock implements WRBlockEntities.Validator
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
    public TileEntity newBlockEntity(IBlockReader level)
    {
        return new WRSignBlockEntity();
    }

    @Override
    public boolean isValidEntity(TileEntityType<?> type)
    {
        return type == WRBlockEntities.CUSTOM_SIGN.get();
    }

    public static class Wall extends WallSignBlock implements WRBlockEntities.Validator
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
        public TileEntity newBlockEntity(IBlockReader level)
        {
            return new WRSignBlockEntity();
        }

        @Override
        public boolean isValidEntity(TileEntityType<?> type)
        {
            return type == WRBlockEntities.CUSTOM_SIGN.get();
        }
    }
}
