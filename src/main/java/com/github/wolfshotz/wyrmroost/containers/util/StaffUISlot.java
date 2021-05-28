package com.github.wolfshotz.wyrmroost.containers.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class StaffUISlot extends SlotItemHandler
{
    public final int sX;
    public final int sY;
    public final boolean isPlayerSlot;

    public StaffUISlot(IItemHandler inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.sX = x;
        this.sY = y;
        this.isPlayerSlot = false;
    }

    public StaffUISlot(IInventory inventory, int index, int x, int y)
    {
        super(new InvWrapper(inventory), index, x, y);
        this.sX = x;
        this.sY = y;
        this.isPlayerSlot = inventory instanceof PlayerInventory;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }
}
