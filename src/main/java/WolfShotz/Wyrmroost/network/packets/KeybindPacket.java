package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.network.IPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by WolfShotz - 8/9/2019 - 02:03
 * <p>
 * Class Handling the packet sending of keybind inputs.
 * keybinds are assigned an int, and as such follow the following format:
 */
public class KeybindPacket implements IPacket
{
    public static final int MOUNT_KEY1 = 1;
    public static final int MOUNT_KEY2 = 1 << 1;

    private final int key;
    private final int context;

    public KeybindPacket(int key, int context)
    {
        this.key = key;
        this.context = context;
        handle(ClientEvents.getPlayer()); // handle on the client to
    }

    public KeybindPacket(PacketBuffer buf)
    {
        this.key = buf.readInt();
        this.context = buf.readInt();
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeInt(key);
        buf.writeInt(context);
    }

    public void run(Supplier<NetworkEvent.Context> context) { handle(context.get().getSender()); }

    private void handle(PlayerEntity player)
    {
        switch (key)
        {
            case MOUNT_KEY1:
            case MOUNT_KEY2:
                Entity vehicle = player.getRidingEntity();
                if (vehicle instanceof AbstractDragonEntity)
                    ((AbstractDragonEntity) vehicle).recievePassengerKeybind(key, context);
                break;
            default:
                Wyrmroost.LOG.warn(String.format("uhh this is NOT what I was looking for. KeybindPacket with key: %s and context: %s", key, context));
        }
    }
}
