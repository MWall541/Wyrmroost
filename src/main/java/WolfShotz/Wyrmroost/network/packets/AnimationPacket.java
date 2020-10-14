package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.util.animation.Animation;
import WolfShotz.Wyrmroost.entities.util.animation.IAnimatable;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Supplier;

public class AnimationPacket
{
    private final int entityID, animationIndex;

    public AnimationPacket(int entityID, int index)
    {
        this.entityID = entityID;
        this.animationIndex = index;
    }

    public AnimationPacket(PacketBuffer buf)
    {
        entityID = buf.readInt();
        animationIndex = buf.readInt();
    }
    
    public void encode(PacketBuffer buf)
    {
        buf.writeInt(entityID);
        buf.writeInt(animationIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context)
    {
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> ClientEvents.handleAnimationPacket(entityID, animationIndex));
    }

    public static <T extends Entity & IAnimatable> void send(T entity, Animation animation)
    {
        if (!entity.world.isRemote)
        {
            entity.setAnimation(animation);
            Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                    new AnimationPacket(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
        }
    }
}
