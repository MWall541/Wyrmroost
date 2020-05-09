package WolfShotz.Wyrmroost.util.io;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class ContainerBase<T extends AbstractDragonEntity> extends Container
{
    public T dragon;

    public ContainerBase(T dragon, ContainerType<?> type, int windowID)
    {
        super(type, windowID);
        this.dragon = dragon;
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) { return dragon.getDistance(playerIn) < dragon.getWidth() * 3; }


    public void buildSlotArea(ISlotArea slotArea, int index, int initialX, int initialY, int length, int height)
    {
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < length; ++x)
            {
                addSlot(slotArea.slotPos(index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
}