package WolfShotz.Wyrmroost.util.utils;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.network.AnimationMessage;
import WolfShotz.Wyrmroost.util.network.IMessage;
import WolfShotz.Wyrmroost.util.network.SyncInventoryMessage;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;

public class NetworkUtils
{
    private static int messageIndex;
    
    public static <T extends IMessage> void registerMSG(Class<T> clazz, Function<PacketBuffer, T> decoder) {
        ++messageIndex;
        Wyrmroost.network.registerMessage(messageIndex, clazz, T::encode, decoder, T::handle);
    }
    
    public static <T extends Entity & IAnimatedEntity> void sendAnimationPacket(T entity, Animation animation) {
        if (entity.world.isRemote) return; // Why are we even sending this then...?
        
        AnimationMessage message = new AnimationMessage(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation));
        
        entity.setAnimation(animation);
        Wyrmroost.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
    
    public static void syncInventory(AbstractDragonEntity dragon, CompoundNBT tag) {
        Wyrmroost.network.send(PacketDistributor.ALL.noArg(), new SyncInventoryMessage(dragon.getEntityId(), tag));
    }
}
