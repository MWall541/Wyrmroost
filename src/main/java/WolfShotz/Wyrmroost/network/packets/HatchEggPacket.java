package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @deprecated todo: this is homosexual, rid of its filth.
 */
public class HatchEggPacket
{
    private final int entityID;

    public HatchEggPacket(DragonEggEntity entity) { this.entityID = entity.getEntityId(); }

    public HatchEggPacket(PacketBuffer buf) { this.entityID = buf.readInt(); }

    public void encode(PacketBuffer buf) { buf.writeInt(entityID); }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        ((DragonEggEntity) ClientEvents.getClient().world.getEntityByID(entityID)).hatch();
        return true;
    }
}
