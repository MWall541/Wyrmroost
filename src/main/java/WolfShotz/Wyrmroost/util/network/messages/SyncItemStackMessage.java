package WolfShotz.Wyrmroost.util.network.messages;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.ModUtils;
import WolfShotz.Wyrmroost.util.SyncedItemStackHandler;
import WolfShotz.Wyrmroost.util.network.IMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncItemStackMessage implements IMessage
{
    private SyncedItemStackHandler handler;
    private int dragonID;

    public SyncItemStackMessage(AbstractDragonEntity dragon)
    {
        this.handler = dragon.invHandler.orElse(new SyncedItemStackHandler());
        this.dragonID = dragon.getEntityId();
    }

    public SyncItemStackMessage(PacketBuffer buf)
    {
        this.dragonID = buf.readInt();
        this.handler = new SyncedItemStackHandler();
        this.handler.deserializeNBT(buf.readCompoundTag());
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeInt(dragonID);
        buf.writeCompoundTag(handler.serializeNBT());
    }

    public void run(Supplier<NetworkEvent.Context> context)
    {
        ((AbstractDragonEntity) ModUtils.getClientWorld().getEntityByID(dragonID)).invHandler = LazyOptional.of(() -> handler);
    }
}
