package WolfShotz.Wyrmroost.network.packets;

import WolfShotz.Wyrmroost.Wyrmroost;
import WolfShotz.Wyrmroost.client.ClientEvents;
import WolfShotz.Wyrmroost.entities.util.Animation;
import WolfShotz.Wyrmroost.entities.util.IAnimatedEntity;
import WolfShotz.Wyrmroost.network.IPacket;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Supplier;

public class AnimationPacket implements IPacket
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
    
    @Override
    public void encode(PacketBuffer buf)
    {
        buf.writeInt(entityID);
        buf.writeInt(animationIndex);
    }

    @Override
    public void run(Supplier<NetworkEvent.Context> context)
    {
        World world = ClientEvents.getClient().world;
        IAnimatedEntity entity = (IAnimatedEntity) world.getEntityByID(entityID);

        if (animationIndex < 0) entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
        else entity.setAnimation(entity.getAnimations()[animationIndex]);
    }

    public static <T extends Entity & IAnimatedEntity> void send(T entity, Animation animation)
    {
        entity.setAnimation(animation);
        Wyrmroost.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                new AnimationPacket(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
    }
}
