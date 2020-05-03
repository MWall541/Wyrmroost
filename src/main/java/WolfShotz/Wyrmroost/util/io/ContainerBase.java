package WolfShotz.Wyrmroost.util.io;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.registry.WRIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ContainerBase<T extends AbstractDragonEntity> extends Container
{
    public T dragon;

    public ContainerBase(T dragon, ContainerType<?> type, int windowID)
    {
        super(type, windowID);
        this.dragon = dragon;
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
//        if (dragon instanceof IMultiPartEntity)
//            for (MultiPartEntity part : ((IMultiPartEntity) dragon).getParts())
//                if (part.getDistance(playerIn) < 10) return true;

        return dragon.getDistance(playerIn) < dragon.getWidth() * 3;
    }
    
    public void buildSlotArea(IInventory inventory, int index, int initialX, int initialY, int length, int height)
    {
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < length; ++x)
            {
                addSlot(new Slot(inventory, index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
    
    public void buildSlotArea(ISlotArea slotArea, int index, int initialX, int initialY, int length, int height)
    {
        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < length; ++x)
            {
                addSlot(slotArea.slotPos(index, initialX + x * 18, initialY + y * 18));
                ++index;
            }
        }
    }
    
    public void buildPlayerSlots(PlayerInventory playerInv, int initialX, int initialY)
    {
        buildSlotArea(playerInv, 9, initialX, initialY, 9, 3); // Player inv
        buildSlotArea(playerInv, 0, initialX, initialY + 58, 9, 1); // Hotbar
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

    public static class StalkerContainer extends ContainerBase<RoostStalkerEntity>
    {
        public StalkerContainer(RoostStalkerEntity stalker, PlayerInventory playerInv, int windowID)
        {
            super(stalker, WRIO.STALKER_CONTAINER.get(), windowID);
            buildPlayerSlots(playerInv, 7, 83);
            dragon.invHandler.ifPresent(i -> addSlot(new ItemHandlerSlotBuilder(i, 0, 71, 56).only(s -> !(s.getItem() instanceof BlockItem)).onSlotUpdate(s -> stalker.setItem(s.getStack()))));
        }
    }

    public static class ButterflyContainer extends ContainerBase<ButterflyLeviathanEntity>
    {
        public ButterflyContainer(ButterflyLeviathanEntity bfly, PlayerInventory playerinv, int windowID)
        {
            super(bfly, WRIO.BUTTERFLY_CONTAINER.get(), windowID);
            buildPlayerSlots(playerinv, 7, 83);
            dragon.invHandler.ifPresent(i -> addSlot(new ItemHandlerSlotBuilder(i, 0, 127, 56)
                    .only(s -> s.getItem() == Items.CONDUIT)
                    .limit(1)
                    .onSlotUpdate(s -> dragon.setHasConduit(s.getStack().getItem() == Items.CONDUIT))));
        }
    }
}