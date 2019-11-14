package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.io.container.ButterflyInvContainer;
import WolfShotz.Wyrmroost.content.io.screen.base.AbstractContainerScreen;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ButterflyInvScreen extends AbstractContainerScreen<ButterflyInvContainer>
{
    public static final ResourceLocation BUTTERFLY_GUI = ModUtils.location("textures/io/dragonscreen/butterflyinv.png");
    
    public ButterflyInvScreen(ButterflyInvContainer container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        background = BUTTERFLY_GUI;
        xSize = 174;
        ySize = 164;
        textureWidth = 174;
        textureHeight = 164;
    }
    
    @Override
    protected void init() {
        nameField = new TextFieldWidget(font, guiLeft + 6, guiTop, 80, 12, prevName);
        super.init();
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHealth(140, 2);
    }
    
    @Override
    public void renderEntity(int mouseX, int mouseY) {
        InventoryScreen.drawEntityOnScreen(guiLeft + 65, guiTop + 53, 9, (guiLeft + 65) - mouseX, (guiTop + 53) - mouseY, dragonInv.dragon);
    }
}
