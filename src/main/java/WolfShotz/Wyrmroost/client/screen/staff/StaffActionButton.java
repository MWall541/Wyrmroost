package WolfShotz.Wyrmroost.client.screen.staff;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.content.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.messages.StaffActionMessage;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class StaffActionButton extends AbstractButton
{
    private final StaffAction action;

    public StaffActionButton(int xIn, int yIn, String msg, StaffAction action)
    {
        super(xIn, yIn, 100, 20, msg);
        this.action = action;
    }

    @Override
    public void onPress()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack.getItem() == WRItems.DRAGON_STAFF.get())
        {
            DragonStaffItem.setAction(action, player, stack);
            Wyrmroost.NETWORK.sendToServer(new StaffActionMessage(action));
        }
        Minecraft.getInstance().displayGuiScreen(null);
    }
}
