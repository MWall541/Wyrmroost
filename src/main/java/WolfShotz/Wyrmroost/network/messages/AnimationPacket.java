package WolfShotz.Wyrmroost.network.messages;

import WolfShotz.Wyrmroost.client.animation.IAnimatedEntity;
import WolfShotz.Wyrmroost.network.IMessage;
import WolfShotz.Wyrmroost.util.ModUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AnimationPacket implements IMessage
{
    private int entityID;
    private int animationIndex;

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
        World world = DistExecutor.callWhenOn(Dist.CLIENT, () -> ModUtils::getClientWorld);
        IAnimatedEntity entity = (IAnimatedEntity) world.getEntityByID(entityID);

        if (animationIndex < 0) entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
        else entity.setAnimation(entity.getAnimations()[animationIndex]);
    }
}
