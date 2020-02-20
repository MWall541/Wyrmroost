package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.registry.WRIO;
import WolfShotz.Wyrmroost.util.io.ContainerBase;
import WolfShotz.Wyrmroost.util.io.ItemHandlerSlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SaddleItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OWDrakeInvContainer extends ContainerBase<OWDrakeEntity>
{
    public OWDrakeInvContainer(OWDrakeEntity drake, PlayerInventory playerInv, int windowID)
    {
        super(drake, WRIO.OWDRAKE_CONTAINER.get(), windowID);

        buildPlayerSlots(playerInv, 7, 83);
        dragon.invHandler.ifPresent(h -> {
            addSlot(new ItemHandlerSlotBuilder(h, 0, 73, 16).condition(() -> !dragon.isChild()).only(s -> s.getItem() instanceof SaddleItem).limit(1).onSlotUpdate(s -> dragon.setSaddled(!s.getStack().isEmpty())));
            addSlot(new ItemHandlerSlotBuilder(h, 1, 73, 34).only(s -> s.getItem() instanceof DragonArmorItem).limit(1).onSlotUpdate(s -> dragon.setArmor(s.getStack().getItem())));
            addSlot(buildChestSlot(h, 2, 73, 52, 3, 19));

            buildSlotArea((index, posX, posY) -> new ItemHandlerSlotBuilder(h, index, posX, posY).condition(drake::hasChest), 3, 97, 7, 4, 4);
        });
    }
    
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 36)
            {
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

    public SlotItemHandler buildChestSlot(IItemHandler handler, int index, int x, int y, int index1, int index2)
    {
        return new ItemHandlerSlotBuilder(handler, index, x, y)
                .only(s -> s.getItem() == Items.CHEST)
                .limit(1)
                .canTake(p -> {
                    for (int i = index1; i < index2; ++i)
                        if (!handler.getStackInSlot(i).isEmpty()) return false;
                    return true;
                });
    }
}
