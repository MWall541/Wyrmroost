package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class OWDrakeInvContainer extends ContainerBase<OWDrakeEntity>
{
    public OWDrakeInvContainer(OWDrakeEntity drake, PlayerInventory playerInv, int windowID) {
        super(drake, SetupIO.owDrakeContainer, windowID);
        
        buildPlayerSlots(playerInv, 7, 83);
        
        dragon.getInvCap().ifPresent(h -> {
            addSlot(buildSaddleSlot(h, 73, 16));
    
            addSlot(buildArmorSlot(h, 73, 34));
            
            addSlot(new SlotItemHandler(h, 2, 73, 52) {
                @Override public boolean isItemValid(ItemStack stack) { return stack.getItem() == Items.CHEST; }
    
                @Override public int getSlotStackLimit() { return 1; }
                @Override public int getItemStackLimit(@Nonnull ItemStack stack) { return 1; }
    
    
                @Override public void onSlotChanged() { dragon.setHasChest(getStack().getItem() == Items.CHEST); }
    
                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                    for (int i = 3; i < 19; ++i)
                        if (!h.getStackInSlot(i).isEmpty()) return false;
                    return true;
                }
            });
            
            buildSlotArea((index, posX, posY) -> new SlotItemHandler(h, index, posX, posY) {
                @Override public boolean isEnabled() { return drake.hasChest(); }
            }, 3, 97, 7, 4, 4);
        });
    }
    
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
}
