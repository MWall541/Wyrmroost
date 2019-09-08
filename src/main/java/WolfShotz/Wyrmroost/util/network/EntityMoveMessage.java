package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EntityMoveMessage
{
    private int entityID;
    private double x;
    private double y;
    private double z;
    
    public EntityMoveMessage(Entity entity) {
        this.entityID = entity.getEntityId();
        this.x = entity.getMotion().x;
        this.y = entity.getMotion().y;
        this.z = entity.getMotion().z;
        
        System.out.println(x + " | " + z);
    }
    
    public EntityMoveMessage(PacketBuffer buf) {
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
        if (context.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) return;
        World world = ModUtils.getClientWorld();
        Entity entity = world.getEntityByID(entityID);
        if (entity == null) return;
        
        System.out.println("test");
        
        entity.setMotion(x, y, z);
    }
    
    public EntityMoveMessage setMotion(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        
        return this;
    }
}
