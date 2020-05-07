package WolfShotz.Wyrmroost.network.messages;

import WolfShotz.Wyrmroost.content.items.DragonStaffItem;
import WolfShotz.Wyrmroost.network.IMessage;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StaffModeMessage implements IMessage
{
    public final DragonStaffItem.Mode mode;

    public StaffModeMessage(DragonStaffItem item) { this.mode = item.mode; }

    public StaffModeMessage(PacketBuffer buf)
    {
        mode = buf.readEnumValue(DragonStaffItem.Mode.class);
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        buf.writeEnumValue(mode);
    }

    @Override
    public void run(Supplier<NetworkEvent.Context> context)
    {
        ServerPlayerEntity player = context.get().getSender();
        if (player == null) return;
        ItemStack stack = ModUtils.getHeldStack(context.get().getSender(), WRItems.DRAGON_STAFF.get());
        if (stack.getItem() == WRItems.DRAGON_STAFF.get())
        {
            DragonStaffItem staff = (DragonStaffItem) stack.getItem();
            staff.setMode(mode);
        }
    }
}
