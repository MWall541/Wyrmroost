package WolfShotz.Wyrmroost.content.io;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.io.screen.base.ContainerScreenBase;
import WolfShotz.Wyrmroost.util.network.messages.EntityRenameMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;

public class NameFieldWidget extends TextFieldWidget
{
    private Entity entity;
    private ResetNameButton button;
    
    public NameFieldWidget(FontRenderer font, int posX, int posY, int sizeX, int sizeY, ContainerScreenBase screen) {
        super(font, posX, posY, sizeX, sizeY, screen.prevName);
        this.entity = screen.dragonInv.dragon;
    
        setEnableBackgroundDrawing(false);
        setMaxStringLength(16);
        setTextColor(16777215);
        setText(getMessage());
        screen.getChildren().add(this);
        screen.addButton(button = new ResetNameButton((posX + sizeX), (posY + sizeY) - 13, 15, 15, entity));
    }
    
    @Override
    public void tick() {
        super.tick();
        setEnableBackgroundDrawing(isFocused());
        
        if (!isFocused() && !entity.getDisplayName().getUnformattedComponentText().equals(getText()))
            setText(entity.getDisplayName().getUnformattedComponentText());
        
        button.visible = entity.hasCustomName();
    }
    
    @Override
    public boolean keyPressed(int p1, int p2, int p3) {
        if (isFocused() && p1 == 257) {
            if (!entity.getName().getUnformattedComponentText().equals(getText()))
                Wyrmroost.NETWORK.sendToServer(new EntityRenameMessage(entity, new StringTextComponent(getText())));
            setFocused2(false);
        }
        
        return super.keyPressed(p1, p2, p3);
    }
    
    public static class ResetNameButton extends Button
    {
        public ResetNameButton(int widthIn, int heightIn, int width, int height, Entity entity) {
            super(widthIn, heightIn, width, height, "", b -> Wyrmroost.NETWORK.sendToServer(new EntityRenameMessage(entity, null)));
        }
        
        @Override
        public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
            Minecraft.getInstance().getTextureManager().bindTexture(ContainerScreenBase.WIDGETS);
            if (isHovered) blit(x - 1, y, 15, 1, 13, 12, 30, 15);
            else blit(x, y, 1, 1, 11, 12, 30, 15);
        }
    }
}
