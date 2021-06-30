package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
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

    public boolean handle(Supplier<NetworkEvent.Context> ctx)
    {
        Entity entity = ClientEvents.getLevel().getEntity(entityID);
        if (entity instanceof IAnimatable)
        {
            IAnimatable animatable = (IAnimatable) entity;
            if (animationIndex < 0) animatable.setAnimation(IAnimatable.NO_ANIMATION);
            else animatable.setAnimation(animatable.getAnimations()[animationIndex]);

            return true;
        }

        Wyrmroost.LOG.warn("Recieved invalid entity while handling animation packet: {}", entity);
        return false;
    }

    public static <T extends Entity & IAnimatable> void send(T entity, Animation animation)
    {
        if (!entity.level.isClientSide)
        {
            entity.setAnimation(animation);
            Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                    new AnimationPacket(entity.getId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
        }
    }
}
