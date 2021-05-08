package com.github.wolfshotz.wyrmroost.entities.dragon.helpers;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;

/**
 * todo: hold slots here and have the container work with them from here
 */
public class DragonInventory extends ItemStackHandler implements INamedContainerProvider
{
    public final TameableDragonEntity dragon;

    public DragonInventory(TameableDragonEntity dragon, int size)
    {
        super(size);
        this.dragon = dragon;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        dragon.onInvContentsChanged(slot, dragon.getStackInSlot(slot), false);
    }

    @Override
    protected void onLoad()
    {
        for (int i = 0; i < stacks.size(); i++) dragon.onInvContentsChanged(i, stacks.get(i), true);
    }

    public boolean isEmpty()
    {
        if (stacks.isEmpty()) return true;
        for (ItemStack stack : stacks) if (!stack.isEmpty()) return false;
        stacks.clear(); // shouldn't even have empty stacks?
        return true;
    }

    public boolean isEmptyAfter(int slot)
    {
        if (stacks.isEmpty()) return true;
        if (slot > stacks.size())
        {
            Wyrmroost.LOG.warn("slot's too high but ok..");
            return true;
        }
        for (ItemStack stack : stacks) if (stacks.indexOf(stack) > slot && !stack.isEmpty()) return false;
        return true;
    }

    public NonNullList<ItemStack> getContents()
    {
        return stacks;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent("Dragon Inventory");
    }

    @Override
    public Container createMenu(int id, PlayerInventory playersInv, PlayerEntity player)
    {
        return new DragonInvContainer(this, playersInv, id);
    }
}
