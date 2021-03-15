package com.github.wolfshotz.wyrmroost.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RenameEntityPacket
{
    private final UUID entity;
    private final ITextComponent text;

    public RenameEntityPacket(Entity entity, ITextComponent text)
    {
        this.entity = entity.getUuid();
        this.text = text;
    }

    public RenameEntityPacket(PacketBuffer buf)
    {
        this.entity = buf.readUuid();
        this.text = buf.readText();
    }
    
    public void encode(PacketBuffer buf)
    {
        buf.writeUuid(entity);
        buf.writeText(text);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().getSender().getServerWorld().getEntity(entity).setCustomName(text);
        return true;
    }
}
