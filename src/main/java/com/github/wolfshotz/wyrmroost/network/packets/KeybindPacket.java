package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class KeybindPacket
{
    public static final byte MOUNT_KEY1 = 1;
    public static final byte MOUNT_KEY2 = 2;
    public static final byte SWITCH_FLIGHT = 4;

    private final byte key;
    private final int mods;
    private final boolean pressed;

    public KeybindPacket(byte key, int mods, boolean pressed)
    {
        this.key = key;
        this.mods = mods;
        this.pressed = pressed;
    }

    public KeybindPacket(PacketBuffer buf)
    {
        this.key = buf.readByte();
        this.mods = buf.readInt();
        this.pressed = buf.readBoolean();
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeByte(key);
        buf.writeInt(mods);
        buf.writeBoolean(pressed);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        return process(context.get().getSender());
    }

    public boolean process(PlayerEntity player)
    {
        switch (key)
        {
            case MOUNT_KEY1:
            case MOUNT_KEY2:
                Entity vehicle = player.getVehicle();
                if (vehicle instanceof TameableDragonEntity)
                {
                    TameableDragonEntity dragon = ((TameableDragonEntity) vehicle);
                    if (dragon.isTame() && dragon.getControllingPlayer() == player)
                        dragon.recievePassengerKeybind(key, mods, pressed);
                }
                break;
            case SWITCH_FLIGHT:
                if (!pressed)
                {
                    ClientEvents.getPlayer().displayClientMessage(new StringTextComponent((ClientEvents.keybindFlight = !ClientEvents.keybindFlight)? "Free flight" : "Manual flight"), true);
                }
                break;
            default:
                Wyrmroost.LOG.warn(String.format("Recieved invalid keybind code: %s How tf did u break this", key));
                return false;
        }
        return true;
    }
}
