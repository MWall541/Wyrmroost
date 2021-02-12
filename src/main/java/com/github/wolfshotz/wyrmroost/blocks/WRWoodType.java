package com.github.wolfshotz.wyrmroost.blocks;

import net.minecraft.block.WoodType;

/**
 * @deprecated https://github.com/MinecraftForge/MinecraftForge/pull/7623
 */
public class WRWoodType extends WoodType
{
    public static final WoodType OSERI = new WRWoodType("oseri");

    public WRWoodType(String nameIn)
    {
        super(nameIn);
        WoodType.register(this);
    }
}