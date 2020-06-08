package WolfShotz.Wyrmroost.network;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.entities.util.Animation;
import WolfShotz.Wyrmroost.entities.util.IAnimatedEntity;
import WolfShotz.Wyrmroost.network.packets.*;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;

public class NetworkUtils
{
    private static int messageIndex;

    public static void registerPackets()
    {
        register(AnimationPacket.class);
        register(KeybindPacket.class);
        register(HatchEggPacket.class);
        register(RenameEntityPacket.class);
        register(StaffActionPacket.class);
    }

    public static <T extends IMessage> void register(Class<T> clazz)
    {
        ++messageIndex;
        Wyrmroost.NETWORK.registerMessage(messageIndex,
                clazz,
                T::encode,
                buf ->
                {
                    try { return clazz.getConstructor(PacketBuffer.class).newInstance(buf); }
                    catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
                    {
                        throw new RuntimeException(String.format("Invalid/Missing Constructor in packet class: %s [] %s", clazz.toString(), e));
                    }
                }, T::handle);
    }

    public static <T extends Entity & IAnimatedEntity> void sendAnimationPacket(T entity, Animation animation)
    {
        if (entity.world.isRemote) return; // Why are we even sending this then...?

        AnimationPacket message = new AnimationPacket(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation));

        entity.setAnimation(animation);
        Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
