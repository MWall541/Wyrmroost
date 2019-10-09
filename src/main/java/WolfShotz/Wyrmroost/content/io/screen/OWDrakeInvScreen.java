package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.io.screen.base.AbstractContainerScreen;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OWDrakeInvScreen extends AbstractContainerScreen<OWDrakeInvContainer>
{
    private static final ResourceLocation DRAKE_GUI = ModUtils.location("textures/io/owdrakeinv.png");
    
    public OWDrakeInvScreen(OWDrakeInvContainer container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        background = DRAKE_GUI;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawHealth(54, 57);
        if (dragonInv.dragon.isSpecial()) drawSpecialIcon(6, 11);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (dragonInv.dragon.hasChest()) drawRowedSlots(96, 6, 4, 4);
    }
}
