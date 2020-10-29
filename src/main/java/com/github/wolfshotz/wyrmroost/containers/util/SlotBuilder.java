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

    private int limit = super.getSlotStackLimit();
    private BooleanSupplier isEnabled = () -> true;
    private Predicate<ItemStack> isItemValid = super::isItemValid;
    private Predicate<PlayerEntity> canTakeStack = super::canTakeStack;
    private Consumer<SlotBuilder> onSlotUpdate = s ->
    {};

    public SlotBuilder(IItemHandler handler, int index, int posX, int posY)
    {
        super(handler, index, posX, posY);
    }

    public SlotBuilder(IItemHandler handler, int index) { this(handler, index, CENTER_X, CENTER_Y); }

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

    public SlotBuilder only(IItemProvider item) { return only(s -> s.getItem() == item); }

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
    public boolean isEnabled() { return isEnabled.getAsBoolean(); }

    @Override
    public void onSlotChanged() { this.onSlotUpdate.accept(this); }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) { return isEnabled() && isItemValid.test(stack); }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) { return this.canTakeStack.test(playerIn); }

    @Override
    public int getSlotStackLimit() { return limit; }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) { return limit; }
}
