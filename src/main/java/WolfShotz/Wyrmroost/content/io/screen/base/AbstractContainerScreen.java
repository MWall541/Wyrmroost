package WolfShotz.Wyrmroost.content.io.screen.base;

import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

public abstract class AbstractContainerScreen<T extends ContainerBase> extends ContainerScreen<T>
{
    public static final ResourceLocation GUI = ModUtils.location("textures/io/dragoninv.png");
    public static final ResourceLocation HEART = new ResourceLocation("textures/particle/heart.png");
    public static final ResourceLocation SPECIAL = new ResourceLocation("textures/particle/glitter_7.png");
    public static final ResourceLocation SLOT = ModUtils.location("textures/io/slots/slot.png");
    public static final ResourceLocation SLOT_ARMOR = ModUtils.location("textures/io/slots/armor_slot.png");
    public static final ResourceLocation SLOT_SADDLE = ModUtils.location("textures/io/slots/saddle_slot.png");
    
    public T dragonInv;
    private Random rand = new Random();
    int relX;
    int relY;
    
    public AbstractContainerScreen(T container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        this.dragonInv = container;
    }
    
    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        relX = (width - xSize) / 2;
        relY = (height - ySize) / 2;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(GUI);
        blit(relX, relY, 0, 0, xSize, ySize);
    
        InventoryScreen.drawEntityOnScreen(relX + 40, relY + 60, 15, (float)(relX + 40) - mouseX, (float)(relY + 75 - 30) - mouseY, dragonInv.dragon);
    }
    
    public void drawSlot(int x, int y) {
        bindTexture(SLOT);
        blit(relX + x, relY + y, 0, 0, 18, 18, 18, 18);
    }
    
    public void drawWithSlotDim(ResourceLocation slot, int x, int y) {
        bindTexture(slot);
        blit(relX + x, relY + y, 0, 0, 18, 18, 18, 18);
    }
    
    public void drawRowedSlots(int initialX, int initialY, int rows, int columns) {
        bindTexture(SLOT);
        
        for (int row=0; row < rows; ++row)
            for (int column=0; column < columns; ++column) {
                blit(relX + initialX + row * 18, relY + initialY + column * 18, 0, 0, 18, 18, 18, 18);
            }
    }
    
    public void drawHealth(int x, int y) {
        int health = (int) Math.floor(dragonInv.dragon.getHealth() / 2);
        boolean shake = health < dragonInv.dragon.getMaxHealth() / 5;
        
        GlStateManager.pushMatrix();
        if (shake) GlStateManager.translated(0, rand.nextDouble() * 2d, 0);
        bindTexture(HEART);
        blit(x, y, 0, 0, 15, 15, 15, 15);
        drawCenteredString(font, Integer.toString(health), x + 8, y + 5, 0xffffff);
        GlStateManager.popMatrix();
    }
    
    public void drawSpecialIcon(int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1f, 0.65f, 0, 1);
        bindTexture(SPECIAL);
        blit(x, y, 0, 0, 10, 10, 10, 10);
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();
    }
    
    public void bindTexture(ResourceLocation texture) { minecraft.getTextureManager().bindTexture(texture); }
}
