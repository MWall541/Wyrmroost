package WolfShotz.Wyrmroost.containers;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.containers.util.ISlotArea;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.entities.dragon.helpers.DragonInvHandler;
import WolfShotz.Wyrmroost.registry.WRIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.function.Function;

public class DragonInvContainer extends Container
{
    public final DragonInvHandler inventory;
    public final PlayerInventory playerInv;

    public DragonInvContainer(DragonInvHandler inv, PlayerInventory playerInv, int windowID)
    {
        super(WRIO.DRAGON_INVENTORY.get(), windowID);
        this.inventory = inv;
        this.playerInv = playerInv;
        inv.dragon.addContainerInfo(this);
    }

    public static INamedContainerProvider getProvider(AbstractDragonEntity dragon)
    {
        return new INamedContainerProvider()
        {
            @Override
            public ITextComponent getDisplayName() { return new StringTextComponent("Dragon Inventory"); }

            @Nullable
            @Override
            public Container createMenu(int id, PlayerInventory playersInv, PlayerEntity player)
            {
                return new DragonInvContainer(dragon.getInvHandler(), playersInv, id);
            }
        };
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) { return inventory.dragon.getOwner() == playerIn; }

    @Override
    public Slot addSlot(Slot slotIn) { return super.addSlot(slotIn); }

    public Slot addSlot(Function<DragonInvHandler, SlotItemHandler> slotFunc)
    {
        return addSlot(slotFunc.apply(inventory));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < 36 && !mergeItemStack(itemStack1, 36, inventorySlots.size(), false)) return ItemStack.EMPTY;
            else if (!mergeItemStack(itemStack1, 0, 37, true)) return ItemStack.EMPTY;

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
        }

        return itemStack;
    }

    public void makeSlots(int firstIndex, int firstX, int firstY, int length, int height, ISlotArea slot)
    {
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < length; ++x)
            {
                if (inventory.getSlots() <= firstIndex)
                {
                    Wyrmroost.LOG.error("TOO MANY SLOTS! ABORTING THE REST! Ended Index: {}, Supposed to be: {}", firstIndex, length * height);
                    return;
                }
                addSlot(slot.get(firstIndex, firstX + x * 18, firstY + y * 18));
                ++firstIndex;
            }
        }
    }

    public void addSlots(IInventory inventory, int index, int initialX, int initialY, int length, int height)
    {
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < length; ++x)
            {
                if (inventory.getSizeInventory() <= index)
                {
                    Wyrmroost.LOG.error("TOO MANY SLOTS! ABORTING THE REST!");
                    return;
                }
                addSlot(new Slot(inventory, index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }

    public void buildPlayerSlots(PlayerInventory playerInv, int initialX, int initialY)
    {
        addSlots(playerInv, 9, initialX, initialY, 9, 3); // Player inv
        addSlots(playerInv, 0, initialX, initialY + 58, 9, 1); // Hotbar
    }
}
