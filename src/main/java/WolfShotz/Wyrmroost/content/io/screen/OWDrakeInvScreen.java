package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OWDrakeInvScreen extends ContainerScreen<OWDrakeInvContainer>
{
    private static final ResourceLocation TEXTURE = ModUtils.location("textures/io/dragon/owdrake.png");
    
    private OWDrakeInvContainer drakeInv;
    
    public OWDrakeInvScreen(OWDrakeInvContainer container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        this.drakeInv = container;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(ModUtils.location("textures/io/dragon/owdrake.png"));
        int relX = (width - xSize) / 2;
        int relY = (height - ySize) / 2;
        blit(relX, relY, 0, 0, xSize, ySize);
        
        if (drakeInv.dragon.hasChest()) blit(relX + 96, relY + 6, 0, ySize, 72, 72);
        
        InventoryScreen.drawEntityOnScreen(relX + 40, relY + 60, 15, (float)(relX + 40) - mouseX, (float)(relY + 75 - 30) - mouseY, drakeInv.dragon);
    }
}
