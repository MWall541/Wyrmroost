package WolfShotz.Wyrmroost.util;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.AbstractDragonEntity;
import WolfShotz.Wyrmroost.network.AnimationMessage;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

public class NetworkUtils
{
    public static void sendAnimationPacket(AbstractDragonEntity dragon, Animation animation) {
        if (dragon.world.isRemote) return; // Why are we even sending this then...?
        
        AnimationMessage message = new AnimationMessage(dragon, ArrayUtils.indexOf(dragon.getAnimations(), animation));
    
        dragon.setAnimation(animation);
        Wyrmroost.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> dragon), message);
    }
}
