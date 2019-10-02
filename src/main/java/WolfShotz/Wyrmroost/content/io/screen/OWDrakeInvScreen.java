package WolfShotz.Wyrmroost.content.io.screen;

import WolfShotz.Wyrmroost.content.io.container.OWDrakeInvContainer;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

public class OWDrakeInvScreen extends ContainerScreen<OWDrakeInvContainer>
{
    private static final ResourceLocation GUI = ModUtils.location("textures/io/dragon/owdrake.png");
    private static final ResourceLocation HEART = new ResourceLocation("textures/particle/heart.png");
    private static final ResourceLocation SPECIAL = new ResourceLocation("textures/particle/glint.png");
    
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int health = (int) Math.floor(drakeInv.dragon.getHealth() / 2);
        boolean shake = health < drakeInv.dragon.getMaxHealth() / 5;
        
        if (shake) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(0, new Random().nextDouble() * 2d, 0);
        }
            minecraft.textureManager.bindTexture(HEART);
            blit(54, 57, 0, 0, 15, 15, 15, 15);
            drawCenteredString(font, Integer.toString(health), 62, 62, 0xffffff);
        
        if (drakeInv.dragon.isSpecial()) {
            GlStateManager.pushMatrix();
            GlStateManager.color4f(1f, 0.65f, 0, 1);
            minecraft.textureManager.bindTexture(new ResourceLocation("textures/particle/glitter_7.png"));
            blit(6, 11, 0, 0, 10, 10, 10, 10);
            GlStateManager.color4f(1f, 1f, 1f, 1f);
            GlStateManager.popMatrix();
        }
        
        if (shake) GlStateManager.popMatrix();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(GUI);
        int relX = (width - xSize) / 2;
        int relY = (height - ySize) / 2;
        blit(relX, relY, 0, 0, xSize, ySize);
        
        if (drakeInv.dragon.hasChest()) blit(relX + 96, relY + 6, 0, ySize, 72, 72);
        
        InventoryScreen.drawEntityOnScreen(relX + 40, relY + 60, 15, (float)(relX + 40) - mouseX, (float)(relY + 75 - 30) - mouseY, drakeInv.dragon);
    }
}
