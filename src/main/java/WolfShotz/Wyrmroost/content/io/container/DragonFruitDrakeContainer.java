package WolfShotz.Wyrmroost.content.io.container;

import WolfShotz.Wyrmroost.content.entities.dragon.dfruitdrake.DragonFruitDrakeEntity;
import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.event.SetupIO;
import net.minecraft.entity.player.PlayerInventory;

public class DragonFruitDrakeContainer extends ContainerBase<DragonFruitDrakeEntity>
{
    public DragonFruitDrakeContainer(DragonFruitDrakeEntity dragon, PlayerInventory inv, int windowID) {
        super(dragon, SetupIO.FRUIT_DRAKE_CONTAINER.get(), windowID);
        
        buildPlayerSlots(inv, 7, 83);
        
        dragon.getInvCap().ifPresent(i -> addSlot(buildSaddleSlot(i, 85, 65)));
    }
}
