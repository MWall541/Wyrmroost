package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.items.book.TarragonTomeItem;
import com.github.wolfshotz.wyrmroost.items.book.action.BookAction;
import com.github.wolfshotz.wyrmroost.items.book.action.BookActions;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BookActionPacket
{
    public final BookAction action;

    public BookActionPacket(BookAction action)
    {
        this.action = action;
    }

    public BookActionPacket(PacketBuffer buf)
    {
        action = BookActions.ACTIONS.get(buf.readInt());
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeInt(BookActions.ACTIONS.indexOf(action));
    }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        ServerPlayerEntity player = context.get().getSender();
        ItemStack stack = ModUtils.getHeldStack(player, WRItems.TARRAGON_TOME.get());
        if (stack != null)
        {
            TarragonTomeItem.setAction(action, player, stack);
            return true;
        }
        return false;
    }
}
