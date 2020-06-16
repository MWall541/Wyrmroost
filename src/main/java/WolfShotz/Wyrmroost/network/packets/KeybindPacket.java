package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.network.IMessage;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by WolfShotz - 8/9/2019 - 02:03
 * <p>
 * Class Handling the packet sending of keybind inputs.
 * keybinds are assigned an int, and as such follow the following format:
 * 0: Generic Attack
 * 1: Special Attack start
 * 2: Special Attack end
 * 3: Call Dragon
 */
public class KeybindPacket implements IMessage
{
    public static final int PRIMARY_ATTACK = 1;
    public static final int SECONDARY_ATTACK = 2;

    private final int key;

    public KeybindPacket(int key)
    {
        this.key = key;
    }

    public KeybindPacket(PacketBuffer buf) { key = buf.readInt(); }

    public void encode(PacketBuffer buf) { buf.writeInt(key); }
    
    public void run(Supplier<NetworkEvent.Context> context)
    {
        Entity riding = context.get().getSender().getRidingEntity();
        if (riding instanceof AbstractDragonEntity) ((AbstractDragonEntity) riding).recievePassengerKeybind(key);
    }
}
