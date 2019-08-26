package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AnimationMessage
{
    private int dragonID;
    private int animationIndex;
    
    public AnimationMessage(AbstractDragonEntity entity, int index) {
        this.dragonID = entity.getEntityId();
        this.animationIndex = index;
    }
    
    public AnimationMessage(PacketBuffer buf) {
        dragonID = buf.readInt();
        animationIndex = buf.readInt();
    }
    
    public void encode(PacketBuffer buf) {
        buf.writeInt(dragonID);
        buf.writeInt(animationIndex);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        World world = DistExecutor.callWhenOn(Dist.CLIENT, () -> ModUtils::getClientWorld);
        AbstractDragonEntity dragon = (AbstractDragonEntity) world.getEntityByID(dragonID);
        
        if (animationIndex < 0) dragon.setAnimation(IAnimatedEntity.NO_ANIMATION);
        else dragon.setAnimation(dragon.getAnimations()[animationIndex]);
    }
}
