package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ButterflyInvContainer extends ContainerBase<ButterflyLeviathanEntity>
{
    public ButterflyInvContainer(ButterflyLeviathanEntity dragon, PlayerInventory playerInv, int windowID) {
        super(dragon, SetupIO.BUTTERFLY_CONTAINER.get(), windowID);
        
        buildPlayerSlots(playerInv, 7, 83);
        
        dragon.getInvCap().ifPresent(i -> addSlot(new SlotItemHandler(i, 0, 127, 56) {
            @Override public boolean isItemValid(@Nonnull ItemStack stack) { return stack.getItem() == Items.CONDUIT; }
            @Override public int getSlotStackLimit() { return 1; }
            @Override public int getItemStackLimit(@Nonnull ItemStack stack) { return 1; }
        }));
    }
}
