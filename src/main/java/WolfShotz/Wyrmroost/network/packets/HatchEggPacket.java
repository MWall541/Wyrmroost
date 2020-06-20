package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HatchEggPacket implements IPacket
{
    private final int entityID;

    public HatchEggPacket(DragonEggEntity entity)
    {
        this.entityID = entity.getEntityId();
    }

    public HatchEggPacket(PacketBuffer buf)
    {
        this.entityID = buf.readInt();
    }
    
    public void encode(PacketBuffer buf)
    {
        buf.writeInt(entityID);
    }
    
    public void run(Supplier<NetworkEvent.Context> context)
    {
        World world = ClientEvents.getClient().world;
        
        ((DragonEggEntity) world.getEntityByID(entityID)).hatch();
    }
}
