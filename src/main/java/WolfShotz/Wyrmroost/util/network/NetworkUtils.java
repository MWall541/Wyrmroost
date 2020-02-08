package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.Animation;
import WolfShotz.Wyrmroost.util.entityutils.client.animation.IAnimatedObject;
import WolfShotz.Wyrmroost.util.network.messages.AnimationMessage;
import WolfShotz.Wyrmroost.util.network.messages.DragonKeyBindMessage;
import WolfShotz.Wyrmroost.util.network.messages.EggHatchMessage;
import WolfShotz.Wyrmroost.util.network.messages.EntityRenameMessage;
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
    }
    
    public static <T extends IMessage> void registerMSG(Class<T> clazz, Function<PacketBuffer, T> decoder)
    {
        ++messageIndex;
        Wyrmroost.NETWORK.registerMessage(messageIndex, clazz, IMessage::encode, decoder, IMessage::handle);
    }
    
    public static <T extends Entity & IAnimatedObject> void sendAnimationPacket(T entity, Animation animation)
    {
        if (entity.world.isRemote) return; // Why are we even sending this then...?
        
        AnimationMessage message = new AnimationMessage(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation));
        
        entity.setAnimation(animation);
        Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
