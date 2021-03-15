package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.entities.dragon.SilverGliderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SGGlidePacket
{
    private final boolean gliding;

    public SGGlidePacket(PacketBuffer buffer)
    {
        this.gliding = buffer.readBoolean();
    }

    public SGGlidePacket(boolean gliding)
    {
        this.gliding = gliding;
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeBoolean(gliding);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        ServerPlayerEntity reciever = context.get().getSender();
        if (reciever != null && !reciever.getPassengerList().isEmpty())
        {
            Entity entity = reciever.getPassengerList().get(0);
            if (entity instanceof SilverGliderEntity)
            {
                ((SilverGliderEntity) entity).isGliding = gliding;
                return true;
            }
        }
        return false;
    }

    public static void send(boolean gliding)
    {
        Wyrmroost.NETWORK.sendToServer(new SGGlidePacket(gliding));
    }
}
