package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

public class OWDrakeInvContainer extends Container
{
    public OWDrakeInvContainer(int windowID, PlayerInventory playerInv) {
        super(SetupIO.owDrakeContainer, windowID);
        
        buildPlayerSlots(playerInv);
    }
    
    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) { return true; }
    
    private void buildSlotArea(IInventory inventory, int index, int initialX, int initialY, int length, int height) {
        for (int x=1; x <= length; ++x) {
            for (int y=1; y <= height; ++y) {
                addSlot(new Slot(inventory, index, initialX * (x * 2) - 12, initialY * y));
                ++index;
            }
        }
    }
    
    private void buildPlayerSlots(PlayerInventory playerInv) {
    
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    
        for(int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }
    
    
}
