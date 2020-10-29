package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.items.staff.DragonStaffItem;
import com.github.wolfshotz.wyrmroost.items.staff.StaffAction;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StaffActionPacket
{
    public final StaffAction action;

    public StaffActionPacket(StaffAction action) { this.action = action; }

    public StaffActionPacket(PacketBuffer buf) { action = StaffAction.VALUES[buf.readInt()]; }

    public void encode(PacketBuffer buf) { buf.writeInt(action.ordinal()); }

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
