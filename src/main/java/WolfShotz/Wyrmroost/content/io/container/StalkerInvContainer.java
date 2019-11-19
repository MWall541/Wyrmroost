package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.registry.SetupIO;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;

public class StalkerInvContainer extends ContainerBase<RoostStalkerEntity>
{
    public StalkerInvContainer(RoostStalkerEntity dragon, PlayerInventory playerInv, int windowID) {
        super(dragon, SetupIO.STALKER_CONTAINER.get(), windowID);
        
        buildPlayerSlots(playerInv, 7, 83);
        
        dragon.getInvCap().ifPresent(i -> addSlot(new SlotItemHandler(i, 0, 85, 65)));
    }
}
