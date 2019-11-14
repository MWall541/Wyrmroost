package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.content.io.screen.base.AbstractContainerScreen;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OWDrakeInvScreen extends AbstractContainerScreen<OWDrakeInvContainer>
{
    private static final ResourceLocation DRAKE_GUI = ModUtils.location("textures/io/dragonscreen/owdrakeinv.png");
    
    public OWDrakeInvScreen(OWDrakeInvContainer container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        background = DRAKE_GUI;
        xSize = 174;
        ySize = 163;
        textureWidth = 174;
        textureHeight = 236;
    }
    
    @Override
    protected void init() {
        nameField = new TextFieldWidget(font, guiLeft + 6, guiTop, 80, 12, prevName);
        super.init();
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawHealth(54, 57);
        if (dragonInv.dragon.isSpecial()) drawSpecialIcon(6, 11);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (dragonInv.dragon.hasChest()) blit(guiLeft + 96, guiTop + 6, 0, 164, 72, 72, textureWidth, textureHeight);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
