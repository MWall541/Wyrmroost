package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.network.IMessage;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HatchEggPacket implements IMessage
{
    private int entityID;

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
        World world = DistExecutor.callWhenOn(Dist.CLIENT, () -> ModUtils::getClientWorld);
        
        ((DragonEggEntity) world.getEntityByID(entityID)).hatch();
    }
}
