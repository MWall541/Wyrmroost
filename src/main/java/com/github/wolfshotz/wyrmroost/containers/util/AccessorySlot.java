package com.github.wolfshotz.wyrmroost.containers.util;

import com.github.wolfshotz.wyrmroost.client.screen.DragonStaffScreen;
import com.sun.javafx.geom.Vec2d;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

import static com.github.wolfshotz.wyrmroost.client.ClientEvents.getClient;

public class AccessorySlot extends DynamicSlot
{
    public final int sZ;
    @Nullable public final Vec2d uv;

    public AccessorySlot(IItemHandler inventory, int index, int x, int y, int z, Vec2d uv)
    {
        super(inventory, index, x, y);
        this.sZ = z;
        this.uv = uv;
    }

    public AccessorySlot(IItemHandler inventory, int index, int x, int y, int z)
    {
        this(inventory, index, x, y, z, null);
    }

    @Override
    public boolean isActive()
    {
        return super.isActive() && getClient().screen instanceof DragonStaffScreen && ((DragonStaffScreen) getClient().screen).showAccessories();
    }
}
