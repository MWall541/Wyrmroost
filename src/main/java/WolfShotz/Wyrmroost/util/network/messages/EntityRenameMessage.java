package WolfShotz.Wyrmroost.util.network.messages;

import WolfShotz.Wyrmroost.util.network.IMessage;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class EntityRenameMessage implements IMessage
{
    private final UUID entity;
    private final ITextComponent text;
    
    public EntityRenameMessage(Entity entity, ITextComponent text) {
        this.entity = entity.getUniqueID();
        this.text = text;
    }
    
    public EntityRenameMessage(PacketBuffer buf) {
        this.entity = buf.readUniqueId();
        this.text = buf.readTextComponent();
    }
    
    @Override
    public void encode(PacketBuffer buf) {
        buf.writeUniqueId(entity);
        buf.writeTextComponent(text);
    }
    
    @Override
    public void run(Supplier<NetworkEvent.Context> context) {
        context.get().getSender().getServerWorld().getEntityByUuid(entity).setCustomName(text);
    }
}
