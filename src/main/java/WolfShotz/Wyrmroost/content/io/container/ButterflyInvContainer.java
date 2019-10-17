package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.butterflyleviathan.ButterflyLeviathanEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ButterflyInvContainer extends ContainerBase<ButterflyLeviathanEntity>
{
    public ButterflyInvContainer(ButterflyLeviathanEntity dragon, PlayerInventory playerInv, int windowID) {
        super(dragon, SetupIO.butterflyContainer, windowID);
    
        buildPlayerSlots(playerInv, 7, 83);
        
        dragon.getInvCap().ifPresent(i -> addSlot(new SlotItemHandler(i, 0, 71, 57) {
            @Override public boolean isItemValid(@Nonnull ItemStack stack) { return stack.getItem() == Items.CONDUIT; }
        }));
    }
    
    
}
