package WolfShotz.Wyrmroost.content.io.screen.base;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.io.ContainerBase;
import WolfShotz.Wyrmroost.util.io.NameFieldWidget;
import WolfShotz.Wyrmroost.util.network.messages.EntityRenameMessage;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

// #blit(xPos, yPos, initialX pixel, initialY pixel, x width, y width, .png width, .png height);
@OnlyIn(Dist.CLIENT)
public class ContainerScreenBase<T extends ContainerBase<?>> extends ContainerScreen<T>
{
    public static final ResourceLocation STANDARD_GUI = Wyrmroost.rl("textures/io/dragonscreen/dragoninv.png");
    public static final ResourceLocation HEART = new ResourceLocation("textures/particle/heart.png");
    public static final ResourceLocation SPECIAL = new ResourceLocation("textures/particle/glitter_7.png");

    public T dragonInv;
    public ResourceLocation background;
    public Random rand = new Random();
    public NameFieldWidget nameField;
    public String prevName;
    public int textureWidth, textureHeight;
    
    public ContainerScreenBase(T container, PlayerInventory playerInv, ITextComponent name)
    {
        super(container, playerInv, name);
        this.dragonInv = container;
        background = STANDARD_GUI;
        textureWidth = textureHeight = 256;
        ySize = 164;
    }
    
    @Override
    protected void init()
    {
        super.init();
        prevName = dragonInv.dragon.hasCustomName()? dragonInv.dragon.getCustomName().getUnformattedComponentText() : dragonInv.dragon.getDisplayName().getUnformattedComponentText();
        nameField = createNameField();
    }
    
    @Override
    public void tick()
    {
        super.tick();
        nameField.tick();
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
        nameField.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        drawHealth(115, 5);
        if (dragonInv.dragon.isSpecial()) drawSpecialIcon(6, 11);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        renderEntity(mouseX, mouseY);
        bindTexture(background); // Bind background layer last so we can do other rendering in subclasses
        blit(guiLeft, guiTop, 0, 0, xSize, ySize, textureWidth, textureHeight);
    }
    
    @Override
    public boolean keyPressed(int p1, int p2, int p3)
    {
        nameField.keyPressed(p1, p2, p3);
        
        if (nameField.isFocused() && p1 == 257)
        {
            if (!dragonInv.dragon.getName().getUnformattedComponentText().equals(nameField.getText()))
                Wyrmroost.NETWORK.sendToServer(new EntityRenameMessage(dragonInv.dragon, new StringTextComponent(nameField.getText())));
            nameField.setFocused2(false);
        }
        
        return nameField.isFocused() && nameField.getVisible() && p1 != 256 || super.keyPressed(p1, p2, p3);
    }
    
    public void drawHealth(int x, int y)
    {
        int health = (int) Math.floor(dragonInv.dragon.getHealth() / 2);
        boolean shake = health < dragonInv.dragon.getMaxHealth() / 5;
        
        GlStateManager.pushMatrix();
        if (shake) GlStateManager.translated(0, rand.nextDouble() * 2d, 0);
        bindTexture(HEART);
        blit(x, y, 0, 0, 15, 15, 15, 15);
        drawCenteredString(font, Integer.toString(health), x + 8, y + 5, 0xffffff);
        GlStateManager.popMatrix();
    }
    
    public void drawSpecialIcon(int x, int y)
    {
        GlStateManager.pushMatrix();
        GlStateManager.color4f(1f, 0.65f, 0, 1);
        bindTexture(SPECIAL);
        blit(x, y, 0, 0, 10, 10, 10, 10);
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();
    }

    /**
     * Public access version of {@link net.minecraft.client.gui.screen.Screen#addButton(Widget)}
     */
    @Override
    public <T extends Widget> T addButton(T widget) { return super.addButton(widget); }
    
    public List<IGuiEventListener> getChildren()
    {
        return children;
    }
    
    public void renderEntity(int mouseX, int mouseY)
    {
        InventoryScreen.drawEntityOnScreen(guiLeft + 40, guiTop + 60, 15, (guiLeft + 40) - mouseX, (guiTop + 75 - 30) - mouseY, dragonInv.dragon);
    }

    public NameFieldWidget createNameField()
    {
        return new NameFieldWidget(font, guiLeft + 72, guiTop + 22, 100, 12, this);
    }

    public void bindTexture(ResourceLocation texture)
    {
        assert minecraft != null;
        minecraft.getTextureManager().bindTexture(texture);
    }

    public static class BasicEntityScreen<T extends ContainerBase<?>> extends ContainerScreenBase<T>
    {
        public BasicEntityScreen(T container, PlayerInventory playerInv, ITextComponent name)
        {
            super(container, playerInv, name);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
        {
            super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
            blit(guiLeft + 70, guiTop + 55, 0, 164, 18, 18, textureWidth, textureHeight);
        }

        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
        {
            super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        }
    }
}