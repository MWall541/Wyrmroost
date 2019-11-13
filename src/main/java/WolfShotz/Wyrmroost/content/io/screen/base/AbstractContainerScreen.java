package WolfShotz.Wyrmroost.content.io.screen.base;

import WolfShotz.Wyrmroost.content.io.container.base.ContainerBase;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

// #blit(xPos, yPos, initialX pixel, initialY pixel, x width, y width, .png width, .png height);
public abstract class AbstractContainerScreen<T extends ContainerBase> extends ContainerScreen<T>
{
    public static final ResourceLocation STANDARD_GUI = ModUtils.location("textures/io/dragoninv.png");
    public static final ResourceLocation HEART = new ResourceLocation("textures/particle/heart.png");
    public static final ResourceLocation SPECIAL = new ResourceLocation("textures/particle/glitter_7.png");
    
    public T dragonInv;
    public ResourceLocation background;
    public Random rand = new Random();
    public TextFieldWidget nameField;
    private String prevName;
    public int textureWidth, textureHeight;
    
    public AbstractContainerScreen(T container, PlayerInventory playerInv, ITextComponent name) {
        super(container, playerInv, name);
        this.dragonInv = container;
        background = STANDARD_GUI;
        textureWidth = textureHeight = 256;
    }
    
    @Override
    protected void init() {
        super.init();
        prevName = dragonInv.dragon.hasCustomName()? dragonInv.dragon.getCustomName().getUnformattedComponentText() : dragonInv.dragon.getDisplayName().getUnformattedComponentText();
        nameField = new TextFieldWidget(font, guiLeft + 6, guiTop, 80, 12, prevName);
        nameField.setMaxStringLength(16);
        nameField.setTextColor(16777215);
        nameField.setEnableBackgroundDrawing(false);
        nameField.setText(prevName);
        children.add(nameField);
        GlStateManager.color4f(1f ,1f, 1f, 1f);
    }
    
    @Override
    public void tick() {
        super.tick();
        nameField.tick();
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
        
        bindTexture(background); // Bind background layer last so we can do other rendering in subclasses
        blit(guiLeft, guiTop, 0, 0, xSize, ySize, textureWidth, textureHeight);
        renderEntity(mouseX, mouseY);
        nameField.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean keyPressed(int p1, int p2, int p3) {
        nameField.keyPressed(p1, p2, p3);
        return nameField.isFocused() && nameField.getVisible() && p1 != 256 || super.keyPressed(p1, p2, p3);
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
    
    public void renderEntity(int mouseX, int mouseY) { InventoryScreen.drawEntityOnScreen(guiLeft + 40, guiTop + 60, 15, (guiLeft + 40) - mouseX, (guiTop + 75 - 30) - mouseY, dragonInv.dragon); }
    
    public void drawSpecialIcon(int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1f, 0.65f, 0, 1);
        bindTexture(SPECIAL);
        blit(x, y, 0, 0, 10, 10, 10, 10);
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();
    }
    
    public void bindTexture(ResourceLocation texture) {
        assert minecraft != null;
        minecraft.getTextureManager().bindTexture(texture);
    }
}
