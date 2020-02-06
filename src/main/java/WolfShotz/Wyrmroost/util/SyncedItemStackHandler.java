package WolfShotz.Wyrmroost.util;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class SyncedItemStackHandler extends ItemStackHandler
{
    private final List<IInventoryListener> listenerList = Lists.newArrayList();

    public SyncedItemStackHandler()
    {
        this(1);
    }

    public SyncedItemStackHandler(int size)
    {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public SyncedItemStackHandler(NonNullList<ItemStack> list)
    {
        super(list);
    }

    public void addListener(IInventoryListener listener)
    {
        listenerList.add(listener);
    }

    @Override
    protected void onLoad()
    {
        listenerList.forEach(l -> l.onContentsChanged(this));
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        listenerList.forEach(l -> l.onContentsChanged(this));
    }

    public interface IInventoryListener
    {
        void onContentsChanged(SyncedItemStackHandler handler);
    }
}
