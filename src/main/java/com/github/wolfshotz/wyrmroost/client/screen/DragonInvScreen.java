package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.containers.DragonInvContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y)
    {
        renderBackground(ms);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        int midX = (width - xSize) / 2;
        int midY = (height - ySize) / 2;
        blit(ms, midX, midY, 0, 0, xSize, ySize);

        for (Slot slot : container.inventorySlots)
            if (slot.isEnabled())
                blit(ms, (midX + slot.xPos) - 1, (midY + slot.yPos) - 1, 194, 0, 18, 18);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y)
    {
        String name = container.inventory.dragon.getName().getUnformattedComponentText();
        font.drawString(ms, name, (float) (xSize / 2 - font.getStringWidth(name) / 2), 6f, 0x404040);

        font.drawString(ms, playerInventory.getDisplayName().getString(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }
}
