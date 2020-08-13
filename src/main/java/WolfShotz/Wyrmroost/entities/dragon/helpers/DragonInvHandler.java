package WolfShotz.Wyrmroost.entities.dragon.helpers;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

/**
 * todo: hold slots here and have the container work with them from here
 */
public class DragonInvHandler extends ItemStackHandler
{
    public final AbstractDragonEntity dragon;

    public DragonInvHandler(AbstractDragonEntity dragon, int size)
    {
        super(size);
        this.dragon = dragon;
    }

    @Override
    protected void onContentsChanged(int slot) { dragon.onInvContentsChanged(slot, dragon.getStackInSlot(slot), false); }

    @Override
    protected void onLoad()
    {
        for (ItemStack stack : stacks) dragon.onInvContentsChanged(stacks.indexOf(stack), stack, true);
    }

    public boolean isEmpty()
    {
        if (stacks.isEmpty()) return true;
        for (ItemStack stack : stacks) if (!stack.isEmpty()) return false;
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

    public NonNullList<ItemStack> getStacks() { return stacks; }
}
