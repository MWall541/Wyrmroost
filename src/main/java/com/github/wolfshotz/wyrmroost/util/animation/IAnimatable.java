package com.github.wolfshotz.wyrmroost.util.animation;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public interface IAnimatable
{
    Animation NO_ANIMATION = new Animation(0);

    int getAnimationTick();

    void setAnimationTick(int tick);

    Animation getAnimation();

    void setAnimation(Animation animation);

    Animation[] getAnimations();

    default boolean noAnimations()
    {
        return getAnimation() == NO_ANIMATION;
    }

    default void updateAnimations()
    {
        Animation current = getAnimation();
        if (current != NO_ANIMATION)
        {
            int tick = getAnimationTick();
            current.tick(this, tick);
            if (++tick >= current.getDuration())
            {
                setAnimation(NO_ANIMATION);
                tick = 0;
            }
            setAnimationTick(tick);
        }
    }

    static void registerCapability()
    {
        CapabilityManager.INSTANCE.register(IAnimatable.class, new Capability.IStorage<IAnimatable>()
        {
            // There is no data needed to be stored.
            @Nullable
            @Override
            public INBT writeNBT(Capability<IAnimatable> capability, IAnimatable instance, Direction side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<IAnimatable> capability, IAnimatable instance, Direction side, INBT nbt)
            {
            }
        }, CapImpl::new);
    }

    class CapImpl implements IAnimatable
    {
        @CapabilityInject(IAnimatable.class)
        public static final Capability<IAnimatable> CAPABILITY = null;

        private int animationTick = 0;
        private Animation animation;

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
