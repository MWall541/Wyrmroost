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
    public static final ResourceLocation TEXTURE = Wyrmroost.id("textures/io/dragon_inventory.png");

    public DragonInvScreen(DragonInvContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        leftPos = 194;
        topPos = 220;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
    {
        renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int x, int y)
    {
        renderBackground(ms);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(TEXTURE);
        int midX = (width - x) / 2;
        int midY = (height - y) / 2;
        blit(ms, midX, midY, 0, 0, x, y);

        for (Slot slot : menu.slots)
            if (slot.isActive())
                blit(ms, (midX + slot.x) - 1, (midY + slot.y) - 1, 194, 0, 18, 18);
    }

    @Override
    protected void renderLabels(MatrixStack ms, int x, int y)
    {
        String name = menu.inventory.dragon.getName().getString();
        font.draw(ms, name, (float) (x / 2 - font.width(name) / 2), 6f, 0x404040);

        font.draw(ms, inventory.getDisplayName().getString(), 8.0F, this.height - 96f + 2f, 4210752);
    }
}
