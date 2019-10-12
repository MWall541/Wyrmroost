package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HatchMessage
{
    private int entityID;
    
    public HatchMessage(DragonEggEntity entity) {
        this.entityID = entity.getEntityId();
    }
    
    public HatchMessage(PacketBuffer buf) {
        entityID = buf.readInt();
    }
    
    public void encode(PacketBuffer buf) {
        buf.writeInt(entityID);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        World world = DistExecutor.callWhenOn(Dist.CLIENT, () -> ModUtils::getClientWorld);
        
        ((DragonEggEntity) world.getEntityByID(entityID)).hatch();
        context.get().setPacketHandled(true);
    }
}
