package WolfShotz.Wyrmroost.util.network.messages;

import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.network.IMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EggHatchMessage implements IMessage
{
    private int entityID;
    
    public EggHatchMessage(DragonEggEntity entity)
    {
        this.entityID = entity.getEntityId();
    }
    
    public EggHatchMessage(PacketBuffer buf)
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
