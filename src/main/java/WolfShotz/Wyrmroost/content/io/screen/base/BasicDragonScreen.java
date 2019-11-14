package WolfShotz.Wyrmroost.content.io.screen.base;

import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class BasicDragonScreen extends AbstractContainerScreen<ContainerBase>
{
    private boolean singleSlot;
    
    public BasicDragonScreen(ContainerBase container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawHealth(115, 5);
        if (dragonInv.dragon.isSpecial()) drawSpecialIcon(6, 11);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (singleSlot) blit(guiLeft + 70, guiTop + 55, 0, 164, 18, 18, textureWidth, textureHeight);
        InventoryScreen.drawEntityOnScreen(guiLeft + 40, guiTop + 60, 15, (float)(guiLeft + 40) - mouseX, (float)(guiTop + 75 - 30) - mouseY, dragonInv.dragon);
    }
    
    public static BasicDragonScreen singleSlotScreen(ContainerBase containerBase, PlayerInventory playerInventory, ITextComponent name) {
        BasicDragonScreen screen = new BasicDragonScreen(containerBase, playerInventory, name);
        screen.singleSlot = true;
        return screen;
    }
    
    @Override
    public TextFieldWidget initNameField() { return new TextFieldWidget(font, guiLeft + 90, guiTop + 25, 80, 12, prevName); }
}
