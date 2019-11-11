package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class BasicSlotInvContainer<T extends AbstractDragonEntity> extends ContainerBase<T>
{
    public BasicSlotInvContainer(T dragon, int windowID) {
        super(dragon, SetupIO.basicSlotContainer, windowID);
    }
    
    public BasicSlotInvContainer(T dragon, PlayerInventory inv, int windowID, int playerX, int playerY, ISlotSupplier slots) {
        this(dragon, windowID);
        
        buildPlayerSlots(inv, playerX, playerY);
        
        dragon.getInvCap().ifPresent(i -> { for (Slot slot : slots.get(i)) addSlot(slot); });
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
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
    
    public interface ISlotSupplier
    {
        Slot[] get(IItemHandler handler);
    }
}
