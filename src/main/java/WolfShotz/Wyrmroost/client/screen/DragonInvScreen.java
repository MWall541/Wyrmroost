package WolfShotz.Wyrmroost.client.screen;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.containers.DragonInvContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.SlotItemHandler;

public class DragonInvScreen extends ContainerScreen<DragonInvContainer>
{
    public static final ResourceLocation TEXTURE = Wyrmroost.rl("textures/io/dragon_inv_screen.png");

    public DragonInvScreen(DragonInvContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        xSize = 194;
        ySize = 220;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        renderBackground();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        blit(x, y, 0, 0, xSize, ySize);

        for (Slot slot : container.inventorySlots)
            if (slot.isEnabled() && slot instanceof SlotItemHandler)
                blit((x + slot.xPos) - 1, (y + slot.yPos) - 1, 194, 0, 18, 18);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String name = container.inventory.dragon.getName().getUnformattedComponentText();
        font.drawString(name, (float) (xSize / 2 - font.getStringWidth(name) / 2), 6f, 0x404040);

//        font.drawString(playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }
}
