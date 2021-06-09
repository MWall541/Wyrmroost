package com.github.wolfshotz.wyrmroost.containers.util;

import net.minecraftforge.items.IItemHandler;

/**
 * T H R E E  D E E
 * E.
 */
public class Slot3D extends DynamicSlot
{
    public final int anchorZ;
    public int z;

    public Slot3D(IItemHandler inventory, int index, int x, int y, int z)
    {
        super(inventory, index, x, y);
        this.anchorZ = z;
    }

    public void setPos(int x, int y, int z)
    {
        setPos(x, y);
        this.z = z;
    }
}
