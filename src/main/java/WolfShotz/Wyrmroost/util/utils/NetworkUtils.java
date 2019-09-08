package WolfShotz.Wyrmroost.util.utils;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.network.AnimationMessage;
import WolfShotz.Wyrmroost.util.network.EntityMoveMessage;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
