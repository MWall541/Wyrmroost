package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.items.staff.DragonStaffItem;
import WolfShotz.Wyrmroost.items.staff.StaffAction;
import WolfShotz.Wyrmroost.registry.WRItems;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StaffActionPacket
{
    public final StaffAction action;

    public StaffActionPacket(StaffAction action) { this.action = action; }

    public StaffActionPacket(PacketBuffer buf) { action = StaffAction.ACTIONS.get(buf.readInt()); }

    public void encode(PacketBuffer buf) { buf.writeInt(StaffAction.ACTIONS.indexOf(action)); }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        ServerPlayerEntity player = context.get().getSender();
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.DRAGON_STAFF.get());
        if (stack != null)
        {
            DragonStaffItem.setAction(action, player, stack);
            return true;
        }
        return false;
    }
}
