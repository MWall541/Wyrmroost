package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.dragon.SilverGliderEntity;
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
        if (reciever != null && !reciever.getPassengers().isEmpty())
        {
            Entity entity = reciever.getPassengers().get(0);
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
