package WolfShotz.Wyrmroost.client.screen;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.network.messages.RenameEntityPacket;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class NameFieldWidget extends TextFieldWidget
{
    public NameFieldWidget(FontRenderer font, int posX, int posY, int sizeX, int sizeY, AbstractDragonEntity dragon)
    {
        super(font, posX, posY, sizeX, sizeY, dragon.getName().getFormattedText());

        setText(getMessage());
        setCanLoseFocus(true);
        changeFocus(true);
        setTextColor(0xffffff);
        setDisabledTextColour(0xffffff);
        setEnableBackgroundDrawing(false);
        setMaxStringLength(35);
        setResponder(s ->
        {
            if (s.equals(dragon.getName().getUnformattedComponentText())) return;
            StringTextComponent name = s.isEmpty()? null : new StringTextComponent(s);
            Wyrmroost.NETWORK.sendToServer(new RenameEntityPacket(dragon, name));
        });
    }
}
