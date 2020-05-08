package WolfShotz.Wyrmroost.content.io.screen.staff;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.DragonStaffItem;
import WolfShotz.Wyrmroost.network.messages.StaffActionMessage;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.ItemStack;

public class StaffModeButton extends AbstractButton
{
    private final DragonStaffItem.Action action;

    public StaffModeButton(int xIn, int yIn, int widthIn, int heightIn, String msg, DragonStaffItem.Action action)
    {
        super(xIn, yIn, widthIn, heightIn, msg);
        this.action = action;
    }

    @Override
    public void onPress()
    {
        ItemStack stack = ModUtils.getHeldStack(Minecraft.getInstance().player, WRItems.DRAGON_STAFF.get());
        if (stack.getItem() == WRItems.DRAGON_STAFF.get())
        {
            DragonStaffItem staff = (DragonStaffItem) stack.getItem();
            staff.setAction(action, Minecraft.getInstance().world, stack);
            Wyrmroost.NETWORK.sendToServer(new StaffActionMessage(action));
        }
        Minecraft.getInstance().displayGuiScreen(null);
    }
}
