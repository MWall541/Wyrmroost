package WolfShotz.Wyrmroost.client.screen.widgets;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.network.packets.RenameEntityPacket;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class NameFieldWidget extends TextFieldWidget
{
    public NameFieldWidget(FontRenderer font, int posX, int posY, int sizeX, int sizeY, AbstractDragonEntity dragon)
    {
        super(font, posX, posY, sizeX, sizeY, dragon.getName());

        setText(getMessage().getString());
        setCanLoseFocus(true);
        changeFocus(true);
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
