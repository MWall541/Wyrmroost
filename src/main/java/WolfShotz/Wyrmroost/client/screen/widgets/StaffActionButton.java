package WolfShotz.Wyrmroost.client.screen.widgets;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.network.packets.StaffActionPacket;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.TickFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class StaffActionButton extends AbstractButton
{
    public final StaffAction action;
    public final TickFloat focusTime = new TickFloat().setLimit(0, 6);
    public boolean wasHovered = false;

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
            Wyrmroost.NETWORK.sendToServer(new StaffActionPacket(action));
        }
        Minecraft.getInstance().displayGuiScreen(null);
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float partialTick)
    {
        focusTime.add(isHovered? 1 : -1);

        if (wasHovered != isHovered)
        {
            onFocusedChanged(isHovered);
            wasHovered = isHovered;
        }

        drawCenteredString(Minecraft.getInstance().fontRenderer,
                getMessage(),
                x + width / 2,
                (y + (height - 8) / 2) + (int) -focusTime.get(partialTick),
                (int) MathHelper.lerp(focusTime.get(partialTick), 0xffffff, 0xfffd8a));
    }

    @Override
    protected void onFocusedChanged(boolean focusing)
    {
        if (focusing)
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.BLOCK_NOTE_BLOCK_BASS, -1f));
    }
}
