package WolfShotz.Wyrmroost.entities.util.animation;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityAnimationHandler
{
    @CapabilityInject(IAnimatable.class)
    public static Capability<IAnimatable> ANIMATABLE_CAPABILITY = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IAnimatable.class, new Capability.IStorage<IAnimatable>()
        {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IAnimatable> capability, IAnimatable instance, Direction side) { return null;}

            @Override
            public void readNBT(Capability<IAnimatable> capability, IAnimatable instance, Direction side, INBT nbt) {}
        }, Holder::new);
    }

    public static class Holder implements IAnimatable
    {
        private int animationTick;
        private Animation animation = IAnimatable.NO_ANIMATION;

        @Override
        public int getAnimationTick()
        {
            return animationTick;
        }

        @Override
        public void setAnimationTick(int tick)
        {
            this.animationTick = tick;
        }

        @Override
        public Animation getAnimation()
        {
            return animation;
        }

        @Override
        public void setAnimation(Animation animation)
        {
            this.animation = animation;
        }

        @Override
        public Animation[] getAnimations()
        {
            return new Animation[0];
        }
    }
}
