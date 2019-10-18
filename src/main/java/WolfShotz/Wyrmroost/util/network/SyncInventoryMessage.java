package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Supplier;

public class SyncInventoryMessage
{
    int entityID;
    private CompoundNBT tag;
    
    public SyncInventoryMessage(int entityID, CompoundNBT tag) {
        this.tag = tag;
        this.entityID = entityID;
    }
    
    public SyncInventoryMessage(PacketBuffer buf) {
        entityID = buf.readInt();
        tag      = buf.readCompoundTag();
    }
    
    public void encode(PacketBuffer buf) {
        buf.writeInt(entityID);
        buf.writeCompoundTag(tag);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) return;
        World world = ModUtils.getClientWorld();
        Entity entity = world.getEntityByID(entityID);
        if (!(entity instanceof AbstractDragonEntity)) return;
        
        ((AbstractDragonEntity) entity).invHandler.ifPresent(i -> ((ItemStackHandler) i).deserializeNBT(tag));
    }
}
