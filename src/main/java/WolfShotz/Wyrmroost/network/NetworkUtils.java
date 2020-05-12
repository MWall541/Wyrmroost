package WolfShotz.Wyrmroost.network;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.animation.Animation;
import WolfShotz.Wyrmroost.client.animation.IAnimatedEntity;
import WolfShotz.Wyrmroost.network.messages.*;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;

public class NetworkUtils
{
    private static int messageIndex;
    
    public static void registerMessages()
    {
        registerMSG(AnimationMessage.class, AnimationMessage::new);
        registerMSG(DragonKeyBindMessage.class, DragonKeyBindMessage::new);
        registerMSG(EggHatchMessage.class, EggHatchMessage::new);
        registerMSG(EntityRenameMessage.class, EntityRenameMessage::new);
        registerMSG(StaffActionMessage.class, StaffActionMessage::new);
    }
    
    public static <T extends IMessage> void registerMSG(Class<T> clazz, Function<PacketBuffer, T> decoder)
    {
        ++messageIndex;
        Wyrmroost.NETWORK.registerMessage(messageIndex, clazz, IMessage::encode, decoder, IMessage::handle);
    }

    public static <T extends Entity & IAnimatedEntity> void sendAnimationPacket(T entity, Animation animation)
    {
        if (entity.world.isRemote) return; // Why are we even sending this then...?

        AnimationMessage message = new AnimationMessage(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation));

        entity.setAnimation(animation);
        Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
