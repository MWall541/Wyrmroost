package WolfShotz.Wyrmroost.content.io.container.base;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.event.SetupIO;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BasicSlotInvContainer<T extends AbstractDragonEntity> extends ContainerBase<T>
{
    public BasicSlotInvContainer(T dragon, ContainerType type, PlayerInventory inv, int windowID, int playerX, int playerY, ISlotSupplier slots) {
        super(dragon, type, windowID);
        
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
    
    public static BasicSlotInvContainer<ButterflyLeviathanEntity> butterflyContainer(ButterflyLeviathanEntity butterfly, PlayerInventory inv, int windowID) {
        return new BasicSlotInvContainer<>(butterfly, SetupIO.BUTTERFLY_CONTAINER.get(), inv, windowID, 7, 83, i -> new SlotItemHandler[] {
                new SlotItemHandler(i, 0, 127, 56) {
                    @Override public boolean isItemValid(@Nonnull ItemStack stack) { return stack.getItem() == Items.CONDUIT; }
                    @Override public int getSlotStackLimit() { return 1; }
                    @Override public int getItemStackLimit(@Nonnull ItemStack stack) { return 1; }
                }
        });
    }
    
    public static BasicSlotInvContainer<RoostStalkerEntity> stalkerContainer(RoostStalkerEntity stalker, PlayerInventory inv, int windowID) {
        return new BasicSlotInvContainer<>(stalker, SetupIO.STALKER_CONTAINER.get(), inv, windowID, 7, 83, i -> new SlotItemHandler[] { new SlotItemHandler(i, 0, 127, 56) });
    }
    
    public static BasicSlotInvContainer<DragonFruitDrakeEntity> dragonFruitContainer(DragonFruitDrakeEntity fruitDrake, PlayerInventory inv, int windowID) {
        return new BasicSlotInvContainer<>(fruitDrake, SetupIO.FRUIT_DRAKE_CONTAINER.get(), inv, windowID, 7, 83, i -> new SlotItemHandler[] {
                ContainerBase.buildSaddleSlot(fruitDrake, i, 127, 56)
        });
    }
}
