package WolfShotz.Wyrmroost.content.entities.dragon.helpers;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraftforge.items.ItemStackHandler;

public class DragonInvHandler extends ItemStackHandler
{
    private final AbstractDragonEntity dragon;

    public DragonInvHandler(AbstractDragonEntity dragon, int size)
    {
        super(size);
        this.dragon = dragon;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        dragon.onInvContentsChanged(slot, dragon.getStackInSlot(slot));
    }
}
