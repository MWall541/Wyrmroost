package com.github.wolfshotz.wyrmroost.containers.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SlotBuilder extends SlotItemHandler
{
    @Nullable public Vec2d iconUV;
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

    public SlotBuilder(IInventory inventory, int index, int x, int y)
    {
        super(new InvWrapper(inventory), index, x, y);
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
        return only(s -> {
            Item item = s.getItem();
            if (Block.class.isAssignableFrom(clazz))
                return item instanceof BlockItem && clazz.isInstance(((BlockItem) item).getBlock());
            return clazz.isInstance(item);
        });
    }

    public SlotBuilder not(IItemProvider item)
    {
        return only(s -> s.getItem() != item);
    }

    public SlotBuilder noShulkers()
    {
        return only(s -> !(Block.byItem(s.getItem()) instanceof ShulkerBoxBlock));
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

    public SlotBuilder iconUV(Vec2d uv)
    {
        this.iconUV = uv;
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

    public void blitBackgroundIcon(Screen screen, MatrixStack ms, int x, int y)
    {
        if (iconUV != null)
            screen.blit(ms, x, y, (int) iconUV.x, (int) iconUV.y, 16, 16);
    }
}
