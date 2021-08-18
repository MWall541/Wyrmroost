package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.entities.dragon.TameableDragonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class KeybindHandler
{
    public static final byte MOUNT_KEY = 1;
    public static final byte ALT_MOUNT_KEY = 2;
    public static final byte SWITCH_FLIGHT = 4;

    private final byte key;
    private final int mods;
    private final boolean pressed;

    public KeybindHandler(byte key, int mods, boolean pressed)
    {
        this.key = key;
        this.mods = mods;
        this.pressed = pressed;
    }

    public KeybindHandler(PacketBuffer buf)
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
            case MOUNT_KEY:
            case ALT_MOUNT_KEY:
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
                    boolean b = ClientEvents.keybindFlight = !ClientEvents.keybindFlight;
                    String translate = "entity.wyrmroost.dragons.flight." + (b? "controlled" : "free");
                    ClientEvents.getPlayer().displayClientMessage(new TranslationTextComponent(translate), true);
                }
                break;
            default:
                Wyrmroost.LOG.warn(String.format("Recieved invalid keybind code: %s How tf did u break this", key));
                return false;
        }
        return true;
    }
}
