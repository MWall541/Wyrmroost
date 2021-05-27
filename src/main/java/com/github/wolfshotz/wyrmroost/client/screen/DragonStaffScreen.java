package com.github.wolfshotz.wyrmroost.client.screen;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.containers.DragonStaffContainer;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DragonStaffScreen extends ContainerScreen<DragonStaffContainer>
{
    private static final ResourceLocation SPRITES = Wyrmroost.id("textures/gui/container/dragon_inventory.png");

    private final LerpedFloat collapsedTime = new LerpedFloat().clamp(0, 1);

    public DragonStaffScreen(DragonStaffContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
        this.imageWidth = 193;
        this.imageHeight = 97;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTick);
        renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack ms, float partialTicks, int mouseX, int mouseY)
    {
        getMinecraft().getTextureManager().bind(SPRITES);
        float speed = 0.35f * partialTicks;
        int xMid = (width - 193) / 2;
        boolean flag = mouseX > xMid && ((collapsedTime.get() == 1 && mouseY > height - 87) || mouseY > height - 35);
        collapsedTime.add(flag? speed : -speed);
        int y = height - (20 + (int) (75 * collapsedTime.get(partialTicks)));
        blit(ms, xMid, y, 0, 0, imageWidth, imageHeight);


    }

    @Override
    protected void renderLabels(MatrixStack ms, int mouseX, int mouseY)
    {
    }
}
