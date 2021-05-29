package com.github.wolfshotz.wyrmroost.containers.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class DynamicSlot extends SlotBuilder
{
    public final int sX;
    public final int sY;
    public final boolean isPlayerSlot;

    public DynamicSlot(IItemHandler inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.sX = x;
        this.sY = y;
        this.isPlayerSlot = false;
    }

    public DynamicSlot(IInventory inventory, int index, int x, int y)
    {
        super(new InvWrapper(inventory), index, x, y);
        this.sX = x;
        this.sY = y;
        this.isPlayerSlot = inventory instanceof PlayerInventory;
    }
}
