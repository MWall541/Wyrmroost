package WolfShotz.Wyrmroost.util.io;

import net.minecraft.inventory.container.Slot;

/**
 * Interface used to provide index and position information (after calculated) to new Slot Objects.
 * Mostly used in building slot areas.
 */
public interface ISlotArea
{
    Slot slotPos(int index, int posX, int posY);
}