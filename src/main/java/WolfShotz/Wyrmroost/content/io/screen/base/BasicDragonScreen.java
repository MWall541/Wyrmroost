package WolfShotz.Wyrmroost.content.io.screen.base;

import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class BasicDragonScreen extends AbstractContainerScreen<ContainerBase>
{
    public BasicDragonScreen(ContainerBase container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawHealth(115, 5);
        if (dragonInv.dragon.isSpecial()) drawSpecialIcon(6, 11);
    }
}
