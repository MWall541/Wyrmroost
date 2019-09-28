package WolfShotz.Wyrmroost.content.io.container.base;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

public abstract class ContainerBase<T extends AbstractDragonEntity> extends Container
{
    public T dragon;
    
    public ContainerBase(T dragon, ContainerType<?> type, int windowID) {
        super(type, windowID);
        this.dragon = dragon;
    }
    
    public void buildSlotArea(IInventory inventory, int index, int initialX, int initialY, int length, int height) {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < length; ++x) {
                addSlot(new Slot(inventory, index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
    
    public void buildSlotArea(ISlotArea slotArea, int index, int initialX, int initialY, int length, int height) {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < length; ++x) {
                addSlot(slotArea.slotPos(index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
    
    public void buildPlayerSlots(PlayerInventory playerInv, int initialX, int initialY) {
        buildSlotArea(playerInv, 9, initialX, initialY, 9, 3); // Player inv 83
        buildSlotArea(playerInv, 0, initialX, initialY + 58, 9, 1); // Hotbar
    }
    
    /**
     * Interface used to provide index and position information (after calculated) to new Slot Objects.
     * Mostly used in building slot areas.
     */
    public interface ISlotArea
    {
        Slot slotPos(int index, int posX, int posY);
    }
}
