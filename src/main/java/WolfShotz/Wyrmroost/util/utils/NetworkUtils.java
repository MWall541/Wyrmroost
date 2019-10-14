package WolfShotz.Wyrmroost.util.utils;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragonegg.DragonEggEntity;
import WolfShotz.Wyrmroost.util.network.AnimationMessage;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

public class NetworkUtils
{
    public static <T extends Entity & IAnimatedEntity> void sendAnimationPacket(T entity, Animation animation) {
        if (entity.world.isRemote) return; // Why are we even sending this then...?
        
        AnimationMessage message = new AnimationMessage(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation));
        
        entity.setAnimation(animation);
        Wyrmroost.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
