package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.event.SetupIO;
import WolfShotz.Wyrmroost.event.SetupItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;

import java.util.function.Predicate;

public class OWDrakeInvContainer extends ContainerBase<OWDrakeEntity>
{
    public OWDrakeInvContainer(int windowID, PlayerInventory playerInv, OWDrakeEntity drake) {
        super(drake, SetupIO.owDrakeContainer, windowID);
        this.dragon = drake;
        
        buildPlayerSlots(playerInv, 7, 83);
        
        addSlot(new Slot(drake.drakeInv, 0, 75, 16) { // Chest
            @Override public boolean isItemValid(ItemStack stack) { return stack.getItem() == Items.CHEST; }
    
            @Override public int getSlotStackLimit() { return 1; }
    
            @Override
            public void onSlotChanged() { dragon.setHasChest(getStack().getItem() == Items.CHEST); }
    
            @Override
            public boolean canTakeStack(PlayerEntity playerIn) {
                for (int i = 3; i < 19; ++i)
                    if (!drake.drakeInv.getStackInSlot(i).isEmpty()) return false;
                return true;
            }
        });
        
        addSlot(new Slot(drake.drakeInv, 1, 75, 33) { // Saddle
            @Override public boolean isItemValid(ItemStack stack) { return stack.getItem() instanceof SaddleItem; }
    
            @Override public boolean isEnabled() { return !drake.isChild(); }
    
            @Override public void onSlotChanged() { dragon.setSaddled(getStack().getItem() instanceof SaddleItem); }
        });
    
        addSlot(new Slot(drake.drakeInv, 2, 75, 50) { // Armor
            Predicate<Item> isArmor = DragonArmorItem.class::isInstance;
            
            @Override public boolean isItemValid(ItemStack stack) { return isArmor.test(stack.getItem()); }
        
            @Override public void onSlotChanged() { dragon.setArmored(isArmor.test(getStack().getItem())); }
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
    
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 36) {
                if (!mergeItemStack(itemstack1, 36, 39, false))
                    if (dragon.hasChest() && !mergeItemStack(itemstack1, 39, inventorySlots.size(), false))
                        return ItemStack.EMPTY;
                    else if (!dragon.hasChest()) return ItemStack.EMPTY;
            }
            else if (!mergeItemStack(itemstack1, 0, 36, true)) return ItemStack.EMPTY;
            
            if (itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
        }
        
        return itemstack;
    }
    
    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        dragon.drakeInv.closeInventory(playerIn);
    }
}
