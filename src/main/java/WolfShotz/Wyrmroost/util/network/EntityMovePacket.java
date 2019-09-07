package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EntityMovePacket
{
    private int entityID;
    private double x;
    private double y;
    private double z;
    
    public EntityMovePacket(Entity entity) {
        this.entityID = entity.getEntityId();
        this.x = entity.getMotion().x;
        this.y = entity.getMotion().y;
        this.z = entity.getMotion().z;
    }
    
    public EntityMovePacket(PacketBuffer buf) {
        entityID = buf.readInt();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }
    
    public void encode(PacketBuffer buf) {
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        World world = DistExecutor.callWhenOn(Dist.CLIENT, () -> ModUtils::getClientWorld);
        
        
    }
}
