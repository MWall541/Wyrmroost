package WolfShotz.Wyrmroost.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessage
{
    void encode(PacketBuffer buf);
    
    default void run(Supplier<NetworkEvent.Context> context)
    {
    }
    
    default void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> run(context));
        context.get().setPacketHandled(true);
    }
}
