package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ButterflyInvContainer extends ContainerBase<ButterflyLeviathanEntity>
{
    public ButterflyInvContainer(ButterflyLeviathanEntity dragon, PlayerInventory playerInv, int windowID) {
        super(dragon, SetupIO.butterflyContainer, windowID);
    
        buildPlayerSlots(playerInv, 7, 83);
        
        dragon.getInvCap().ifPresent(i -> addSlot(new SlotItemHandler(i, 0, 127, 56) {
            @Override public boolean isItemValid(@Nonnull ItemStack stack) { return stack.getItem() == Items.CONDUIT; }
            @Override public int getSlotStackLimit() { return 1; }
            @Override public int getItemStackLimit(@Nonnull ItemStack stack) { return 1; }
        }));
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        System.out.println(index);
        
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < 36 && !mergeItemStack(itemStack1, 36, inventorySlots.size(), false)) return ItemStack.EMPTY;
            else if (!mergeItemStack(itemStack1, 0, 37, true)) return ItemStack.EMPTY;

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
        }

        return itemStack;
    }
}
