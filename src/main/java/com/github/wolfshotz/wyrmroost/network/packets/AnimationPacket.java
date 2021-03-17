package com.github.wolfshotz.wyrmroost.network.packets;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.ClientEvents;
import com.github.wolfshotz.wyrmroost.util.animation.Animation;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
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
        if (!entity.world.isClientSide)
        {
            entity.setAnimation(animation);
            Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                    new AnimationPacket(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
        }
    }
}
