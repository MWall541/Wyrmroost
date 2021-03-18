package com.github.wolfshotz.wyrmroost.containers.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SlotBuilder extends SlotItemHandler
{
    public static final int CENTER_X = 89;
    public static final int CENTER_Y = 60;

    private int limit = super.getMaxStackSize();
    private BooleanSupplier isEnabled = () -> true;
    private Predicate<ItemStack> isItemValid = super::mayPlace;
    private Predicate<PlayerEntity> canTakeStack = super::mayPickup;
    private Consumer<SlotBuilder> onSlotUpdate = s ->
    {
    };

    public SlotBuilder(IItemHandler handler, int index, int posX, int posY)
    {
        super(handler, index, posX, posY);
    }

    public SlotBuilder(IItemHandler handler, int index)
    {
        this(handler, index, CENTER_X, CENTER_Y);
    }

    public SlotBuilder condition(BooleanSupplier isEnabled)
    {
        this.isEnabled = isEnabled;
        return this;
    }

    public SlotBuilder onUpdate(Consumer<SlotBuilder> onUpdate)
    {
        this.onSlotUpdate = onUpdate;
        return this;
    }

    public SlotBuilder only(Predicate<ItemStack> isItemValid)
    {
        this.isItemValid = isItemValid;
        return this;
    }

    public SlotBuilder only(IItemProvider item)
    {
        return only(s -> s.getItem() == item);
    }

    public SlotBuilder only(Class<? extends IItemProvider> clazz)
    {
        if (Block.class.isAssignableFrom(clazz))
        {
            return only(s ->
            {
                Item item = s.getItem();
                return item instanceof BlockItem && clazz.isInstance(((BlockItem) item).getBlock());
            });
        }
        else return only(s -> clazz.isInstance(s.getItem()));
    }

    public SlotBuilder canTake(Predicate<PlayerEntity> canTakeStack)
    {
        this.canTakeStack = canTakeStack;
        return this;
    }

    public SlotBuilder limit(int limit)
    {
        this.limit = limit;
        return this;
    }

    // ===

    @Override
    public boolean isActive()
    {
        return isEnabled.getAsBoolean();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack)
    {
        return isItemValid.test(stack);
    }

    @Override
    public int getMaxStackSize()
    {
        return limit;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack)
    {
        return limit;
    }

    @Override
    public boolean mayPickup(PlayerEntity player)
    {
        return canTakeStack.test(player);
    }

    @Override
    public void setChanged()
    {
        onSlotUpdate.accept(this);
    }
}
