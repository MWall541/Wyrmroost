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
    public static void sendAnimationPacket(Entity entityIn, Animation animation) {
        if (entityIn.world.isRemote) return; // Why are we even sending this then...?
        if (!(entityIn instanceof IAnimatedEntity)) return;
        IAnimatedEntity animEntity = (IAnimatedEntity) entityIn;
        
        AnimationMessage message = new AnimationMessage(entityIn.getEntity().getEntityId(), ArrayUtils.indexOf(animEntity.getAnimations(), animation));
    
        animEntity.setAnimation(animation);
        Wyrmroost.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> entityIn), message);
    }
}
