package com.github.wolfshotz.wyrmroost.blocks.tile;

import com.github.wolfshotz.wyrmroost.registry.WRBlockEntities;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class WRSignBlockEntity extends SignTileEntity
{
    @Override
    public TileEntityType<?> getType()
    {
        return WRBlockEntities.CUSTOM_SIGN.get();
    }
}
