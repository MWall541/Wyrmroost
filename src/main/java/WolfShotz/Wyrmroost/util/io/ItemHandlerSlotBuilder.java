package WolfShotz.Wyrmroost.util.io;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemHandlerSlotBuilder extends SlotItemHandler
{
    private Supplier<Boolean> isEnabled = () -> true;
    private Consumer<ItemHandlerSlotBuilder> onSlotUpdate = s -> {};
    private Predicate<ItemStack> isItemValid = super::isItemValid;
    private Predicate<PlayerEntity> canTakeStack = super::canTakeStack;
    private int limit = super.getSlotStackLimit();

    public ItemHandlerSlotBuilder(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    public ItemHandlerSlotBuilder condition(Supplier<Boolean> isEnabled)
    {
        this.isEnabled = isEnabled;
        return this;
    }

    public ItemHandlerSlotBuilder onSlotUpdate(Consumer<ItemHandlerSlotBuilder> onUpdate)
    {
        this.onSlotUpdate = onUpdate;
        return this;
    }

    public ItemHandlerSlotBuilder only(Predicate<ItemStack> isItemValid)
    {
        this.isItemValid = isItemValid;
        return this;
    }

    public ItemHandlerSlotBuilder canTake(Predicate<PlayerEntity> canTakeStack)
    {
        this.canTakeStack = canTakeStack;
        return this;
    }

    public ItemHandlerSlotBuilder limit(int limit)
    {
        this.limit = limit;
        return this;
    }

    // ===

    @Override
    public boolean isEnabled() { return isEnabled.get(); }

    @Override
    public void onSlotChanged() { this.onSlotUpdate.accept(this); }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) { return isItemValid.test(stack); }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) { return this.canTakeStack.test(playerIn); }

    @Override
    public int getSlotStackLimit() { return limit; }
}
