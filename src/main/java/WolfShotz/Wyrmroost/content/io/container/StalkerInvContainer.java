package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.rooststalker.RoostStalkerEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerInventory;

public class StalkerInvContainer extends ContainerBase<RoostStalkerEntity>
{
    public StalkerInvContainer(int windowID, PlayerInventory playerInv, RoostStalkerEntity stalker) {
        super(stalker, SetupIO.stalkerContainer, windowID);
    }
}
