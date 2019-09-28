package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SaddleItem;

public class OWDrakeInvContainer extends ContainerBase<OWDrakeEntity>
{
    public OWDrakeInvContainer(int windowID, PlayerInventory playerInv, OWDrakeEntity drake) {
        super(drake, SetupIO.owDrakeContainer, windowID);
        this.dragon = drake;
        
        buildPlayerSlots(playerInv, 7, 83);
        addSlot(new Slot(drake.drakeInv, 0, 75, 16) { // Chest
            @Override public boolean isItemValid(ItemStack stack) { return stack.getItem() == Blocks.CHEST.asItem(); }
    
            @Override public int getSlotStackLimit() { return 1; }
    
            @Override public void onSlotChanged() {
                inventory.markDirty();
                dragon.setHasChest(getStack().getItem() == Blocks.CHEST.asItem());
            }
    
            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                for (int i = 3; i < 19; ++i)
                    if (!drake.drakeInv.getStackInSlot(i).isEmpty()) return false;
                return true;
            }
        });
        addSlot(new Slot(drake.drakeInv, 1, 75, 33) { // Saddle
            @Override public boolean isItemValid(ItemStack stack) { return stack.getItem() instanceof SaddleItem; }
    
            @Override
            public void onSlotChanged() { dragon.setSaddled(getStack().getItem() instanceof SaddleItem); }
        });
        buildSlotArea((index, posX, posY) -> new Slot(drake.drakeInv, index, posX, posY) {
            @Override public boolean isEnabled() { return drake.hasChest(); }
        }, 3, 97, 7, 4, 4);
    }
    
    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) { return true; }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        dragon.drakeInv.closeInventory(playerIn);
    }
}
