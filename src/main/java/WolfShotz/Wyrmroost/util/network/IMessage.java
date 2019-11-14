package WolfShotz.Wyrmroost.util.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessage
{
    void encode(PacketBuffer buf);
    
    void handle(Supplier<NetworkEvent.Context> context);
}
