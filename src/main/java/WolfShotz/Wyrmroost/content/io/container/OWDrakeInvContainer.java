package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.owdrake.OWDrakeEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.content.items.DragonArmorItem;
import WolfShotz.Wyrmroost.registry.SetupIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class OWDrakeInvContainer extends ContainerBase<OWDrakeEntity>
{
    public OWDrakeInvContainer(OWDrakeEntity drake, PlayerInventory playerInv, int windowID)
    {
        super(drake, SetupIO.OWDRAKE_CONTAINER.get(), windowID);

        buildPlayerSlots(playerInv, 7, 83);

        dragon.invHandler.ifPresent(h -> {
            addSlot(buildSaddleSlot(h, 73, 16));
            addSlot(buildArmorSlot(h, 73, 34));
            addSlot(buildChestSlot(h, 2, 73, 52, 3, 19));

            buildSlotArea((index, posX, posY) -> new SlotItemHandler(h, index, posX, posY)
            {
                @Override
                public boolean isEnabled() { return drake.hasChest(); }
            }, 3, 97, 7, 4, 4);
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

    public SlotItemHandler buildSaddleSlot(IItemHandler handler, int x, int y)
    {
        return new SlotItemHandler(handler, 0, x, y)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.SADDLE;
            }

            @Override
            public boolean isEnabled() { return !dragon.isChild(); }

            @Override
            public void onSlotChanged() { dragon.setSaddled(!getStack().isEmpty()); }
        };
    }

    public SlotItemHandler buildArmorSlot(IItemHandler handler, int x, int y)
    {
        return new SlotItemHandler(handler, 1, x, y)
        {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) { return stack.getItem() instanceof DragonArmorItem; }

            @Override
            public void onSlotChanged()
            {
                DragonArmorItem.setDragonArmored(dragon, 1);
                dragon.setArmor(getStack().getItem());
            }
        };
    }

    public SlotItemHandler buildChestSlot(IItemHandler handler, int index, int x, int y, int index1, int index2)
    {
        return new SlotItemHandler(handler, index, x, y)
        {
            @Override
            public boolean isItemValid(ItemStack stack) { return stack.getItem() == Items.CHEST; }

            @Override
            public int getSlotStackLimit() { return 1; }

            @Override
            public int getItemStackLimit(@Nonnull ItemStack stack) { return 1; }

            @Override
            public boolean canTakeStack(PlayerEntity playerIn)
            {
                for (int i = index1; i < index2; ++i)
                    if (!handler.getStackInSlot(i).isEmpty()) return false;
                return true;
            }
        };
    }
}
