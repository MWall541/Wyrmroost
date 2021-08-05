package com.github.wolfshotz.wyrmroost.items.staff;

import com.github.wolfshotz.wyrmroost.client.render.TarragonTomeRenderer;
import com.github.wolfshotz.wyrmroost.registry.WRItems;
import com.github.wolfshotz.wyrmroost.util.LerpedFloat;
import com.github.wolfshotz.wyrmroost.util.ModUtils;
import com.github.wolfshotz.wyrmroost.util.animation.IAnimatable;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TarragonTomeItem extends Item
{
    public TarragonTomeItem()
    {
        super(WRItems.builder().stacksTo(1).rarity(Rarity.RARE).setISTER(() -> TarragonTomeRenderer::new));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new AnimationCap();
    }

    @Override
    public void inventoryTick(ItemStack stack, World level, Entity entity, int invIndex, boolean holding)
    {
        if (holding) ((AnimationCap) ModUtils.getCapabilityInstance(IAnimatable.CapImpl.CAPABILITY, stack)).tick();
    }

    public static class AnimationCap extends IAnimatable.CapImpl implements ICapabilityProvider
    {
        private final LazyOptional<IAnimatable> instance = LazyOptional.of(() -> this);
        public final LerpedFloat flipTime = new LerpedFloat();
        public float flipDuration;
        public float flipA;

        public void tick()
        {
            if (random.nextDouble() < 0.075) flipDuration += random.nextInt(4) - random.nextInt(4);
            float f = MathHelper.clamp((flipDuration - flipTime.get()) * 0.4f, -0.2f, 0.2f);
            flipA += (f - flipA) * 0.9F;
            flipTime.add(flipA);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return CapImpl.CAPABILITY.orEmpty(cap, instance);
        }
    }
}
