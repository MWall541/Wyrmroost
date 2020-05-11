package WolfShotz.Wyrmroost.content.entities.dragon.helpers;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

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
    protected void onLoad() { stacks.forEach(s -> dragon.onInvContentsChanged(stacks.indexOf(s), s, true)); }

    public boolean isEmpty()
    {
        if (stacks.isEmpty()) return true;
        return stacks.stream().allMatch(ItemStack::isEmpty);
    }

    public boolean isEmptyAfter(int slot)
    {
        if (stacks.isEmpty()) return true;
        if (slot > stacks.size())
        {
            ModUtils.L.error("slot's too high but ok..");
            return true;
        }
        return stacks.stream().filter(s -> stacks.indexOf(s) > slot).allMatch(ItemStack::isEmpty);
    }
}
